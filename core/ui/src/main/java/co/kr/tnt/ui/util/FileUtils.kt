package co.kr.tnt.ui.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File

fun Uri.toFile(context: Context): File? {
    return getRealPathFromUri(this, context)?.let { filePath ->
        File(filePath)
    } ?: run {
        Log.e("toFile", "Error creating file for URI: $this")
        null
    }
}

fun getRealPathFromUri(uri: Uri, context: Context): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
    }
    return null
}
