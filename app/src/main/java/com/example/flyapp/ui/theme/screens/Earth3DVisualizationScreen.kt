package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import io.github.sceneview.Scene
import io.github.sceneview.SceneView
import io.github.sceneview.collision.Quaternion.add
import io.github.sceneview.gesture.CameraGestureDetector.CameraManipulator
import io.github.sceneview.loaders.EnvironmentLoader
import io.github.sceneview.math.Color
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun Earth3DVisualizationScreen(
    modifier: Modifier,
    departLat: Float,
    departLon: Float,
    arriveLat: Float,
    arriveLon: Float,
    progress: Float
) {
    val earthRotation by rememberInfiniteTransition(label = "EarthRotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val view = rememberView(engine)
    val cameraNode = rememberCameraNode(engine) {
        position = Position(z = 3.0f)
    }

    Scene(
        modifier = modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Transparent),
        engine = engine,
        view = view,
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        collisionSystem = rememberCollisionSystem(view),
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000.0f
        },
        cameraNode = cameraNode,
        cameraManipulator = rememberCameraManipulator(),
        childNodes = rememberNodes {
            // Add Earth model
            val earthNode = ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/earth3.glb"
                ),
                scaleToUnits = 1.0f
            ).apply {
                rotation = io.github.sceneview.math.Rotation(0f, earthRotation, 0f)
            }
            add(earthNode)

            // Add plane model or flight path marker
            val planeNode = ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/airplane-_low-poly.glb"
                ),
                scaleToUnits = 0.05f
            )

            // Calculate flight position based on progress
            val radianFactor = (Math.PI / 180f).toFloat()

            // Convert lat/lon to 3D coordinates (simplified spherical projection)
            val radius = 1.0f
            val departX = radius * cos(departLat * radianFactor) * cos(departLon * radianFactor)
            val departY = radius * sin(departLat * radianFactor)
            val departZ = radius * cos(departLat * radianFactor) * sin(departLon * radianFactor)

            // Calculate arrival position
            val arriveX = radius * cos(arriveLat * radianFactor) * cos(arriveLon * radianFactor)
            val arriveY = radius * sin(arriveLat * radianFactor)
            val arriveZ = radius * cos(arriveLat * radianFactor) * sin(arriveLon * radianFactor)

            // Linear interpolation for flight path
            val currentX = departX + (arriveX - departX) * progress
            val currentY = departY + (arriveY - departY) * progress
            val currentZ = departZ + (arriveZ - departZ) * progress

            // Normalize to keep on sphere surface
            val mag = sqrt(currentX * currentX + currentY * currentY + currentZ * currentZ)
            val normalizedX = currentX / mag * radius
            val normalizedY = currentY / mag * radius
            val normalizedZ = currentZ / mag * radius

            // Position the plane
            planeNode.position = Position(normalizedX, normalizedY, normalizedZ)

            // Orient the plane in the direction of travel
            val dirX = arriveX - departX
            val dirY = arriveY - departY
            val dirZ = arriveZ - departZ

            // Calculate rotation to face direction of travel
            val yaw = atan2(dirZ, dirX) * (180f / Math.PI.toFloat())
            val pitch = atan2(dirY, sqrt(dirX * dirX + dirZ * dirZ)) * (180f / Math.PI.toFloat())

            planeNode.rotation = io.github.sceneview.math.Rotation(pitch, yaw + 90f, 0f)

            add(planeNode)
        }
    )
}
@Preview
@Composable
fun Earth3DVisualizationScreenPreview() {
    var progress by remember { mutableFloatStateOf(0f) }
    Earth3DVisualizationScreen(
        modifier = Modifier.fillMaxSize(),
        departLat = 37.7749f,
        departLon = -122.4194f,
        arriveLat = 34.0522f,
        arriveLon = -118.2437f,
        progress = progress
    )
}
