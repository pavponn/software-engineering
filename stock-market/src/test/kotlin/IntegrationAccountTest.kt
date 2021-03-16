import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import accounts.account.AccountManager
import accounts.account.AccountManagerImpl
import accounts.config.HttpClientConfig
import accounts.dao.InMemoryUserAccountDao
import accounts.http.KtorStockMarketHttpClient
import accounts.model.UserStocks
import java.lang.IllegalArgumentException

class IntegrationAccountTest {

    class MyFixedHostPortGenericContainer(imageName: String) :
        FixedHostPortGenericContainer<MyFixedHostPortGenericContainer>(imageName) {
        fun pause() {
            dockerClient.pauseContainerCmd(getContainerId()).exec()
        }

        fun unpause() {
            dockerClient.unpauseContainerCmd(getContainerId()).exec()
        }
    }

    companion object {
        const val APPLE = "APPL"
        const val ALIBABA = "BABA"
        const val VIRGIN_GALACTIC = "SPCE"
        const val CARNIVAL = "CCL"
    }

    private val clientConfig = HttpClientConfig("http", "localhost", 8841)
    private val stockMarketClient = KtorStockMarketHttpClient(clientConfig)
    private val marketAdminClient = StockMarketAdminHttpClient(clientConfig)


    private lateinit var accountManager: AccountManager
    private lateinit var container: MyFixedHostPortGenericContainer

    @Before
    fun startExchange() {
        accountManager = AccountManagerImpl(
            stockMarketClient,
            InMemoryUserAccountDao()
        )
        container = MyFixedHostPortGenericContainer("pavponn/stock-market:latest")
            .withFixedExposedPort(clientConfig.port, 8080)
            .withExposedPorts(8080)
        container.start()
    }

    @After
    fun stopExchange() {
        container.stop()
    }

    @Test
    fun `should add user correctly with zero balance`() = runBlocking {
        val id = accountManager.addUser("pavel")
        assertEquals(0, accountManager.getBalance(id))
        assertEquals(0, accountManager.getTotalUserBalance(id))
    }

    @Test
    fun `should deposit funds correctly`() = runBlocking {
        val depositValue = 10L
        val id = createAccountWithInitialBalance("pavel", depositValue)
        assertEquals(depositValue, accountManager.getBalance(id))
        assertEquals(depositValue, accountManager.getTotalUserBalance(id))
    }

    @Test
    fun `should be able to buy stocks`() = runBlocking {
        val initBalance = 20L
        val id = createAccountWithInitialBalance("pavel", initBalance)
        marketAdminClient.listCompany(APPLE, 3, 3)
        marketAdminClient.listCompany(ALIBABA, 2, 4)
        accountManager.buyStocks(id, APPLE, 2)
        accountManager.buyStocks(id, ALIBABA, 2)
        assertEquals(initBalance - 6 - 8, accountManager.getBalance(id))
        assertEquals(initBalance, accountManager.getTotalUserBalance(id))
        val stocks = accountManager.getUserStocks(id)
        assertEquals(stocks, mapOf(APPLE to UserStocks(APPLE, 2, 3), ALIBABA to UserStocks(ALIBABA, 2, 4)))
    }

    @Test
    fun `should see updated stock prices`() = runBlocking {
        val initBalance = 20L
        val id = createAccountWithInitialBalance("Nickolay", initBalance)
        marketAdminClient.listCompany(APPLE, 3, 3)
        accountManager.buyStocks(id, APPLE, 2)
        marketAdminClient.changePrice(APPLE, 10)
        val stocks = accountManager.getUserStocks(id)
        assertEquals(stocks, mapOf(APPLE to UserStocks(APPLE, 2, 10)))
        assertEquals(initBalance + 14, accountManager.getTotalUserBalance(id))
    }

    @Test
    fun `should be able to sell stocks`() = runBlocking {
        val initBalance = 40L
        val id = createAccountWithInitialBalance("Vadim", initBalance)
        marketAdminClient.listCompany(CARNIVAL, 4, 10)
        accountManager.buyStocks(id, CARNIVAL, 2)
        accountManager.sellStocks(id, CARNIVAL, 2)
        val stocks = accountManager.getUserStocks(id)
        assertEquals(stocks, emptyMap<String, UserStocks>())
        assertEquals(initBalance, accountManager.getBalance(id))
        assertEquals(initBalance, accountManager.getTotalUserBalance(id))
    }

    @Test
    fun `should sell stocks for updated price`() = runBlocking {
        val initBalance = 40L
        val id = createAccountWithInitialBalance("Fedor", initBalance)
        marketAdminClient.listCompany(VIRGIN_GALACTIC, 4, 20)
        accountManager.buyStocks(id, VIRGIN_GALACTIC, 2)
        marketAdminClient.changePrice(VIRGIN_GALACTIC, 2)
        val profit = accountManager.sellStocks(id, VIRGIN_GALACTIC, 2)
        assertEquals(4, profit)
        assertEquals(4, accountManager.getTotalUserBalance(id))
    }

    @Test(expected = IllegalStateException::class)
    fun `should not be able to buy stocks that are too expensive`() = runBlocking {
        val id = createAccountWithInitialBalance("Ilya", 4)
        marketAdminClient.listCompany(VIRGIN_GALACTIC, 5, 5)
        val stocks = accountManager.buyStocks(id, VIRGIN_GALACTIC, 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should not be able to buy unlisted stocks`() = runBlocking {
        val id = createAccountWithInitialBalance("Ilya", 4)
        val stocks = accountManager.buyStocks(id, VIRGIN_GALACTIC, 1)
    }

    @Test
    fun `should not be able to buy more stocks that market has`() = runBlocking {
        val id = createAccountWithInitialBalance("Pavel", 999999)
        marketAdminClient.listCompany(VIRGIN_GALACTIC, 5, 10)
        val stocks = accountManager.buyStocks(id, VIRGIN_GALACTIC, 100)
    }

    private fun createAccountWithInitialBalance(name: String, initBalance: Long): Long {
        val id = accountManager.addUser(name)
        accountManager.deposit(id, initBalance)
        return id
    }

}
