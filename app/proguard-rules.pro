# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

## ------------------------ firebase-crashlytics ------------------------
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
## ------------------------ firebase-crashlytics ------------------------

## ------------------------ firebase-analytics ------------------------
-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}
-keep public class com.google.android.gms.measurement.AppMeasurement {
    public *;
}
## ------------------------ firebase-analytics ------------------------

## ------------------ kakao -------------------
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter

# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**
## ------------------ kakao -------------------

-keep enum co.kr.tnt.navigation.model.ScreenMode { *; }

