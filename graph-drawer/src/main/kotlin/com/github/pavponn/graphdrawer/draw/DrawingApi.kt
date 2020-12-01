package com.github.pavponn.graphdrawer.draw

interface DrawingApi {

    val drawingAreaWidth: Int

    val drawingAreaHeight: Int

    fun drawCircle(circle: Circle)

    fun drawArrow(from: Point, to: Point)

    fun show() {}
}