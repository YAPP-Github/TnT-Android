package co.kr.tnt.ui.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.provider.Settings
import android.util.Log

fun Context.moveToAppSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.data = Uri.parse("package:$packageName")
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun Context.getAppVersion(): String {
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return packageInfo.versionName ?: throw NotFoundException()
    } catch (e: Exception) {
        Log.e("Version not found", "버전 확인에 실패하였습니다.")
        return "0.0.0"
    }
}
