class PaperCurrency(val company: String, var price: Double, var quantity: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PaperCurrency
        if (company != other.company) return false
        if (price != other.price) return false
        if (quantity != other.quantity) return false
        return true
    }

    override fun hashCode(): Int {
        var result = company.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + quantity
        return result
    }

}
