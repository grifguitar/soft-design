import java.util.concurrent.ConcurrentHashMap

class MoneyServer {
    class ServerException(name: String) : Exception(name)

    class PaperCurrency(val company: String, var price: Double, var quantity: Int)

    private val companyToPaperCurrencyMap: MutableMap<String, PaperCurrency> = ConcurrentHashMap()

    fun getCompanyToPaperCurrencyMap(): Map<String, PaperCurrency> {
        return companyToPaperCurrencyMap
    }

    fun getPaperCurrencyByCompany(company: String): PaperCurrency {
        return companyToPaperCurrencyMap[company] ?: throw ServerException("$company company not exist!")
    }

    fun addPaperCurrency(company: String, quantity: Int) {
        if (quantity < 0)
            throw ServerException("expected a non-negative amount of paper currency!")
        val paperCurrency = getPaperCurrencyByCompany(company)
        paperCurrency.quantity += quantity
    }

    fun buyPaperCurrency(company: String, quantity: Int): Double {
        val paperCurrency = getPaperCurrencyByCompany(company)
        val q = paperCurrency.quantity
        if (quantity > q)
            throw ServerException("the storage only has $q paper currency, but requested $quantity!")
        paperCurrency.quantity -= quantity
        return paperCurrency.price * quantity
    }

    fun changePrice(company: String, price: Double) {
        if (price <= 0)
            throw ServerException("expected positive price!")
        val paperCurrency = getPaperCurrencyByCompany(company)
        paperCurrency.price = price
    }

    fun addCompany(company: String, price: Double) {
        if (price <= 0)
            throw ServerException("expected positive price of paper currency!")
        if (company in companyToPaperCurrencyMap)
            throw ServerException("expected unknown company, but received $company!")
        companyToPaperCurrencyMap[company] = PaperCurrency(company, price, 0)
    }
}
