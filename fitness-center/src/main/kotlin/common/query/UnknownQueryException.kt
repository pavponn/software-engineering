package common.query

class UnknownQueryException(query: Query) : Exception("Unknown query $query")