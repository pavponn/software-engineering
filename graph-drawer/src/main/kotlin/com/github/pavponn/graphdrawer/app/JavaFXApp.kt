package com.github.pavponn.graphdrawer.app

import com.github.pavponn.graphdrawer.draw.JavaFXDrawingApi
import javafx.application.Application
import javafx.stage.Stage

class JavaFXApp : Application(), GraphApp {

    override fun start() {
        launch(this::class.java)
    }

    override fun start(primaryStage: Stage) {
        val drawingApi = JavaFXDrawingApi(
            primaryStage,
            AppParams.windowParams.width,
            AppParams.windowParams.height
        )
        AppParams.drawer(drawingApi).drawGraph()
        drawingApi.show()
    }
}