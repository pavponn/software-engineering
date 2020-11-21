package com.github.pavponn.graphdrawer

import com.github.pavponn.graphdrawer.graph.GraphParser
import com.github.pavponn.graphdrawer.app.*

fun main(args: Array<String>) {
    GlobalParams.windowParams = WindowParams(1200, 800)
    try {
        require(args.size == 2) { "Usage: <drawing api> <graph type>" }
        val type = when (args[1]) {
            "matrix" -> GraphType.MATRIX
            "list" -> GraphType.LIST
            else -> throw IllegalArgumentException("Wrong graph type")
        }
        GlobalParams.drawer = when (type) {
            GraphType.LIST -> GraphParser().parseListGraph()
            GraphType.MATRIX -> GraphParser().parseMatrixGraph()
        }
        val application = when (args[0]) {
            "javafx" -> JavaFxApplication()
            "awt" -> AwtApplication()
            else -> throw IllegalArgumentException("Wrong drawing API: either 'javafx' or awt")
        }
        application.startApplication()
    } catch (e: IllegalArgumentException) {
        println("Incorrect usage of program: ${e.message}")
    }
}