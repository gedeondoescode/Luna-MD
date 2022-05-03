package ui.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import util.ExitDialog
import util.FileDialog

@Composable
fun LunaMainWindow(state: LunaWindowState) {
    val scope = rememberCoroutineScope()

    fun exit() = scope.launch { state.exit() }

    Window(
        state = state.window,
        title = titleOf(state),
        onCloseRequest = { exit() }
    ) {
        LaunchedEffect(Unit) {state.run()}

        WindowMenuBar(state)

        BasicTextField(
            state.text,
            state::text::set,
            enabled = state.isInit,
            modifier = Modifier.fillMaxSize()
        )

        //todo: we'll come back around to add some prompts for saving and exiting
        if (state.openDialog.isAwaiting) {
            FileDialog(
                title = "Luna-MD",
                isLoad = true,
                onResult = { state.openDialog.onResult(it) }
            )
        }
        if (state.saveDialog.isAwaiting) {
            FileDialog(
                title = "Luna-MD",
                isLoad = true,
                onResult = { state.saveDialog.onResult(it) }
            )
        }
        if (state.exitDialog.isAwaiting) {
            ExitDialog(
                title = "Luna-MD",
                message = "Save changes?",
                onResult = { state.exitDialog.onResult(it) }

            )
        }
    }
}


private fun titleOf(state: LunaWindowState): String {
    val changeMark = if (state.isChanged) "*" else ""
    val filePath = state.path ?: "Untitled"
    return "$changeMark$filePath - Luna-MD"
}

@Composable
private fun WindowNotifications(state: LunaWindowState) {
    fun LunaWindowNotification.format() = when (this) {
        is LunaWindowNotification.SaveSuccess -> Notification(
            "Successfully saved!", path.toString(), Notification.Type.Info
        )
        is LunaWindowNotification.SaveError -> Notification(
            "File isn't saved", path.toString(), Notification.Type.Error
        )
    }
    LaunchedEffect(Unit) {
        state.notifications.collect {
            state.sendNotification(it.format())
        }
    }
}

@Composable
private fun FrameWindowScope.WindowMenuBar(state: LunaWindowState) = MenuBar {
    val scope = rememberCoroutineScope()
    fun save() = scope.launch { state.save() }
    fun open() = scope.launch { state.open() }
    fun exit() = scope.launch { state.exit() }

    Menu("File") {
        Item("New Window", onClick = state::newWindow)
        Item("Open", onClick = { open() })
        Item("Save", onClick = { save() }, enabled = state.isChanged || state.path == null)
        Separator()
        Item("Exit", onClick = { exit() })
    }
}
