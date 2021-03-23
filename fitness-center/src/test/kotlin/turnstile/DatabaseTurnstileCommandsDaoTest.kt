package turnstile

import base.mockkGetExistingMember
import base.model.Member
import base.model.TurnstileEventType
import base.utils.toInstant
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import turnstile.dao.DatabaseTurnstileCommandsDao

@Suppress("UNCHECKED_CAST")
class DatabaseTurnstileCommandsDaoTest {

    companion object {
        const val MEMBER_ID = 1L
        const val MEMBER_NAME = "pablo"
        const val EVENT_ID = 1L
        val MEMBERSHIP_END_TIME = LocalDateTime.parse("2023-04-05")!!
        val ENTER_TIME = LocalDateTime.parse("2021-01-01")!!
        val EXIT_TIME = LocalDateTime.parse("2021-12-12")!!
        const val PREV_EVENT_ID = 321L
        val PREV_EVENT_TIME = LocalDateTime.parse("2020-01-01")!!
        val ENTER_EVENT_TYPE = TurnstileEventType.ENTER
        val EXIT_EVENT_TYPE = TurnstileEventType.EXIT
    }

    lateinit var connection: SuspendingConnection

    @Before
    fun setUp() {
        connection = mockk<SuspendingConnection>()
    }

    @Test
    fun `should be able to enter first time`() = runBlocking {
        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkGetExistingMember(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME, EVENT_ID)
            mockkGetNonExistingTurnstileEvent(transaction, MEMBER_ID)
            mockkAddTurnstileEvent(transaction, MEMBER_ID, 0, ENTER_EVENT_TYPE, ENTER_TIME.toInstant())

            callback(transaction)
        }

        val turnstileCommand = DatabaseTurnstileCommandsDao(connection)
        val actualResult = turnstileCommand.enter(MEMBER_ID, ENTER_TIME.toInstant())
        assertEquals(Unit, actualResult)
    }

    @Test
    fun `should be able to enter`() = runBlocking {

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkGetExistingMember(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME, EVENT_ID)
            mockkGetExistingTurnstileEvent(
                transaction,
                MEMBER_ID,
                MEMBER_NAME,
                PREV_EVENT_ID,
                EXIT_EVENT_TYPE.toString(),
                PREV_EVENT_TIME
            )
            mockkAddTurnstileEvent(transaction, MEMBER_ID, PREV_EVENT_ID + 1, ENTER_EVENT_TYPE, ENTER_TIME.toInstant())

            callback(transaction)
        }

        val turnstileCommand = DatabaseTurnstileCommandsDao(connection)
        val actualResult = turnstileCommand.enter(MEMBER_ID, ENTER_TIME.toInstant())
        assertEquals(Unit, actualResult)
    }


    @Test
    fun `should be able to exit`() = runBlocking {

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkGetExistingMember(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME, EVENT_ID)
            mockkGetExistingTurnstileEvent(
                transaction,
                MEMBER_ID,
                MEMBER_NAME,
                PREV_EVENT_ID,
                ENTER_EVENT_TYPE.toString(),
                PREV_EVENT_TIME
            )
            mockkAddTurnstileEvent(transaction, MEMBER_ID, PREV_EVENT_ID + 1, EXIT_EVENT_TYPE, EXIT_TIME.toInstant())

            callback(transaction)
        }

        val turnstileCommand = DatabaseTurnstileCommandsDao(connection)
        val actualResult = turnstileCommand.exit(MEMBER_ID, EXIT_TIME.toInstant())
        assertEquals(Unit, actualResult)
    }


    @Test(expected = IllegalStateException::class)
    fun `should throw exception when enter twice`() = runBlocking {
        val connection = mockk<SuspendingConnection>()

        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkGetExistingMember(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME, EVENT_ID)
            mockkGetExistingTurnstileEvent(
                transaction,
                MEMBER_ID,
                MEMBER_NAME,
                PREV_EVENT_ID,
                ENTER_EVENT_TYPE.toString(),
                PREV_EVENT_TIME
            )
            mockkAddTurnstileEvent(transaction, MEMBER_ID, PREV_EVENT_ID + 1, ENTER_EVENT_TYPE, ENTER_TIME.toInstant())

            callback(transaction)
        }

        val turnstileCommand = DatabaseTurnstileCommandsDao(connection)
        turnstileCommand.enter(MEMBER_ID, ENTER_TIME.toInstant())
    }


    @Test(expected = IllegalStateException::class)
    fun `should throw exception when exit without any event before`(): Unit = runBlocking {
        coEvery {
            connection.inTransaction(any<suspend (SuspendingConnection) -> Member?>())
        }.coAnswers {
            val callback = args[0] as suspend (SuspendingConnection) -> Member?
            val transaction = mockk<SuspendingConnection>()

            mockkGetExistingMember(transaction, MEMBER_ID, MEMBER_NAME, MEMBERSHIP_END_TIME, EVENT_ID)
            mockkGetNonExistingTurnstileEvent(transaction, MEMBER_ID)
            mockkAddTurnstileEvent(transaction, MEMBER_ID, 0, EXIT_EVENT_TYPE, EXIT_TIME.toInstant())

            callback(transaction)
        }

        val turnstileCommand = DatabaseTurnstileCommandsDao(connection)
        turnstileCommand.exit(MEMBER_ID, EXIT_TIME.toInstant())
    }
}