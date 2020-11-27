package com.github.pavponn.graphdrawer

import com.github.pavponn.graphdrawer.app.*

fun main(args: Array<String>) {
    AppParams.windowParams = WindowParams(1200, 800)
    try {
        require(args.size == 2) { "Usage: <drawing api> <graph type>" }
        AppParams.drawer = GraphType.valueOf(args[1]).parseGraph()
        AppType.valueOf(args[0]).getApp().start()
    } catch (e: IllegalArgumentException) {
        println("Incorrect usage of program: ${e.message}")
    }
}