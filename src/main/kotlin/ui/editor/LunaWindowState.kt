package ui.editor
import LunaAppState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
// import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.*
import util.AlertDialogResult
import util.Settings
import java.nio.file.Path


class LunaWindowState (
        private val application: LunaAppState,
        path: Path?,
        private val exit: (LunaWindowState) -> Unit
) {
    val settings: Settings get() = application.settings
    val window = WindowState()

    var path by  mutableStateOf(path)
    private set

    var isChanged by mutableStateOf(false)
        private set

    val openDialog = DialogState<Path?>()
    val saveDialog = DialogState<Path?>()
    val exitDialog = DialogState<AlertDialogResult>()

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

    suspend fun open() {
        if(askToSave()) {
            val path = openDialog.awaitResult()
            if (path != null) {
                open(path)
            }
        }
    }

    suspend fun save(): Boolean {
        check(isInit)
        if (path == null) {
            val path = saveDialog.awaitResult()
            if (path != null) {
                save(path)
                return true
            }
        } else {
            save(path!!)
            return true
        }
        return false
    }

    private var saveJob: Job? = null

    private suspend fun save(path: Path) {
        isChanged = false
        this.path = path

        saveJob?.cancel()
        saveJob = path.launchSaving(text)
    }

    suspend fun exit(): Boolean {
        return if (askToSave()) {
            exit(this)
            true
        } else {
            false
        }
    }
    private suspend fun askToSave(): Boolean {
        if (isChanged) {
            when (exitDialog.awaitResult()) {
                AlertDialogResult.Yes -> {
                    if (save()) {
                        return true
                    }
                }
                AlertDialogResult.No -> {
                    return true
                }
                AlertDialogResult.Cancel -> return false
            }
        } else {
            return true
        }
        return false
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

class DialogState<T> {
    private var onResult: CompletableDeferred<T>? by mutableStateOf(null)

    val isAwaiting get() = onResult != null

    suspend fun awaitResult(): T {
        onResult = CompletableDeferred()
        val result = onResult!!.await()
        onResult = null
        return result
    }
    fun onResult(result: T) = onResult!!.complete(result)
}