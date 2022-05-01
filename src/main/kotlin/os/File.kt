package os

import kotlinx.coroutines.CoroutineScope
import util.TextLines

expect val HomeFolder: File //This is throwing an error: No declaration for mainKt

interface File {
    val name: String
    val isDirectory: Boolean
    val children: List<File>
    val hasChildren: Boolean

    fun readLines(scope: CoroutineScope): TextLines
}