package admin

import admin.dao.DatabaseAdminCommandsDao
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.SuspendingConnection
import base.model.Member
import base.utils.toInstant
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import base.sql.SqlQueries

@Suppress("UNCHECKED_CAST")
class DatabaseAdminCommandsDaoTest {
    companion object {
        const val MEMBER_ID = 1L
        const val EVENT_ID = 1L
        const val MEMBER_NAME = "pablo"
        const val NEXT_MAX_ID_FOR_DB = 100L
        const val START_ID = 0L

        val MEMBERSHIP_END_TIME = LocalDateTime.parse("2020-01-01")!!

        val SUBSCRIBE_1 = LocalDateTime.parse("2021-01-01")!!
        val SUBSCRIBE_2 = LocalDateTime.parse("2022-06-03")!!
        val SUBSCRIBE_3 = LocalDateTime.parse("2021-09-11")!!
    }

    private lateinit var connection: SuspendingConnection

    @Before
    fun setUp() {
        connection = mockk<SuspendingConnection>()
    }

    @Test
    fun `should add member`() = runBlocking {

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            val getIdRows = mockk<ResultSet>()
            val getIdRow = mockk<RowData>()
            every { getIdRows[0] }.returns(getIdRow)
            every { getIdRow.getLong("maxid") }.returns(START_ID)
            coEvery {
                transaction.sendPreparedStatement(SqlQueries.getMaxMemberId)
            }.returns(QueryResult(0, "OK", getIdRows))

            coEvery {
                transaction.sendPreparedStatement(
                    SqlQueries.updateMaxIdForMembers,
                    listOf(NEXT_MAX_ID_FOR_DB, START_ID)
                )
            }.returns(QueryResult(0, "OK"))

            coEvery {
                transaction.sendPreparedStatement(SqlQueries.addMember, listOf(MEMBER_ID, MEMBER_NAME))
            }.returns(QueryResult(0, "OK"))

            callback(transaction)
        }

        val adminCommandsDao = DatabaseAdminCommandsDao(connection)
        val actualMemberId = adminCommandsDao.addMember(MEMBER_NAME)
        assertEquals(MEMBER_ID, actualMemberId)
    }

    @Test
    fun `should renew subscription`() = runBlocking {

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkUpdateSubscription(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME.toInstant(), SUBSCRIBE_1.toInstant(), EVENT_ID)

            callback(transaction)
        }

        val managerCommand = DatabaseAdminCommandsDao(connection)
        val actualResult = managerCommand.renewSubscription(MEMBER_ID, SUBSCRIBE_1.toInstant())
        assertEquals(Unit, actualResult)
    }

    @Test
    fun `should renew subscription multiple times`() = runBlocking {

        val connection = mockk<SuspendingConnection>()

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkUpdateSubscription(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME.toInstant(), SUBSCRIBE_1.toInstant(), EVENT_ID)
            mockkUpdateSubscription(transaction, MEMBER_ID, MEMBER_NAME, SUBSCRIBE_1.toInstant(), SUBSCRIBE_2.toInstant(), EVENT_ID)
            mockkUpdateSubscription(transaction, MEMBER_ID, MEMBER_NAME, SUBSCRIBE_2.toInstant(), SUBSCRIBE_3.toInstant(), EVENT_ID)

            callback(transaction)
        }

        val managerCommand = DatabaseAdminCommandsDao(connection)
        val actualFirstResult = managerCommand.renewSubscription(MEMBER_ID, SUBSCRIBE_1.toInstant())
        assertEquals(Unit, actualFirstResult)
        val actualSecondResult = managerCommand.renewSubscription(MEMBER_ID, SUBSCRIBE_2.toInstant())
        assertEquals(Unit, actualSecondResult)
        val actualThirdResult = managerCommand.renewSubscription(MEMBER_ID, SUBSCRIBE_3.toInstant())
        assertEquals(Unit, actualThirdResult)
    }
}