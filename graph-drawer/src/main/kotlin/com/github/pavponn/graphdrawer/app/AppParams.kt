package com.github.pavponn.graphdrawer.app

import com.github.pavponn.graphdrawer.draw.DrawingApi
import com.github.pavponn.graphdrawer.graph.Graph

object AppParams {
    lateinit var drawer: (DrawingApi) -> Graph
    lateinit var windowParams: WindowParams
}