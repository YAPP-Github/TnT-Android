package co.kr.tnt.ui.utils

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import co.kr.tnt.domain.IMAGE_MAX_SIZE
import co.kr.tnt.ui.extensions.toResizedByteArray
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

fun Uri.convertToAllowedImageFormat(
    context: Context,
    maxSizeInBytes: Int = IMAGE_MAX_SIZE,
): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    val compressedBytes = bitmap.toResizedByteArray(maxSizeInBytes)
    val convertedFile = File(context.cacheDir, "image.jpg")
    val outputStream = convertedFile.outputStream()

    outputStream.use { it.write(compressedBytes) }
    outputStream.flush()
    outputStream.close()

    return convertedFile
}

fun isAllowedImageFormat(file: File): Boolean {
    val allowedExtensions = listOf("jpg", "jpeg", "png", "svg")
    return file.extension.lowercase() in allowedExtensions
}
