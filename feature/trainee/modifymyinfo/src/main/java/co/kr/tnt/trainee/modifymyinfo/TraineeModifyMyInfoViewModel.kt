package co.kr.tnt.trainee.modifymyinfo

import androidx.lifecycle.viewModelScope
import co.kr.tnt.core.ui.R.string.core_failed_to_server_request
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
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
        private var initializedInfo: User.Trainee? = null
        private var profileImageUpdatePolicy: ProfileImageUpdatePolicy = ProfileImageUpdatePolicy.Keep

        init {
            loadUserInfo()
        }

        override suspend fun handleEvent(event: TraineeModifyMyInfoUiEvent) {
            when (event) {
                TraineeModifyMyInfoUiEvent.OnDeleteProfileImage -> {
                    profileImageUpdatePolicy = ProfileImageUpdatePolicy.Remove
                    deleteProfileImage()
                }

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
                    initializedInfo = user

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

        private fun updateProfileImage(image: String) {
            updateState { copy(profileImage = image) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun deleteProfileImage() {
            updateState { copy(profileImage = null) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateName(name: String) {
            updateState { copy(name = name) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateBirthday(birthday: LocalDate) {
            updateState { copy(birthday = birthday) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateHeight(height: String) {
            updateState { copy(height = height) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateWeight(weight: String) {
            updateState { copy(weight = weight) }
            isEnableModifyInfo(initializedInfo)
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
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateCaution(caution: String) {
            updateState { copy(caution = caution) }
            isEnableModifyInfo(initializedInfo)
        }

        private fun updateUserInfo() {
            viewModelScope.launch {
                updateState { copy(isLoading = true) }
                val userInfo = User.Trainee.EMPTY
                runCatching {
                    traineeRepository.updateUserInfo(
                        profileImageUpdatePolicy = profileImageUpdatePolicy,
                        userInfo = userInfo.copy(
                            name = currentState.name,
                            image = currentState.profileImage,
                            birthday = currentState.birthday,
                            weight = currentState.weight?.toDoubleOrNull(),
                            height = currentState.height?.toIntOrNull(),
                            ptPurpose = currentState.ptPurpose,
                            caution = currentState.caution,
                        ),
                    )
                }.onSuccess {
                    sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
                }.onFailure {
                    sendEffect(
                        TraineeModifyMyInfoEffect.ShowToast(
                            DisplayText.Resource(core_failed_to_server_request),
                        ),
                    )
                }.also {
                    updateState { copy(isLoading = false) }
                }
            }
        }

        private fun navigateToBack() {
            if (
                isUpdateInfo(
                    initializedInfo = initializedInfo,
                    name = currentState.name,
                    image = currentState.profileImage,
                    birthday = currentState.birthday,
                    height = currentState.height?.toIntOrNull(),
                    weight = currentState.weight?.toDoubleOrNull(),
                    ptPurpose = currentState.ptPurpose,
                    caution = currentState.caution,
                )
            ) {
                updateState { copy(dialogState = DialogState.CONFIRM_EXIT) }
                return
            }

            sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
        }

        private fun isEnableModifyInfo(initializedInfo: User.Trainee?) {
            val isEnable = isUpdateInfo(
                initializedInfo,
                currentState.name,
                currentState.profileImage,
                currentState.birthday,
                currentState.height?.toIntOrNull(),
                currentState.weight?.toDoubleOrNull(),
                currentState.ptPurpose,
                currentState.caution,
            ) && currentState.isNameValid &&
                currentState.isWeightValid &&
                currentState.isWeightValid &&
                currentState.isCautionNoteValid

            updateState { copy(isEnableComplete = isEnable) }
        }

        private fun isUpdateInfo(
            initializedInfo: User.Trainee?,
            name: String,
            image: String?,
            birthday: LocalDate?,
            height: Int?,
            weight: Double?,
            ptPurpose: List<String>?,
            caution: String?,
        ): Boolean =
            initializedInfo?.let {
                it.name != name ||
                    it.image != image ||
                    it.birthday != birthday ||
                    it.height != height ||
                    it.weight != weight ||
                    it.ptPurpose?.toSet() != ptPurpose?.toSet() ||
                    it.caution != caution
            } ?: false
    }
