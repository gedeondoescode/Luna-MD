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
import java.awt.Frame

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
    }
}

private fun titleOf(state: LunaWindowState): String {
    val changeMark = if (state.isChanged) "*" else ""
    val filePath = state.path ?: "Untitled"
    return "$changeMark$filePath - Luna-MD"
}

@Composable
private fun FrameWindowScope.WindowMenuBar(state: LunaWindowState) = MenuBar {
    val scope = rememberCoroutineScope()

    fun exit() = scope.launch { state.exit() }

    Menu("File") {
        Item("New Window", onClick = state::newWindow)
        Item("Exit", onClick = { exit() })
    }
}
