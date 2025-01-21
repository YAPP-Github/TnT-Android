package co.kr.tnt.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val FILL_STRONG = Color(0x2970737C)

// Main color
val Red_950 = Color(0xFF450A0A)
val Red_900 = Color(0xFF7F1D1D)
val Red_800 = Color(0xFF991B1B)
val Red_700 = Color(0xFFB91C1C)
val Red_600 = Color(0xFFDC2626)
val Red_500 = Color(0xFFEF4444)
val Red_400 = Color(0xFFF87171)
val Red_300 = Color(0xFFFCA5A5)
val Red_200 = Color(0xFFFECACA)
val Red_100 = Color(0xFFFEE2E2)
val Red_50 = Color(0xFFFFF0F0)

// Green Color
val Green_950 = Color(0xFF00240C)
val Green_900 = Color(0xFF004517)
val Green_800 = Color(0xFF006E25)
val Green_700 = Color(0xFF009632)
val Green_600 = Color(0xFF00BF40)
val Green_500 = Color(0xFF1ED45A)
val Green_400 = Color(0xFF49E57D)
val Green_300 = Color(0xFF7DF5A5)
val Green_200 = Color(0xFFACFCC7)
val Green_100 = Color(0xFFD9FFE6)

// Orange Color
val Orange_950 = Color(0xFF361E00)
val Orange_900 = Color(0xFF663A00)
val Orange_800 = Color(0xFF9C5800)
val Orange_700 = Color(0xFFD47800)
val Orange_600 = Color(0xFFFF9200)
val Orange_500 = Color(0xFFFFA938)
val Orange_400 = Color(0xFFFFC06E)
val Orange_300 = Color(0xFFFFD49C)
val Orange_200 = Color(0xFFFEE6C6)
val Orange_100 = Color(0xFFFEF4E6)
val Orange_50 = Color(0xFFFFFCF7)

// Lime Color
val Lime_950 = Color(0xFF112900)
val Lime_900 = Color(0xFF225200)
val Lime_800 = Color(0xFF347D00)
val Lime_700 = Color(0xFF48AD00)
val Lime_600 = Color(0xFF58CF04)
val Lime_500 = Color(0xFF6BE016)
val Lime_400 = Color(0xFF88F03E)
val Lime_300 = Color(0xFFAEF779)
val Lime_200 = Color(0xFFCCFCA9)
val Lime_100 = Color(0xFFE6FFD4)
val Lime_50 = Color(0xFFF8FFF2)

// Blue Color
val Blue_950 = Color(0xFF002130)
val Blue_900 = Color(0xFF004261)
val Blue_800 = Color(0xFF006796)
val Blue_700 = Color(0xFF008DCF)
val Blue_600 = Color(0xFF00AEFF)
val Blue_500 = Color(0xFF3DC2FF)
val Blue_400 = Color(0xFF70D2FF)
val Blue_300 = Color(0xFFA1E1FF)
val Blue_200 = Color(0xFFC4ECFE)
val Blue_100 = Color(0xFFE5F6FE)
val Blue_50 = Color(0xFFF7FDFF)

// Violet Color
val Violet_950 = Color(0xFF11024D)
val Violet_900 = Color(0xFF23098F)
val Violet_800 = Color(0xFF3A16C9)
val Violet_700 = Color(0xFF4F29E5)
val Violet_600 = Color(0xFF6541F2)
val Violet_500 = Color(0xFF7D5EF7)
val Violet_400 = Color(0xFF9E86FC)
val Violet_300 = Color(0xFFC0B0FF)
val Violet_200 = Color(0xFFDBD3FE)
val Violet_100 = Color(0xFFF0ECFE)
val Violet_50 = Color(0xFFFBFAFF)

// Purple Color
val Purple_950 = Color(0xFF290247)
val Purple_900 = Color(0xFF580A7D)
val Purple_800 = Color(0xFF861CB8)
val Purple_700 = Color(0xFFAD36E3)
val Purple_600 = Color(0xFFCB59FF)
val Purple_500 = Color(0xFFD478FF)
val Purple_400 = Color(0xFFDE96FF)
val Purple_300 = Color(0xFFE9BAFF)
val Purple_200 = Color(0xFFF2D6FF)
val Purple_100 = Color(0xFFF9EDFF)
val Purple_50 = Color(0xFFFEFBFF)

