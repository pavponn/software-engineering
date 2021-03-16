package accounts.dao


interface UserAccountDao {

    fun addUser(name: String): Long
    fun deposit(id: Long, money: Long)
    fun withdraw(id: Long, money: Long): Boolean
    fun getBalance(id: Long): Long

    suspend fun getUserStocks(id: Long): Map<String, Long>
    suspend fun addStocks(id: Long, companyTicker: String, count: Long): Boolean
    suspend fun removeStocks(id: Long, companyTicker: String, count: Long): Boolean
}