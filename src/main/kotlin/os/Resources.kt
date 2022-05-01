package os

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
expect fun Font(name: String, res: String, weight: FontWeight, style: FontStyle ): Font //This is throwing an error: No declaration for mainKt