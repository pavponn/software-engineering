package com.github.pavponn.model


class SearchEngineResponse(
    val searchEngine: SearchEngine,
    val results: List<SearchResult>
) {

    override fun toString(): String {

        return "\n$searchEngine: {\n${results.map { "\t$it\n" }.joinToString(separator = "")}}"
    }
}