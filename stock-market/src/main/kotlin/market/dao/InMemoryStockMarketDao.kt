package market.dao

import market.model.Stocks
import java.util.concurrent.ConcurrentHashMap

class InMemoryStockMarketDao : AbstractStockMarketDao() {

    private val companiesStocks = ConcurrentHashMap<String, Stocks>()

    override fun getStocksInfoImpl(companyTicker: String): Stocks? = companiesStocks[companyTicker]

    override fun listCompanyImpl(companyTicker: String, stocks: Stocks): Boolean =
        companiesStocks.putIfAbsent(companyTicker, stocks) == null


    override fun addStocksImpl(companyTicker: String, count: Long): Boolean =
        companiesStocks.computeIfPresent(companyTicker) { _, stocks ->
            stocks.copy(count = stocks.count + count)
        } != null

    override fun changeStocksPriceImpl(companyTicker: String, newPrice: Long): Boolean =
        companiesStocks.computeIfPresent(companyTicker) { _, stocks ->
            stocks.copy(price = newPrice)
        } != null

    override fun buyStocksImpl(companyTicker: String, count: Long, price: Long): Long? {
        var moneyToMarket: Long? = null
        companiesStocks.computeIfPresent(companyTicker) { _, stocks ->
            if (stocks.count < count || stocks.price != price) {
                stocks
            } else {
                moneyToMarket = count * price
                stocks.copy(count = stocks.count - count)
            }
        }
        return moneyToMarket
    }

    override fun sellStocksImpl(companyTicker: String, count: Long): Long? {
        var moneyToUser: Long? = null
        companiesStocks.computeIfPresent(companyTicker) { _, stocks ->
            moneyToUser = count * stocks.price
            stocks.copy(count = stocks.count + count)
        }
        return moneyToUser
    }

}

