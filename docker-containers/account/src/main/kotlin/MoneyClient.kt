import com.beust.klaxon.Klaxon
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class MoneyClient(private val url: String, private val account: Account) {
    private val client = HttpClient(CIO)

    suspend fun getPaperCurrenciesInfo(companyName: String): PaperCurrency {
        val json = client.get<String>("$url/get-paper-currency?companyName=$companyName")
        return Klaxon().parse<PaperCurrency>(json)!!
    }

    suspend fun buy(userId: Int, companyName: String, quantity: Int) {
        val paperCurrencyInfo = getPaperCurrenciesInfo(companyName)
        if (quantity <= 0) {
            throw Account.AccountException("expected positive quantity paper currency!")
        }
        if (paperCurrencyInfo.quantity < quantity) {
            throw Account.AccountException("only ${paperCurrencyInfo.quantity} paper currency available!")
        }
        account.withdrawMoney(userId, paperCurrencyInfo.price * quantity)
        client.post<HttpResponse>("$url/buy-paper-currency?companyName=$companyName&quantity=$quantity")
        account.addPaperCurrency(userId, companyName, quantity)
    }

    suspend fun sell(userId: Int, companyName: String, quantity: Int) {
        val paperCurrencyInfo = getPaperCurrenciesInfo(companyName)
        if (quantity <= 0) {
            throw Account.AccountException("expected positive quantity paper currency!")
        }
        account.deductPaperCurrency(userId, companyName, quantity)
        client.post<HttpResponse>("$url/add-paper-currency?companyName=$companyName&quantity=$quantity")
        account.addMoney(userId, paperCurrencyInfo.price * quantity)
    }
}
