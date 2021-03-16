package accounts.account

import accounts.model.UserStocks

interface AccountManager {

    fun addUser(name: String): Long
    fun deposit(id: Long, money: Long)
    fun withdraw(id: Long, money: Long): Boolean
    fun getBalance(id: Long): Long

    suspend fun getUserStocks(id: Long): Map<String, UserStocks>
    suspend fun getTotalUserBalance(id: Long): Long
    suspend fun buyStocks(id: Long, companyTicker: String, count: Long): Long
    suspend fun sellStocks(id: Long, companyTicker: String, count: Long): Long
}