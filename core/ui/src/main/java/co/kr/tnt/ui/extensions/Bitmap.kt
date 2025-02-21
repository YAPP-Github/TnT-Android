package co.kr.tnt.ui.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toResizedByteArray(maxSizeInBytes: Int): ByteArray {
    var resizedBitmap = this
    var quality = 100

    val byteArrayOutputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
    var compressedSize = byteArrayOutputStream.size()

    while (compressedSize > maxSizeInBytes) {
        resizedBitmap = Bitmap.createScaledBitmap(
            resizedBitmap,
            (resizedBitmap.width * 0.9).toInt(),
            (resizedBitmap.height * 0.9).toInt(),
            true,
        )

        if (quality > 50) {
            quality -= 5
        }

        byteArrayOutputStream.reset()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        compressedSize = byteArrayOutputStream.size()
    }

    return byteArrayOutputStream.toByteArray()
}
