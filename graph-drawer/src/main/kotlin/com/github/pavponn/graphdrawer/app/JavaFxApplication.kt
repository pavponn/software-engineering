package com.github.pavponn.graphdrawer.app

import com.github.pavponn.graphdrawer.draw.JavaFxDrawingApi
import javafx.application.Application
import javafx.stage.Stage


class JavaFxApplication :
    Application(),
    DrawingApplication {

    override fun startApplication() {
        launch(this::class.java)
    }

    override fun start(primaryStage: Stage) {
        val drawingApi = JavaFxDrawingApi(
            primaryStage,
            ApplicationParameters.windowParams.width,
            ApplicationParameters.windowParams.height
        )
        ApplicationParameters.drawer(drawingApi).drawGraph()
        drawingApi.show()
    }
}