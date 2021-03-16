package market.dao

import market.model.Stocks

interface StockMarketWriteDao {

    fun listCompany(companyTicker: String, stocks: Stocks)

    fun addStocks(companyTicker: String, count: Long)
    fun changeStocksPrice(companyTicker: String, newPrice: Long)

    fun buyStocks(companyTicker: String, count: Long, price: Long): Long
    fun sellStocks(companyTicker: String, count: Long): Long
}