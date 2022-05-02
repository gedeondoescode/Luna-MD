package ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter

val LocalAppRes = staticCompositionLocalOf<AppRes> {
    error("LocalLunaResources not provided!")
}

@Composable
fun rememberAppRes(): AppRes {
    val icon = rememberVectorPainter(Icons.Default.List)
    return remember { AppRes(icon) }
}

class AppRes (val icon: VectorPainter)

@Composable
fun rememberVectorPainter(image: ImageVector) =
    rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportHeight = image.viewportHeight,
        viewportWidth = image.viewportWidth,
        name = image.name,
        content = { _, _ -> RenderVectorGroup(group = image.root) }
    )