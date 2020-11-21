package com.github.pavponn.graphdrawer.draw

import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.geom.Ellipse2D
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class AwtDrawingApi(
    private val graphics2D: Graphics2D,
    override val drawingAreaWidth: Int,
    override val drawingAreaHeight: Int
) : DrawingApi {

    override fun drawVertexCircle(circle: Circle) {
        graphics2D.fill(
            Ellipse2D.Double(
                (circle.center.x - circle.radius).toDouble(),
                (circle.center.y - circle.radius).toDouble(),
                (circle.radius * 2).toDouble(),
                (circle.radius * 2).toDouble()
            )
        )
    }

    override fun drawEdgeArrow(from: Point, to: Point) {
        graphics2D.drawLine(from.x, from.y, to.x, to.y)
        drawArrowHead(to, from)
    }

    private fun drawArrowHead(tip: Point, tail: Point) {
        val phi = Math.toRadians(3.0)
        val dy = tip.y - tail.y.toDouble()
        val dx = tip.x - tail.x.toDouble()
        val theta = atan2(dy, dx)
        var rho: Double = theta + phi
        val xs = Array(2) { 0 }
        val ys = Array(2) { 0 }
        for (j in 0..1) {
            xs[j] = (tip.x - 25 * cos(rho)).toInt()
            ys[j] = (tip.y - 25 * sin(rho)).toInt()
            graphics2D.drawLine(tip.x, tip.y, xs[j], ys[j])
            rho = theta - phi
        }
        graphics2D.drawLine(xs[0], ys[0], xs[1], ys[1])
        val xsPolygon = intArrayOf(tip.x, xs[0], xs[1])
        val ysPolygon = intArrayOf(tip.y, ys[0], ys[1])
        graphics2D.fillPolygon(Polygon(xsPolygon, ysPolygon, 3))
    }

}