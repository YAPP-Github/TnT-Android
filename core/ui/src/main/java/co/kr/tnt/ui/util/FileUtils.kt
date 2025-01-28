package co.kr.tnt.ui.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun Uri.toMultiPart(context: Context, keyName: String = "image"): MultipartBody.Part? {
    return getRealPathFromUri(this, context)?.let { filePath ->
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(keyName, file.name, requestFile)
    } ?: run {
        Log.e("toMultipartImagePart", "Error creating multipart image for URI: $this")
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
