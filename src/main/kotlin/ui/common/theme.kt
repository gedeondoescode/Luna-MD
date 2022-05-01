package ui.common

import androidx.compose.material.darkColors
//import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object AppTheme {
    val colors: Colors = Colors()

    class Colors(
        val backgroundDark: Color = Color(0x212529),
        val backgroundDarkMedium: Color = Color(0x343a40),
        val backgroundLight: Color = Color(0xffffff),
        val backgroundLightMedium: Color = Color(0xe5e5e5),

        val material: androidx.compose.material.Colors = darkColors(
            background = backgroundDark,
            surface = backgroundDarkMedium,
            primary = Color.White
        ),
    )
}