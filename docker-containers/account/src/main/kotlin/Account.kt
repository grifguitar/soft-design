import java.util.concurrent.ConcurrentHashMap

class Account {
    class AccountException(name: String) : Exception(name)

    private val users: MutableMap<Int, User> = ConcurrentHashMap()

    fun addPaperCurrency(userId: Int, companyName: String, quantity: Int) {
        if (quantity <= 0) throw AccountException("expected positive amount!")
        val user = users[userId] ?: throw AccountException("user $userId not found!")
        user.paperCurrencies[companyName] = user.paperCurrencies.getOrDefault(companyName, 0) + quantity
    }

    fun deductPaperCurrency(userId: Int, companyName: String, quantity: Int) {
        if (quantity <= 0) throw AccountException("expected positive amount!")
        val user = users[userId] ?: throw AccountException("user $userId not found!")
        if (user.paperCurrencies.getOrDefault(companyName, 0) < quantity) throw AccountException("insufficient funds!")
        user.paperCurrencies[companyName] = user.paperCurrencies[companyName]!! - quantity
    }

    fun createUser(userName: String): Int {
        val maxId = users.keys.maxOrNull() ?: -1
        val newId = maxId + 1
        users[newId] = User(newId, userName, 0.0, mutableMapOf())
        return newId
    }

    fun getUser(userId: Int): User {
        return users[userId] ?: throw AccountException("There is no user with id $userId")
    }

    fun addMoney(userId: Int, amount: Double) {
        if (amount <= 0) throw AccountException("expected positive amount!")
        val user = users[userId] ?: throw AccountException("user $userId not found!")
        user.money += amount
    }

    fun withdrawMoney(userId: Int, amount: Double) {
        val user = users[userId] ?: throw AccountException("user $userId not found!")
        if (amount <= 0 || amount > user.money) throw AccountException("insufficient funds!")
        user.money -= amount
    }

}
