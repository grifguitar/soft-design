package model

enum class Currency(val fullName: String, val rate: Double) {
    RUB("rubles", 1.00),
    USD("dollars", 79.45),
    EUR("euros", 86.43);

    companion object {
        fun convert(from: Currency, to: Currency, amount: Double): Double {
            return (from.rate / to.rate) * amount
        }
    }
}
