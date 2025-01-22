package co.kr.tnt.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.moveToAppSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.data = Uri.parse("package:$packageName")
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}
