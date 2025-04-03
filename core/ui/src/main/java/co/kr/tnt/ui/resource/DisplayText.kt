package co.kr.tnt.ui.resource

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface DisplayText {
    fun asString(context: Context): String

    @JvmInline
    value class Plain(
        val value: String,
    ) : DisplayText {
        override fun asString(context: Context): String = value
    }

    class Resource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : DisplayText {
        override fun asString(context: Context): String = context.getString(resId, *args)
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is Plain -> value
            is Resource -> stringResource(resId, *args)
        }
    }

    companion object {
        val EMPTY = Plain("")
    }
}
