package com.github.pavponn.server

import com.github.pavponn.model.SearchEngine

interface SearchClient {
    fun search(query: String): String

    fun engine(): SearchEngine
}