package be.kdg.filmapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Black,
        fontSize      = 32.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Black,
        fontSize      = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Black,
        fontSize      = 20.sp,
        letterSpacing = 0.5.sp,
    ),
    titleLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Bold,
        fontSize      = 18.sp,
        letterSpacing = 1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Bold,
        fontSize      = 13.sp,
        letterSpacing = 2.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Light,
        fontSize      = 16.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Light,
        fontSize      = 14.sp,
        letterSpacing = 0.sp,
    ),
    labelLarge = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Bold,
        fontSize      = 11.sp,
        letterSpacing = 2.sp,
    ),
    labelMedium = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Bold,
        fontSize      = 10.sp,
        letterSpacing = 1.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily    = FontFamily.Default,
        fontWeight    = FontWeight.Bold,
        fontSize      = 9.sp,
        letterSpacing = 3.sp,
    ),
)
