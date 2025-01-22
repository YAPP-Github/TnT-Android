package co.kr.tnt.ui.permission

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.os.Build
import androidx.annotation.StringRes
import co.kr.tnt.core.ui.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted

enum class TnTPermission(
    val values: List<String>,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val permanentlyDeniedTitle: Int,
    @StringRes val permanentlyDeniedDescription: Int,
) {
    NOTIFICATION(
        values = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(POST_NOTIFICATIONS)
        } else {
            emptyList()
        },
        title = R.string.alarm_permission_title,
        description = R.string.alarm_permission_description,
        permanentlyDeniedTitle = R.string.alarm_permission_permanently_title,
        permanentlyDeniedDescription = R.string.alarm_permission_permanently_description,
    ),
    MEDIA_ACCESS(
        values = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> listOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_VIDEO,
                READ_MEDIA_VISUAL_USER_SELECTED,
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_VIDEO,
            )

            else -> listOf(READ_EXTERNAL_STORAGE)
        },
        title = R.string.media_permission_title,
        description = R.string.media_permission_description,
        permanentlyDeniedTitle = R.string.media_permission_permanently_title,
        permanentlyDeniedDescription = R.string.media_permission_permanently_description,
    ),
    ;

    /**
     * 모든 "필수" 권한이 허용되었는지 확인하고 결과를 리턴합니다.
     *
     * 예를 들어 미디어 접근 허용 권한의 경우 Android 14부터 '사진/동영상 일부 접근 권한'만 허용하더라도,
     * "필수" 권한이 허용되었다는 것으로 인식합니다.
     *
     * @return 모든 "필수" 권한이 허용되었는지에 대한 여부
     */
    @OptIn(ExperimentalPermissionsApi::class)
    fun isRequireGranted(permissions: MultiplePermissionsState): Boolean {
        return when (this) {
            NOTIFICATION -> permissions.allPermissionsGranted
            MEDIA_ACCESS -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    return permissions.allPermissionsGranted
                }

                return permissions.allPermissionsGranted ||
                    permissions.permissions.any {
                        it.permission == READ_MEDIA_VISUAL_USER_SELECTED && it.status.isGranted
                    }
            }
        }
    }
}
