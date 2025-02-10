package co.kr.tnt.main

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.domain.repository.LoginRepository
import co.kr.tnt.domain.repository.SettingRepository
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
    private val loginRepository: LoginRepository,
    private val settingRepository: SettingRepository,
) :
    BaseViewModel<MainUiState, MainUiEvent, MainSideEffect>(MainUiState()) {
        init {
            viewModelScope.launch {
                val startDestination = getStartDestination()

                updateState {
                    copy(
                        showSplash = false,
                        startDestination = startDestination,
                    )
                }
            }
        }

        private suspend fun getStartDestination(): Route {
            return runCatching {
                loginRepository.getUserType()
            }.fold(
                onSuccess = { userType ->
                    when (userType) {
                        UserType.TRAINER -> Route.TrainerMain
                        UserType.TRAINEE -> Route.TraineeMain
                    }
                },
                onFailure = {
                    Route.Login
                },
            )
        }

        override suspend fun handleEvent(event: MainUiEvent) {
            when (event) {
                MainUiEvent.OnNotificationPermissionRevoked -> settingRepository.setEnablePushNotification(false)
            }
        }
    }
