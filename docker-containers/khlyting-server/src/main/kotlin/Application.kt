import com.beust.klaxon.Klaxon
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = 8080;
    val host = "0.0.0.0";

    embeddedServer(Netty, port = port, host = host) {

        val moneyServer = MoneyServer()

        class MissingParameterException(name: String) : Exception("expected parameter: $name!")

        routing {

            get("/get-paper-currency") {
                try {
                    val parameters = call.request.queryParameters
                    call.respond(
                        HttpStatusCode.OK,
                        Klaxon().toJsonString(
                            moneyServer.getPaperCurrencyByCompany(
                                parameters["companyName"] ?: throw MissingParameterException("companyName")
                            )
                        )
                    )
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

            post("/add-paper-currency") {
                try {
                    val parameters = call.request.queryParameters
                    moneyServer.addPaperCurrency(
                        parameters["companyName"] ?: throw MissingParameterException("companyName"),
                        (parameters["quantity"] ?: throw MissingParameterException("quantity")).toInt()
                    )
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

            get("/get-paper-currency-all") {
                try {
                    call.respond(
                        HttpStatusCode.OK,
                        Klaxon().toJsonString(
                            moneyServer.getCompanyToPaperCurrencyMap()
                        )
                    )
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

            post("/buy-paper-currency") {
                try {
                    val parameters = call.request.queryParameters
                    call.respond(
                        HttpStatusCode.OK,
                        moneyServer.buyPaperCurrency(
                            parameters["companyName"] ?: throw MissingParameterException("companyName"),
                            (parameters["quantity"] ?: throw MissingParameterException("quantity")).toInt()
                        ).toString()
                    )
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

            post("/change-price") {
                try {
                    val parameters = call.request.queryParameters
                    moneyServer.changePrice(
                        parameters["companyName"] ?: throw MissingParameterException("companyName"),
                        (parameters["price"] ?: throw MissingParameterException("price")).toDouble()
                    )
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

            post("/add-company") {
                try {
                    val parameters = call.request.queryParameters
                    moneyServer.addCompany(
                        parameters["companyName"] ?: throw MissingParameterException("companyName"),
                        (parameters["price"] ?: throw MissingParameterException("price")).toDouble()
                    )
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "")
                }
            }

        }

    }.start(wait = true)

}
