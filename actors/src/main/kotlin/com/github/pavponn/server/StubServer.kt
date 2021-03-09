package com.github.pavponn.server

import com.github.pavponn.model.Delayable
import com.github.pavponn.model.DelayableObject
import com.github.pavponn.model.SearchEngine
import kotlin.collections.ArrayList

class StubServer(
    private val searchEngine: SearchEngine,
    private val responseNumber: Int,
) : DelayableSearchService {

    constructor(searchEngine: SearchEngine, responseNumber: Int, vararg delays: Int)
            : this(searchEngine, responseNumber) {
        setDelays(*delays)
    }

    private val delayableObject: Delayable = DelayableObject()

    override fun setDelays(vararg delays: Int) {
        delayableObject.setDelays(*delays)
    }

    override fun nextDelay(): Int {
        return delayableObject.nextDelay()
    }

    override fun getDelay(): Int {
        return delayableObject.getDelay()
    }

    override fun engine(): SearchEngine = searchEngine

    override fun search(query: String): String {
        val urlsAndTitles = ArrayList<Pair<String, String>>()
        try {
            Thread.sleep(nextDelay() * 1000L)

            for (i in 0 until responseNumber) {
                urlsAndTitles.add(
                    Pair(
                        generateUrl(i, query),
                        generateTitle(i, query)
                    )
                )
            }

        } catch (ignored: InterruptedException) {
            return "[]"
        }

        return formJson(urlsAndTitles)
    }


    private fun formJson(urlsAndTitles: List<Pair<String, String>>): String {
        var urlsAndTitlesJson = ""
        for (i in urlsAndTitles.indices) {
            urlsAndTitlesJson += "{\"url\": \"${urlsAndTitles[i].first}\", \"title\": \"${urlsAndTitles[i].second}\"}"
            if (i != urlsAndTitles.size - 1) {
                urlsAndTitlesJson += ",";
            }
        }
        return "[$urlsAndTitlesJson]"
    }


    private fun generateUrl(index: Int, query: String): String {
        return "${searchEngine.link()}/text=${query}&qkid=${generateQueryRef(query)}"
    }

    private fun generateTitle(index: Int, query: String): String {
        return query + "_$index"
    }


    private fun generateQueryRef(query: String): String {
        var queryRef = ""
        for (i in query.indices) {
            queryRef += (0..9).random().toString()
        }
        return queryRef
    }

}