package co.kr.tnt.trainee.connect

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.ConnectRepository
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectPage
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectSideEffect
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiEvent
import co.kr.tnt.trainee.connect.TraineeConnectContract.TraineeConnectUiState
import co.kr.tnt.trainee.connect.model.FormData
import co.kr.tnt.trainee.connect.model.InputState.INVALID
import co.kr.tnt.trainee.connect.model.InputState.VALID
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraineeConnectViewModel @Inject constructor(
    private val connectRepository: ConnectRepository,
) :
    BaseViewModel<TraineeConnectUiState, TraineeConnectUiEvent, TraineeConnectSideEffect>(
            TraineeConnectUiState(),
        ) {
        init {
            initProfile()
        }

        override suspend fun handleEvent(event: TraineeConnectUiEvent) {
            when (event) {
                is TraineeConnectUiEvent.OnCodeValidateClick -> validateCode(event.code)
                is TraineeConnectUiEvent.OnCodeChanged -> resetCode(event.code)
                is TraineeConnectUiEvent.OnNextClick -> navigateToNext(event.data)
                TraineeConnectUiEvent.OnBackClick -> navigateToBack()
                TraineeConnectUiEvent.OnSkipClick -> navigateToHome()
            }
        }

        private fun initProfile() {
            // TODO 연결 완료 화면에 보여줄 프로필 정보 불러오기
            updateState {
                copy(
                    formData = FormData.ProfileData(
                        trainer = User.Trainer(
                            id = "trainer",
                            name = "김헬짱",
                            image = null,
                        ),
                        trainee = User.Trainee(
                            id = "trainee",
                            name = "김회원",
                            image = "https://buly.kr/3j7VVqN",
                            birthday = null,
                            age = 25,
                            weight = 100.0,
                            height = 165,
                            ptPurpose = listOf("체중 감량", "자세 교정"),
                            caution = "발목이 안좋아서 발목에 무리가는 행동을 하면 안돼요. 잘 부탁드려요!",
                        ),
                    ),
                )
            }
        }

        private fun validateCode(code: String) {
            viewModelScope.launch {
                runCatching {
                    connectRepository.verifyInviteCode(code)
                }.onSuccess { result ->
                    if (result) {
                        updateState { copy(inviteCode = code, isCodeValid = VALID) }
                    } else {
                        updateState { copy(inviteCode = code, isCodeValid = INVALID) }
                    }
                }.onFailure {
                    sendEffect(TraineeConnectSideEffect.ShowToast("서버 요청에 실패했어요"))
                }
            }
        }

        private fun resetCode(code: String) {
            updateState { copy(inviteCode = code, isCodeValid = null) }
        }

        private fun navigateToNext(data: FormData?) {
            val nextPage = when (currentState.page) {
                TraineeConnectPage.PTSessionForm -> {
                    data?.let { updatePTSessionForm(it) }
                    TraineeConnectPage.getNextPage(currentState.page)
                }

                TraineeConnectPage.TraineeConnectComplete -> {
                    sendEffect(TraineeConnectSideEffect.NavigateToHome)
                    return
                }

                else -> TraineeConnectPage.getNextPage(currentState.page)
            }
            updateState { copy(page = nextPage) }
        }

        private fun updatePTSessionForm(data: FormData) {
            updateState { copy(formData = data) }
        }

        private fun navigateToBack() {
            val previousPage = when (currentState.page) {
                TraineeConnectPage.CodeEntry -> {
                    sendEffect(TraineeConnectSideEffect.NavigateToBack)
                    return
                }

                else -> TraineeConnectPage.getPreviousPage(currentState.page)
            }
            updateState { copy(page = previousPage) }
        }

        private fun navigateToHome() {
            sendEffect(TraineeConnectSideEffect.NavigateToHome)
        }
    }
