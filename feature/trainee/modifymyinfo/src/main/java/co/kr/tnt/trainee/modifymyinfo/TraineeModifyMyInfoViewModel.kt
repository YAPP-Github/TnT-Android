package co.kr.tnt.trainee.modifymyinfo

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.repository.TraineeRepository
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoEffect
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiEvent
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import co.kr.tnt.ui.resource.DisplayText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeModifyMyInfoViewModel @Inject constructor(
    private val traineeRepository: TraineeRepository,
) :
    BaseViewModel<TraineeModifyMyInfoUiState, TraineeModifyMyInfoUiEvent, TraineeModifyMyInfoEffect>(
            TraineeModifyMyInfoUiState(),
        ) {
        private var profileImageUpdatePolicy: ProfileImageUpdatePolicy = ProfileImageUpdatePolicy.Keep

        init {
            loadUserInfo()
        }

        override suspend fun handleEvent(event: TraineeModifyMyInfoUiEvent) {
            when (event) {
                TraineeModifyMyInfoUiEvent.OnDeleteProfileImage -> deleteProfileImage()
                is TraineeModifyMyInfoUiEvent.OnProfileImageSelect -> {
                    profileImageUpdatePolicy = ProfileImageUpdatePolicy.Change(File(event.image.path))
                    updateProfileImage(event.image.path)
                }

                is TraineeModifyMyInfoUiEvent.OnChangeName -> updateName(event.name)
                is TraineeModifyMyInfoUiEvent.OnChangeBirthday -> updateBirthday(event.birthday)
                is TraineeModifyMyInfoUiEvent.OnChangeHeight -> updateHeight(event.height)
                is TraineeModifyMyInfoUiEvent.OnChangeWeight -> updateWeight(event.weight)
                is TraineeModifyMyInfoUiEvent.OnSelectPurpose -> updateSelectedPurposes(event.purpose)
                is TraineeModifyMyInfoUiEvent.OnChangeCaution -> updateCaution(event.text)
                TraineeModifyMyInfoUiEvent.OnClickDialogConfirm -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
                }

                TraineeModifyMyInfoUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
                TraineeModifyMyInfoUiEvent.OnClickBack -> navigateToBack()
                TraineeModifyMyInfoUiEvent.OnClickComplete -> updateUserInfo()
            }
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                runCatching {
                    traineeRepository.getMyInfo()
                }.onSuccess { user ->
                    updateState {
                        copy(
                            profileImage = user.image,
                            name = user.name,
                            birthday = user.birthday,
                            height = user.height?.toString(),
                            weight = user.weight?.toString(),
                            ptPurpose = user.ptPurpose,
                            caution = user.caution,
                        )
                    }
                }.onFailure {
                    sendEffect(
                        TraineeModifyMyInfoEffect.ShowToast(
                            DisplayText.Resource(core_failed_to_server_request),
                        ),
                    )
                }
            }
        }

        private fun updateSelectedPurposes(purpose: String) {
            val updatedPurposes = currentState.ptPurpose.orEmpty().toMutableList().apply {
                if (contains(purpose)) {
                    remove(purpose)
                } else {
                    add(purpose)
                }
            }
            updateState { copy(ptPurpose = updatedPurposes) }
        }

        private fun updateCaution(caution: String) {
            updateState { copy(caution = caution) }
        }

        private fun updateHeight(height: String) {
            updateState { copy(height = height) }
        }

        private fun updateWeight(weight: String) {
            updateState { copy(weight = weight) }
        }

        private fun updateBirthday(birthday: LocalDate) {
            updateState { copy(birthday = birthday) }
        }

        private fun updateName(name: String) {
            updateState { copy(name = name) }
        }

        private fun updateProfileImage(image: String) {
            updateState { copy(profileImage = image) }
        }

        private fun deleteProfileImage() {
            updateState { copy(profileImage = null) }
        }

        private fun updateUserInfo() {
            // TODO 수정 api 호출
            sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
        }

        private fun navigateToBack() {
            // TODO 수정된 항목 있나 확인 후, 있을 때만 dialog 띄우기
            updateState { copy(dialogState = DialogState.CONFIRM_EXIT) }
            return
        }
    }
