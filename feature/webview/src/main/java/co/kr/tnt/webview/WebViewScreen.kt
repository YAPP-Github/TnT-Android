package co.kr.tnt.webview

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import co.kr.tnt.designsystem.theme.TnTTheme

@Composable
internal fun WebViewScreen(
    url: String,
    onClickBack: () -> Unit,
) {
    var webView: WebView? = null

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onClickBack()
        }
    }

    Scaffold(contentColor = TnTTheme.colors.commonColors.Common0) { innerPadding ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    webViewClient = WebViewClient()
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    loadUrl(url)
                    webView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
