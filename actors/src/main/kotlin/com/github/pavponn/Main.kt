@file:Suppress("UNCHECKED_CAST")

package com.github.pavponn

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.PatternsCS
import akka.util.Timeout
import com.github.pavponn.actors.MasterActor
import com.github.pavponn.model.AggregatorResponse
import com.github.pavponn.model.SearchEngineResponse
import java.util.*
import java.util.concurrent.TimeUnit

fun main() {
    val scanner = Scanner(System.`in`)
    lateinit var system: ActorSystem
    var masterNumber = 0

    while (true) {

        val query: String = scanner.next()
        if (query == "\\q") {
            break
        }
        system = ActorSystem.create()

        val master: ActorRef = system.actorOf(
            Props.create(MasterActor::class.java, 1L),
            "master${masterNumber++}"
        )
        val response = PatternsCS.ask(master, query, Timeout.apply(15, TimeUnit.SECONDS))
            .toCompletableFuture()
            .join() as AggregatorResponse

        println(response)
        system.terminate()
    }
    system.terminate()

}
