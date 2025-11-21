package co.kr.tnt.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import co.kr.tnt.core.designsystem.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
)

private val PretendardStyle = TextStyle(
    fontFamily = Pretendard,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

// Set of Material typography styles to start with
internal val Typography = TnTTypography(
    h1 = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = (28 * 1.4).sp,
        letterSpacing = (28 * -0.02).sp,
    ),
    h2 = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = (24 * 1.5).sp,
        letterSpacing = (24 * -0.02).sp,
    ),
    h3 = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = (20 * 1.5).sp,
        letterSpacing = (20 * -0.02).sp,
    ),
    h4 = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = (18 * 1.5).sp,
        letterSpacing = (18 * -0.02).sp,
    ),
    body1Bold = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = (16 * 1.5).sp,
        letterSpacing = (16 * -0.02).sp,
    ),
    body1Medium = PretendardStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = (16 * 1.6).sp,
        letterSpacing = (16 * -0.02).sp,
    ),
    body1SemiBold = PretendardStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = (16 * 1.5).sp,
        letterSpacing = (16 * -0.02).sp,
    ),
    body2Bold = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = (15 * 1.5).sp,
        letterSpacing = (15 * -0.02).sp,
    ),
    body2Medium = PretendardStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = (15 * 1.5).sp,
        letterSpacing = (15 * -0.02).sp,
    ),
    label1Bold = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = (13 * 1.3).sp,
        letterSpacing = (13 * -0.02).sp,
    ),
    label1Medium = PretendardStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = (13 * 1.5).sp,
        letterSpacing = (13 * -0.02).sp,
    ),
    label2Bold = PretendardStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = (12 * 1.5).sp,
        letterSpacing = (12 * -0.02).sp,
    ),
    label2Medium = PretendardStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = (12 * 1.5).sp,
        letterSpacing = (12 * -0.02).sp,
    ),
    caption1 = PretendardStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = (11 * 1.3).sp,
        letterSpacing = (11 * -0.02).sp,
    ),
)

@Immutable
data class TnTTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body1Bold: TextStyle,
    val body1Medium: TextStyle,
    val body1SemiBold: TextStyle,
    val body2Bold: TextStyle,
    val body2Medium: TextStyle,
    val label1Bold: TextStyle,
    val label1Medium: TextStyle,
    val label2Bold: TextStyle,
    val label2Medium: TextStyle,
    val caption1: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    TnTTypography(
        h1 = PretendardStyle,
        h2 = PretendardStyle,
        h3 = PretendardStyle,
        h4 = PretendardStyle,
        body1Bold = PretendardStyle,
        body1Medium = PretendardStyle,
        body1SemiBold = PretendardStyle,
        body2Bold = PretendardStyle,
        body2Medium = PretendardStyle,
        label1Bold = PretendardStyle,
        label1Medium = PretendardStyle,
        label2Bold = PretendardStyle,
        label2Medium = PretendardStyle,
        caption1 = PretendardStyle,
    )
}
