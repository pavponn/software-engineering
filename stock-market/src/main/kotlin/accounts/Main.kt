package accounts

import accounts.account.AccountManagerImpl
import accounts.config.HttpClientConfig
import accounts.dao.InMemoryUserAccountDao
import accounts.http.KtorStockMarketHttpClient
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Paths
import java.util.stream.Collectors

fun main(): Unit = runBlocking {
    val appConfig = getConfig()
    val httpClientConfig = getHttpClientConfig(appConfig)
    val accountManager = AccountManagerImpl(
        KtorStockMarketHttpClient(httpClientConfig),
        InMemoryUserAccountDao()
    )
    embeddedServer(Netty, port = getServerConfig(appConfig).getInt("port")) {
        routing {
            post("/user") {
                val name = call.request.queryParameters["name"]
                if (name == null) {
                    call.badRequest()
                } else {
                    val id = accountManager.addUser(name)
                    call.respondText("User $name created with id $id")
                }
            }
            post("/user/{id}/deposit") {
                val id = call.parameters["id"]?.toLong()
                val amount = call.request.queryParameters["amount"]?.toLong()
                if (id == null || amount == null) {
                    call.badRequest()
                } else {
                    try {
                        accountManager.deposit(id, amount)
                        call.respondText("Deposited $amount funds for user with id $id")
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            get("/user/{id}/stocks") {
                val id = call.parameters["id"]?.toLong()
                if (id == null) {
                    call.badRequest()
                } else {
                    try {
                        val userStocks = accountManager.getUserStocks(id)
                        val result = userStocks.values
                            .stream()
                            .map { it.toString() }
                            .collect(Collectors.joining(";"))
                        call.respondText(result)
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            get("/user/{id}/balance") {
                val id = call.parameters["id"]?.toLong()
                if (id == null) {
                    call.badRequest()
                } else {
                    try {
                        val balance = accountManager.getTotalUserBalance(id)
                        call.respondText("Balance: $balance")
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            post("user/{id}/buy") {
                val id = call.parameters["id"]?.toLong()
                val companyTicker = call.request.queryParameters["ticker"]
                val amount = call.request.queryParameters["amount"]?.toLong()
                if (id == null || companyTicker == null || amount == null) {
                    call.badRequest()
                } else {
                    try {
                        val moneySpent = accountManager.buyStocks(id, companyTicker, amount)
                        call.respondText("Bought $amount stocks of $companyTicker for $moneySpent in total")
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
            post("user/{id}/sell") {
                val id = call.parameters["id"]?.toLong()
                val companyTicker = call.request.queryParameters["ticker"]
                val amount = call.request.queryParameters["amount"]?.toLong()
                if (id == null || companyTicker == null || amount == null) {
                    call.badRequest()
                } else {
                    try {
                        val moneyGot = accountManager.sellStocks(id, companyTicker, amount)
                        call.respondText("Sold $amount stocks of $companyTicker for $moneyGot in total")
                    } catch (e: Exception) {
                        call.error(e.message)
                    }
                }
            }
        }
    }.start(wait = true)
    Unit
}

fun getConfig(): Config {
    val confFile = Paths.get("src/main/resources/app.conf").toFile()
    return ConfigFactory.parseFile(confFile)
}

fun getHttpClientConfig(config: Config): HttpClientConfig {
    val clientConfig = config.getConfig("client")
    val schema = clientConfig.getString("schema")
    val host = clientConfig.getString("host")
    val port = clientConfig.getInt("port")
    return HttpClientConfig(schema, host, port)
}

fun getServerConfig(config: Config): Config = config.getConfig("server")


suspend fun ApplicationCall.badRequest(): Unit = respondText("Incorrect parameters", status = HttpStatusCode.BadRequest)

suspend fun ApplicationCall.error(errorMsg: String?): Unit =
    respondText("Error occurred: $errorMsg", status = HttpStatusCode.BadRequest)