package ui.editor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import os.File
import util.EmptyLines
import util.SingleSelection

class DocEditor (
    val fileName: String,
    val lines: (BackgroundScope: CoroutineScope) -> Lines,
) {
    var close: (() -> Unit)? = null
    lateinit var selection: SingleSelection

    val isActive: Boolean
        get() = selection.selected  === this

    fun activate() {
        selection.selected = this
    }

    class Line(val number: Int, val content: Content)

    interface Lines {
        val lineNumberDigitCount: Int get() = size.toString().length
        val size: Int
        operator fun get(index: Int): Line
    }

    class Content(val value: State<String>)
}

fun DocEditor(file: File) = DocEditor(
    fileName = file.name
) { backgroundScope ->
    val textLines = try {
        file.readLines(backgroundScope)
    } catch (e: Throwable) {
        e.printStackTrace()
        EmptyLines
    }

    fun content(index: Int): DocEditor.Content {
        val text = textLines.get(index)
        val state = mutableStateOf(text)
        return DocEditor.Content(state)
    }
    object: DocEditor.Lines {
        override val size get() = textLines.size

        override fun get(index: Int) = DocEditor.Line(
            number = index + 1,
            content = content(index)
        )
    }
}