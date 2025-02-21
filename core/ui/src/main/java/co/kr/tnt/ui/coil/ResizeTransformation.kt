package co.kr.tnt.ui.coil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import co.kr.tnt.ui.extensions.toResizedByteArray
import coil.size.Size
import coil.transform.Transformation

class ResizeTransformation(
    private val maxSizeInBytes: Int,
) : Transformation {
    override val cacheKey: String = "resize_to_max_size_$maxSizeInBytes"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap =
        input.toResizedByteArray(maxSizeInBytes).let { resizedByteArray ->
            BitmapFactory.decodeByteArray(resizedByteArray, 0, resizedByteArray.size)
        }
}
