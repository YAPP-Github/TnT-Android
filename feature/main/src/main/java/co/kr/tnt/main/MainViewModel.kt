package co.kr.tnt.main

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.main.MainContract.MainSideEffect
import co.kr.tnt.main.MainContract.MainUiEvent
import co.kr.tnt.main.MainContract.MainUiState
import co.kr.tnt.navigation.Route
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    loginRepository: LoginRepository,
) :
    BaseViewModel<MainUiState, MainUiEvent, MainSideEffect>(MainUiState()) {
        init {
            viewModelScope.launch {
                val isNeedLogin = loginRepository.isNeedLogin()
                val startDestination: Route = if (isNeedLogin) {
                    Route.Login
                } else {
                    Route.TrainerMain
                }

                updateState {
                    copy(
                        showSplash = false,
                        startDestination = startDestination,
                    )
                }
            }
        }

        override suspend fun handleEvent(event: MainUiEvent) = Unit
    }