// Pink Color
val Pink_950 = Color(0xFF3D0133)
val Pink_900 = Color(0xFF730560)
val Pink_800 = Color(0xFFA81690)
val Pink_700 = Color(0xFFD331B8)
val Pink_600 = Color(0xFFF553DA)
val Pink_500 = Color(0xFFFA73E3)
val Pink_400 = Color(0xFFFF94ED)
val Pink_300 = Color(0xFFFFB8F3)
val Pink_200 = Color(0xFFFED3F7)
val Pink_100 = Color(0xFFFEECFB)
val Pink_50 = Color(0xFFFFFAFE)

// Neutral Color
val Neutral_950 = Color(0xFF0A0A0A)
val Neutral_900 = Color(0xFF171717)
val Neutral_800 = Color(0xFF262626)
val Neutral_700 = Color(0xFF404040)
val Neutral_600 = Color(0xFF525252)
val Neutral_500 = Color(0xFF737373)
val Neutral_400 = Color(0xFFA3A3A3)
val Neutral_300 = Color(0xFFD4D4D4)
val Neutral_200 = Color(0xFFE5E5E5)
val Neutral_100 = Color(0xFFF5F5F5)
val Neutral_50 = Color(0xFFFAFAFA)

// Common Color
val Common_100 = Color(0xFF000000)
val Common_0 = Color(0xFFFFFFFF)

@Immutable
data class TnTColors(
    val mainColors: MainColors,
    val greenColors: GreenColors,
    val orangeColors: OrangeColors,
    val limeColors: LimeColors,
    val blueColors: BlueColors,
    val violetColors: VioletColors,
    val purpleColors: PurpleColors,
    val pinkColors: PinkColors,
    val neutralColors: NeutralColors,
    val commonColors: CommonColors,
)

@Immutable
data class MainColors(
    val Red950: Color,
    val Red900: Color,
    val Red800: Color,
    val Red700: Color,
    val Red600: Color,
    val Red500: Color,
    val Red400: Color,
    val Red300: Color,
    val Red200: Color,
    val Red100: Color,
    val Red50: Color,
)

@Immutable
data class GreenColors(
    val Green950: Color,
    val Green900: Color,
    val Green800: Color,
    val Green700: Color,
    val Green600: Color,
    val Green500: Color,
    val Green400: Color,
    val Green300: Color,
    val Green200: Color,
    val Green100: Color,
)

@Immutable
data class OrangeColors(
    val Orange950: Color,
    val Orange900: Color,
    val Orange800: Color,
    val Orange700: Color,
    val Orange600: Color,
    val Orange500: Color,
    val Orange400: Color,
    val Orange300: Color,
    val Orange200: Color,
    val Orange100: Color,
    val Orange50: Color,
)

@Immutable
data class LimeColors(
    val Lime950: Color,
    val Lime900: Color,
    val Lime800: Color,
    val Lime700: Color,
    val Lime600: Color,
    val Lime500: Color,
    val Lime400: Color,
    val Lime300: Color,
    val Lime200: Color,
    val Lime100: Color,
    val Lime50: Color,
)

@Immutable
data class BlueColors(
    val Blue950: Color,
    val Blue900: Color,
    val Blue800: Color,
    val Blue700: Color,
    val Blue600: Color,
    val Blue500: Color,
    val Blue400: Color,
    val Blue300: Color,
    val Blue200: Color,
    val Blue100: Color,
    val Blue50: Color,
)

@Immutable
data class VioletColors(
    val Violet950: Color,
    val Violet900: Color,
    val Violet800: Color,
    val Violet700: Color,
    val Violet600: Color,
    val Violet500: Color,
    val Violet400: Color,
    val Violet300: Color,
    val Violet200: Color,
    val Violet100: Color,
    val Violet50: Color,
)

@Immutable
data class PurpleColors(
    val Purple950: Color,
    val Purple900: Color,
    val Purple800: Color,
    val Purple700: Color,
    val Purple600: Color,
    val Purple500: Color,
    val Purple400: Color,
    val Purple300: Color,
    val Purple200: Color,
    val Purple100: Color,
    val Purple50: Color,
)

@Immutable
data class PinkColors(
    val Pink950: Color,
    val Pink900: Color,
    val Pink800: Color,
    val Pink700: Color,
    val Pink600: Color,
    val Pink500: Color,
    val Pink400: Color,
    val Pink300: Color,
    val Pink200: Color,
    val Pink100: Color,
    val Pink50: Color,
)

