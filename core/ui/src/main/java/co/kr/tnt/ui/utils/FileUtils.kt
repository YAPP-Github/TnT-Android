package co.kr.tnt.ui.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream

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

fun Uri.convertToAllowedImageFormat(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val convertedFile = File(context.cacheDir, "image.png")
    val outputStream = FileOutputStream(convertedFile)

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return convertedFile
}

fun isAllowedImageFormat(file: File): Boolean {
    val allowedExtensions = listOf("jpg", "jpeg", "png", "svg")
    return file.extension.lowercase() in allowedExtensions
}
