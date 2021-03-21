package sql

object SqlQueries {

    val addUser =
        """
            insert into Users (userId, name)
            values (?, ?)
        """.trimIndent()

    val getUser =
        """
            select * from users 
            left join SubscriptionEvents using (userId)
            where userId = ?
            order by eventId desc;
        """.trimIndent()

    val getUserReports =
        """
            with RankedEvents as (
                select userId,
                    eventType,
                    eventTime,
                    eventId,
                    rank() over (partition by (userId, eventType) order by eventId) as num
                from TurnstileEvents
            ),
                Exits as (
                    select userId,
                        num,
                        eventTime as exitTime,
                        eventId   as exitId
                    from RankedEvents
                    where eventType = 'EXIT'
                ),
                Enters as (
                    select userId,
                        num,
                        eventTime as enterTime
                    from rankedEvents
                    where eventType = 'ENTER'
                )
            select userId,
                count(1)                  AS TotalVisits,
                sum(exitTime - enterTime) AS TotalTime,
                max(exitId)               AS MaxExitId
            from Exits join Enters using (userId, num)
            group by userId
        """.trimIndent()

    val getMaxUserId =
        """
            select maxId from Keys where entity = 'User'
        """.trimIndent()

    val updateMaxIdForUsers =
        """
            update maxId set maxId = ?
            where entity = 'User' 
            and maxId = ?
        """.trimIndent()

    val addTurnstileEvent =
        """
            insert into TurnstileEvents (userId, eventId, eventType, eventTime)
            values (?, ?, ?, ?)
        """.trimIndent()

    val getTurnstileEventsByUserId =
        """
            select *
            from Users left join TurnstileEvents
            using (userId)
            where userId = ?
            order by eventId DESC
        """.trimIndent()

    val renewSubscription =
        """
            insert into SubscriptionEvents (userId, eventId, endTime)
            values (?, ?, ?)
        """.trimIndent()


}