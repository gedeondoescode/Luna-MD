import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.application
import ui.common.LocalAppRes
import ui.common.rememberAppRes

fun main() = application {
    CompositionLocalProvider(LocalAppRes provides rememberAppRes()) {
        LunaApplication(rememberApplicationState())
    }
}