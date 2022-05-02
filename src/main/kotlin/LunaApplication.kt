import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuScope
import androidx.compose.ui.window.Tray
import kotlinx.coroutines.launch
import ui.common.LocalAppRes
import ui.editor.LunaMainWindow

@Composable
fun ApplicationScope.LunaApplication(state: LunaAppState) {
    if (state.settings.isTrayEnabled && state.windows.isNotEmpty()) {
        applicationTray(state)
    }
    for ( window in state.windows) {
        key(window) {
            LunaMainWindow(window)
        }
    }
}

@Composable
private fun ApplicationScope.applicationTray(state: LunaAppState) {
    Tray(
        LocalAppRes.current.icon,
        state = state.tray,
        tooltip = "Luna MD",
        menu = {ApplicationMenu(state)}
    )
}

@Composable
private fun MenuScope.ApplicationMenu(state: LunaAppState) {
    val scope = rememberCoroutineScope()
    fun exit() = scope.launch { state.exit() }

    Item("New", onClick = state::newWindow)
    Separator()
    Item("Exit", onClick = { exit() })
}