import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import os.HomeFolder

@Composable
fun MainView() {
    val lunaView = remember {

        LunaView(
            fileView = FileView(HomeFolder)
        )
    }
}