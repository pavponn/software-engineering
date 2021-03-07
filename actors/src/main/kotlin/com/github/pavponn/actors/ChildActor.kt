package com.github.pavponn.actors

import akka.actor.UntypedAbstractActor
import akka.japi.Creator
import com.github.pavponn.model.SearchEngineResponse
import com.github.pavponn.model.SearchResult
import com.github.pavponn.server.SearchClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ChildActor(private val searchClient: SearchClient) : UntypedAbstractActor() {

    override fun onReceive(message: Any?) {
        if (message is String) {
            val searchResJson = searchClient.search(message)
            val listType = object : TypeToken<List<SearchResult>>() {}.type
            val serverResults = Gson().fromJson<List<SearchResult>>(searchResJson, listType)
            val searchEngineResp = SearchEngineResponse(searchClient.engine(), serverResults)
            sender.tell(searchEngineResp, self())
            context.stop(self())
        }
    }


}