package common.query

interface QueriesHandler {

    suspend fun handle(query: Query): String
}