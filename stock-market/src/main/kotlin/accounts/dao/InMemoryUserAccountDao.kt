package accounts.dao

import accounts.model.UserAccount
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserAccountDao : UserAccountDao {

    private val users = ConcurrentHashMap<Long, UserAccount>()

    override fun addUser(name: String): Long {
        while (true) {
            val id = users.size.toLong()
            if (users.putIfAbsent(id, UserAccount(name, 0, HashMap())) == null) {
                return id
            }
        }
    }

    override fun deposit(id: Long, money: Long) {
        require(money > 0) { "Can't deposit non positive amount" }
        val newBalance =
            users.computeIfPresent(id) { _, userAccount -> userAccount.copy(balance = userAccount.balance + money) }
        check(newBalance != null) { "No user with id $id" }
    }

    override fun withdraw(id: Long, money: Long): Boolean {
        require(money > 0) { "Can't withdraw non positive amount" }
        var enoughFunds = false
        val newBalance = users.computeIfPresent(id) { _, userAccount ->
            if (userAccount.balance >= money) {
                enoughFunds = true
                userAccount.copy(balance = userAccount.balance - money)
            } else {
                userAccount
            }
        }
        check(newBalance != null) { "No user with id $id" }
        check(enoughFunds) { "Not enough funds" }
        return true
    }

    override fun getBalance(id: Long): Long {
        val user = users[id]
        check(user != null) { "No user with id $id" }
        return user.balance
    }

    override suspend fun getUserStocks(id: Long): Map<String, Long> {
        val user = users[id]
        check(user != null) { "No user with id $id" }
        return user.stocks
    }

    override suspend fun addStocks(id: Long, companyTicker: String, count: Long): Boolean {
        require(count > 0) { "Can't add non positive number of stocks" }
        val newUser = users.computeIfPresent(id) { _, user ->
            val stocks = user.stocks.toMutableMap()
            stocks.compute(companyTicker) { _, amount -> amount?.plus(count) ?: count }
            user.copy(stocks = stocks)
        }
        return newUser != null
    }

    override suspend fun removeStocks(id: Long, companyTicker: String, count: Long): Boolean {
        require(count > 0) { "Can't subtract non positive number of stocks" }
        var delete = false
        val newUser = users.computeIfPresent(id) { _, user ->
            val stocks = user.stocks.toMutableMap()
            stocks.compute(companyTicker) { _, amount ->
                check(amount != null && amount >= count) { "Not enough stocks" }
                if (amount == count) {
                    null
                } else {
                    amount - count
                }

            }
            user.copy(stocks = stocks)
        }




        return newUser != null
    }
}