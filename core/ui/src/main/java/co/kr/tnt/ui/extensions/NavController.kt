package co.kr.tnt.ui.extensions

import androidx.navigation.NavController

/**
 * Back button 연속 클릭 시 빈 화면이 출력되는 현상 방지
 */
fun NavController.safePopBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == androidx.lifecycle.Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
