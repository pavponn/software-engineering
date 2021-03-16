package accounts.account

import accounts.dao.UserAccountDao
import accounts.http.StockMarketHttpClient
import accounts.model.UserStocks
import java.lang.Exception

class AccountManagerImpl(
    private val stockMarketClient: StockMarketHttpClient,
    private val userAccountDao: UserAccountDao
) : AccountManager {

    override fun addUser(name: String): Long = userAccountDao.addUser(name)

    override fun deposit(id: Long, money: Long) = userAccountDao.deposit(id, money)

    override fun withdraw(id: Long, money: Long) = userAccountDao.withdraw(id, money)

    override fun getBalance(id: Long): Long = userAccountDao.getBalance(id)

    override suspend fun getUserStocks(id: Long): Map<String, UserStocks> {
        val resultMap = HashMap<String, UserStocks>()
        val stocksMap = userAccountDao.getUserStocks(id)
        for (stocks in stocksMap.entries) {
            val stockWithPrice = stockMarketClient.getCompaniesStockPrices(stocks.key)
            resultMap[stocks.key] = UserStocks(stocks.key, stocks.value, stockWithPrice[stocks.key]!!)
        }
        return resultMap
    }

    override suspend fun getTotalUserBalance(id: Long): Long {
        val userStocks = getUserStocks(id)
        return getBalance(id) + userStocks.values.stream().mapToLong { it.count * it.price }.sum()
    }


    override suspend fun buyStocks(id: Long, companyTicker: String, count: Long): Long {
        var stocksMap: Map<String, Long>? = null
        try {
            stocksMap = stockMarketClient.getCompaniesStockPrices(companyTicker)
        } catch (e: Exception) {
            throw IllegalArgumentException("No such company")
        }
        val neededStockPrice = stocksMap[companyTicker]!!
        withdraw(id, neededStockPrice * count)
        try {
            val ignoredSpentAmount = stockMarketClient.buyStocks(companyTicker, count, neededStockPrice)
        } catch(e: Exception) {
            deposit(id, neededStockPrice * count)
            return 0
        }
        userAccountDao.addStocks(id, companyTicker, count)
        return neededStockPrice * count
    }

    override suspend fun sellStocks(id: Long, companyTicker: String, count: Long): Long {
        val success = userAccountDao.removeStocks(id, companyTicker, count)
        check(success) { "Not enough stocks to sell or no user" }
        var profit: Long? = null
        try {
            profit = stockMarketClient.sellStocks(companyTicker, count)
        } catch (e: Exception) {
            userAccountDao.addStocks(id, companyTicker, count)
            throw e
        }
        deposit(id, profit)
        return profit
    }
}