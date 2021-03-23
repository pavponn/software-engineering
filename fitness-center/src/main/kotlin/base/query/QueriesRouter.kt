package base.query

interface QueriesRouter {

    suspend fun route(query: Query): String
}