@Immutable
data class NeutralColors(
    val Neutral950: Color,
    val Neutral900: Color,
    val Neutral800: Color,
    val Neutral700: Color,
    val Neutral600: Color,
    val Neutral500: Color,
    val Neutral400: Color,
    val Neutral300: Color,
    val Neutral200: Color,
    val Neutral100: Color,
    val Neutral50: Color,
)

@Immutable
data class CommonColors(
    val Common100: Color,
    val Common0: Color,
)

val LocalColors = staticCompositionLocalOf {
    TnTColors(
        mainColors = MainColors(
            Red950 = Red_950,
            Red900 = Red_900,
            Red800 = Red_800,
            Red700 = Red_700,
            Red600 = Red_600,
            Red500 = Red_500,
            Red400 = Red_400,
            Red300 = Red_300,
            Red200 = Red_200,
            Red100 = Red_100,
            Red50 = Red_50,
        ),
        greenColors = GreenColors(
            Green950 = Green_950,
            Green900 = Green_900,
            Green800 = Green_800,
            Green700 = Green_700,
            Green600 = Green_600,
            Green500 = Green_500,
            Green400 = Green_400,
            Green300 = Green_300,
            Green200 = Green_200,
            Green100 = Green_100,
        ),
        orangeColors = OrangeColors(
            Orange950 = Orange_950,
            Orange900 = Orange_900,
            Orange800 = Orange_800,
            Orange700 = Orange_700,
            Orange600 = Orange_600,
            Orange500 = Orange_500,
            Orange400 = Orange_400,
            Orange300 = Orange_300,
            Orange200 = Orange_200,
            Orange100 = Orange_100,
            Orange50 = Orange_50,
        ),
        limeColors = LimeColors(
            Lime950 = Lime_950,
            Lime900 = Lime_900,
            Lime800 = Lime_800,
            Lime700 = Lime_700,
            Lime600 = Lime_600,
            Lime500 = Lime_500,
            Lime400 = Lime_400,
            Lime300 = Lime_300,
            Lime200 = Lime_200,
            Lime100 = Lime_100,
            Lime50 = Lime_50,
        ),
        blueColors = BlueColors(
            Blue950 = Blue_950,
            Blue900 = Blue_900,
            Blue800 = Blue_800,
            Blue700 = Blue_700,
            Blue600 = Blue_600,
            Blue500 = Blue_500,
            Blue400 = Blue_400,
            Blue300 = Blue_300,
            Blue200 = Blue_200,
            Blue100 = Blue_100,
            Blue50 = Blue_50,
        ),
        violetColors = VioletColors(
            Violet950 = Violet_950,
            Violet900 = Violet_900,
            Violet800 = Violet_800,
            Violet700 = Violet_700,
            Violet600 = Violet_600,
            Violet500 = Violet_500,
            Violet400 = Violet_400,
            Violet300 = Violet_300,
            Violet200 = Violet_200,
            Violet100 = Violet_100,
            Violet50 = Violet_50,
        ),
        purpleColors = PurpleColors(
            Purple950 = Purple_950,
            Purple900 = Purple_900,
            Purple800 = Purple_800,
            Purple700 = Purple_700,
            Purple600 = Purple_600,
            Purple500 = Purple_500,
            Purple400 = Purple_400,
            Purple300 = Purple_300,
            Purple200 = Purple_200,
            Purple100 = Purple_100,
            Purple50 = Purple_50,
        ),
        pinkColors = PinkColors(
            Pink950 = Pink_950,
            Pink900 = Pink_900,
            Pink800 = Pink_800,
            Pink700 = Pink_700,
            Pink600 = Pink_600,
            Pink500 = Pink_500,
            Pink400 = Pink_400,
            Pink300 = Pink_300,
            Pink200 = Pink_200,
            Pink100 = Pink_100,
            Pink50 = Pink_50,
        ),
        neutralColors = NeutralColors(
            Neutral950 = Neutral_950,
            Neutral900 = Neutral_900,
            Neutral800 = Neutral_800,
            Neutral700 = Neutral_700,
            Neutral600 = Neutral_600,
            Neutral500 = Neutral_500,
            Neutral400 = Neutral_400,
            Neutral300 = Neutral_300,
            Neutral200 = Neutral_200,
            Neutral100 = Neutral_100,
            Neutral50 = Neutral_50,
        ),
        commonColors = CommonColors(
            Common100 = Common_100,
            Common0 = Common_0,
        ),
    )
}
