package co.kr.tnt.ui.coil

import android.graphics.Bitmap
import coil.size.Size
import coil.transform.Transformation
import java.io.ByteArrayOutputStream

class ResizeTransformation(
    private val maxSizeInBytes: Int,
) : Transformation {
    override val cacheKey: String = "resize_to_max_size_$maxSizeInBytes"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        var bitmap = input
        var quality = 100

        while (true) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            val compressedSize = byteArrayOutputStream.size()

            if (compressedSize <= maxSizeInBytes) {
                break
            }

            bitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.9).toInt(),
                (bitmap.height * 0.9).toInt(),
                true,
            )
            quality -= 5
        }

        return bitmap
    }
}
