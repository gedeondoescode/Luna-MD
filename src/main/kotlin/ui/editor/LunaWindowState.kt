package ui.editor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.*
import util.Settings
import java.nio.file.Path


class LunaWindowState (
        private val application: LunaWindowState,
        path: Path?,
        private val exit: (LunaWindowState) -> Unit
) {
    val settings: Settings get() = application.settings
    val window = WindowState()

    var path by  mutableStateOf(path)
    private set

    var isChanged by mutableStateOf(false)
        private set

    //todo: set notifications for Luna (i.e. request access to read and edit)

    private var _text by mutableStateOf("")

    var text: String
        get() = _text
        set(value) {
            check(isInit)
            _text = value
            isChanged = true
        }
    var isInit by mutableStateOf(false)
        private set

//  todo: I'll uncomment this block once I have a window ready

/*    fun toggleFullScreen() {
        window.placement = if (window.placement == WindowPlacement.Fullscreen) {
            WindowPlacement.Floating
        } else {
            WindowPlacement.Fullscreen
        }
    }*/

    suspend fun run() {
        if (path != null) {
            open(path!!)
        } else {
            initNew()
        }
    }

    private suspend fun open(path: Path) {
        isInit = false
        isChanged = false
        this.path = path
        try {
            _text = path.readTextAsync()
            isInit = true
        } catch (e: Exception) {
            e.printStackTrace()
            text = "Unreadable path: $path"
        }
    }

    private fun initNew() {
        _text = ""
        isInit = true
        isChanged = false
    }

    fun newWindow() {
        application.newWindow()
    }

    suspend fun exit(): Boolean {
        return true
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun Path.launchSaving(text: String) = GlobalScope.launch {
    writeTextAsync(text)
}

private suspend fun Path.writeTextAsync(text: String) = withContext(Dispatchers.IO) {
    toFile().writeText(text)
}

private suspend fun Path.readTextAsync() = withContext(Dispatchers.IO) {
    toFile().readText()
}
