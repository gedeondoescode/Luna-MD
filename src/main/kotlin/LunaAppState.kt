import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import ui.editor.LunaWindowState
import util.Settings

@Composable
fun rememberApplicationState() = remember {
    LunaAppState().apply {
        newWindow()
    }
}

class LunaAppState {
    val settings = Settings()
    val tray = TrayState()

    private val _windows = mutableStateListOf<LunaWindowState>()
    val windows: List<LunaWindowState> get() = _windows

    fun newWindow() {
        _windows.add(
            LunaWindowState(
                application = this,
                path = null,
                exit = _windows::remove
            )
        )
    }

    fun sendNotification(notification: Notification){
        tray.sendNotification(notification)
    }
    suspend fun exit() {
        val windowsCopy = windows.reversed()
        for (window in windowsCopy) {
            if (!window.exit()) {
                break
            }
        }
    }
}