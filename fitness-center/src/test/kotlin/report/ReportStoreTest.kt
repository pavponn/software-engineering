package report

import base.model.MemberReport
import base.utils.toInstant
import base.utils.toJDuration
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDateTime
import org.joda.time.Period
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import report.dao.DatabaseReportQueriesDao
import report.dao.ReportQueriesDao
import report.store.InMemoryReportStore
import report.store.ReportStore
import java.time.Duration

class ReportStoreTest {

    companion object {
        const val MEMBER_1 = 1L
        const val MEMBER_2 = 2L
        const val ENTER_EVENT_ID = 1L
        const val EXIT_EVENT_ID = 2L

        val TIME_1 = LocalDateTime.parse("2021-01-01")!!
        val TIME_2 = LocalDateTime.parse("2022-01-01")!!
    }

    lateinit var connection: SuspendingConnection
    lateinit var dao: ReportQueriesDao
    lateinit var store: ReportStore

    @Before
    fun setUp() {
        connection = mockk<SuspendingConnection>()
        dao = DatabaseReportQueriesDao(connection)
        store = InMemoryReportStore(dao)
    }

    @Test
    fun `should return empty report`() = runBlocking {
        mockkInitializeWithNoData(connection)

        store.initializeStore()

        val expectedResult = null
        val actualResult = store.getMemberReport(MEMBER_1)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should return report after update`() = runBlocking {
        val eventId = 1
        mockkInitializeWithNoData(connection)
        store.initializeStore()
        mockkUpdateStoreOnce(connection, MEMBER_1, -1, ENTER_EVENT_ID, EXIT_EVENT_ID, TIME_1, TIME_2)
        store.updateStore(MEMBER_1)
        val expectedResult = MemberReport(MEMBER_1, 1, Duration.between(TIME_1.toInstant(), TIME_2.toInstant()))
        val actualResult = store.getMemberReport(MEMBER_1)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should return report after nonempty init`() = runBlocking {
        val time = Period.minutes(10)
        mockkInitializeWithData(connection, MEMBER_1, 2L, time, 2)
        store.initializeStore()

        val expectedResult = MemberReport(MEMBER_1, 2, time.toJDuration())
        val actualResult = store.getMemberReport(MEMBER_1)
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `should return empty report after nonempty init`() = runBlocking {
        val time = Period.minutes(10)
        mockkInitializeWithData(connection, MEMBER_1, 2L, time, 2)
        store.initializeStore()

        val expectedResult = null
        val actualResult = store.getMemberReport(MEMBER_2)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `should return report after nonempty init and update`() = runBlocking {
        val time = Period.minutes(10)
        mockkInitializeWithData(connection, MEMBER_1, 2L, time, -1)
        store.initializeStore()

        mockkUpdateStoreOnce(connection, MEMBER_1, -1, ENTER_EVENT_ID, EXIT_EVENT_ID, TIME_1, TIME_2)
        store.updateStore(MEMBER_1)
        val expectedResult = MemberReport(MEMBER_1, 3, time.toJDuration().plus(Duration.between(TIME_1.toInstant(), TIME_2.toInstant())))
        val actualResult = store.getMemberReport(MEMBER_1)
        assertEquals(expectedResult, actualResult)
    }

}
