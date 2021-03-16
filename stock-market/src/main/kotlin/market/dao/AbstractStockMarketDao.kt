package market.dao

import market.model.Stocks

abstract class AbstractStockMarketDao : StockMarketDao {

    override fun getStocksInfo(companyTicker: String): Stocks {
        val stocks = getStocksInfoImpl(companyTicker)
        check(stocks != null) { "Company with ticker $companyTicker doesn't exist" }
        return stocks
    }

    override fun listCompany(companyTicker: String, stocks: Stocks) {
        require(stocks.count > 0 && stocks.price > 0) { "Can't list company with non positive stocks count or (and) price" }
        val result = listCompanyImpl(companyTicker, stocks)
        check(result) { "Company with ticker $companyTicker already exists" }

    }


    override fun addStocks(companyTicker: String, count: Long) {
        require(count > 0) { "Can't add non positive stocks count " }
        val result = addStocksImpl(companyTicker, count)
        check(result) { "Company with ticker $companyTicker doesn't exist" }
    }


    override fun changeStocksPrice(companyTicker: String, newPrice: Long) {
        require(newPrice > 0) { "Can't change price to a non positive value" }
        val result = changeStocksPriceImpl(companyTicker, newPrice)
        check(result) { "Company with ticker $companyTicker doesn't exist" }
    }

    override fun buyStocks(companyTicker: String, count: Long, price: Long): Long {
        require(count > 0) { "Can't buy non positive number of stocks" }
        require(price > 0) { "Can't buy stocks for non positive price" }
        val money = buyStocksImpl(companyTicker, count, price)
        check(money != null) { "Company with ticker $companyTicker doesn't exist or market doesn't hold enough stock" }
        return money
    }

    override fun sellStocks(companyTicker: String, count: Long): Long {
        require(count > 0) { "Can't sell non positive number of stocks" }
        val money = sellStocksImpl(companyTicker, count)
        check(money != null) { "Company with ticker $companyTicker doesn't exist" }
        return money
    }

    abstract fun getStocksInfoImpl(companyTicker: String): Stocks?
    abstract fun listCompanyImpl(companyTicker: String, stocks: Stocks): Boolean
    abstract fun addStocksImpl(companyTicker: String, count: Long): Boolean
    abstract fun changeStocksPriceImpl(companyTicker: String, newPrice: Long): Boolean

    abstract fun buyStocksImpl(companyTicker: String, count: Long, price: Long): Long?
    abstract fun sellStocksImpl(companyTicker: String, count: Long): Long?

}