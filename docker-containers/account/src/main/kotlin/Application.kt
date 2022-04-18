import com.beust.klaxon.Klaxon
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = 8081;
    val host = "0.0.0.0";

    embeddedServer(Netty, port = port, host = host) {

        module1()

    }.start(wait = true)

}

fun Application.module1() {

    val account = Account()

    val moneyClient = MoneyClient("http://0.0.0.0:8080", account)

    class MissingParameterException(name: String) : Exception("expected parameter: $name!")

    suspend fun getUserPaperCurrencies(user: User): List<PaperCurrency> {
        val userPaperCurrenciesMap = user.paperCurrencies.map {
            val paperCurrencyInfo = moneyClient.getPaperCurrenciesInfo(it.key)
            paperCurrencyInfo.quantity = it.value
            paperCurrencyInfo
        }
        return userPaperCurrenciesMap
    }

    routing {

        get("/get-paper-currency") {
            try {
                val parameters = call.request.queryParameters
                call.respond(
                    HttpStatusCode.OK,
                    Klaxon().toJsonString(
                        getUserPaperCurrencies(
                            account.getUser(
                                (parameters["userId"] ?: throw MissingParameterException("userId")).toInt()
                            )
                        )
                    )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

        post("/add-user") {
            try {
                val parameters = call.request.queryParameters
                call.respond(
                    HttpStatusCode.OK,
                    account.createUser(
                        parameters["name"] ?: throw MissingParameterException("name")
                    ).toString()
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

        post("/add-money") {
            try {
                val parameters = call.request.queryParameters
                account.addMoney(
                    (parameters["userId"] ?: throw MissingParameterException("userId")).toInt(),
                    (parameters["amount"] ?: throw MissingParameterException("amount")).toDouble()
                )
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

        get("/get-total-money") {
            try {
                val parameters = call.request.queryParameters
                val user = account.getUser(
                    (parameters["userId"] ?: throw MissingParameterException("userId")).toInt()
                )
                call.respond(
                    HttpStatusCode.OK,
                    (getUserPaperCurrencies(user).sumOf { it.quantity * it.price } + user.money).toString()
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

        post("/buy") {
            try {
                val parameters = call.request.queryParameters
                moneyClient.buy(
                    (parameters["userId"] ?: throw MissingParameterException("userId")).toInt(),
                    (parameters["companyName"] ?: throw MissingParameterException("companyName")),
                    (parameters["quantity"] ?: throw MissingParameterException("quantity")).toInt()
                )
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

        post("/sell") {
            try {
                val parameters = call.request.queryParameters
                moneyClient.sell(
                    (parameters["userId"] ?: throw MissingParameterException("userId")).toInt(),
                    (parameters["companyName"] ?: throw MissingParameterException("companyName")),
                    (parameters["quantity"] ?: throw MissingParameterException("quantity")).toInt()
                )
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }

    }

}
