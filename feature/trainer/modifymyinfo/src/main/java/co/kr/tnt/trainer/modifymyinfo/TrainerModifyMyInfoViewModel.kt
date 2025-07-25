package co.kr.tnt.trainer.modifymyinfo

import androidx.lifecycle.viewModelScope
import co.kr.tnt.domain.UserProfilePolicy
import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.domain.model.User
import co.kr.tnt.domain.repository.TrainerRepository
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoEffect
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiEvent
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState
import co.kr.tnt.trainer.modifymyinfo.TrainerModifyMyInfoContract.TrainerModifyMyInfoUiState.DialogState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
internal class TrainerModifyMyInfoViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
) :
    BaseViewModel<TrainerModifyMyInfoUiState, TrainerModifyMyInfoUiEvent, TrainerModifyMyInfoEffect>(
            TrainerModifyMyInfoUiState(),
        ) {
        private var initializedInfo: User.Trainer? = null
        private var profileImageUpdatePolicy: ProfileImageUpdatePolicy = ProfileImageUpdatePolicy.Keep

        init {
            initMyInfo()
        }

        override suspend fun handleEvent(event: TrainerModifyMyInfoUiEvent) {
            when (event) {
                TrainerModifyMyInfoUiEvent.OnClickBack -> {
                    if (
                        isUpdateInfo(
                            initializedInfo = initializedInfo,
                            name = currentState.name,
                            image = currentState.profileImage,
                        )
                    ) {
                        updateState { copy(dialogState = DialogState.CONFIRM_EXIT) }
                        return
                    }

                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }

                is TrainerModifyMyInfoUiEvent.OnClickComplete -> {
                    viewModelScope.launch {
                        runCatching {
                            trainerRepository.updateUserInfo(
                                profileImageUpdatePolicy = profileImageUpdatePolicy,
                                name = currentState.name,
                            )
                        }.onSuccess {
                            sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                        }.onFailure {
                            sendEffect(TrainerModifyMyInfoEffect.ShowToast("서버 요청에 실패했어요"))
                        }
                    }
                }

                is TrainerModifyMyInfoUiEvent.OnNameChange -> {
                    updateState {
                        copy(
                            name = event.name,
                            isEnableComplete = isEnableModifyInfo(
                                initializedInfo = initializedInfo,
                                name = event.name,
                                image = currentState.profileImage,
                            ),
                        )
                    }
                }

                is TrainerModifyMyInfoUiEvent.OnProfileImageSelect -> {
                    profileImageUpdatePolicy = ProfileImageUpdatePolicy.Change(File(event.image.path))
                    updateState {
                        copy(
                            profileImage = event.image.path,
                            isEnableComplete = isEnableModifyInfo(
                                initializedInfo = initializedInfo,
                                name = currentState.name,
                                image = event.image.path,
                            ),
                        )
                    }
                }

                TrainerModifyMyInfoUiEvent.OnClickDialogConfirm -> {
                    updateState { copy(dialogState = DialogState.NONE) }
                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }

                TrainerModifyMyInfoUiEvent.OnDismissDialog -> updateState { copy(dialogState = DialogState.NONE) }
            }
        }

        private fun initMyInfo() {
            viewModelScope.launch {
                runCatching {
                    trainerRepository.getMyInfo()
                }.onSuccess { myInfo ->
                    initializedInfo = myInfo

                    updateState {
                        copy(
                            name = myInfo.name,
                            profileImage = myInfo.image,
                        )
                    }
                }.onFailure {
                    sendEffect(TrainerModifyMyInfoEffect.ShowToast("서버 요청에 실패했어요"))
                    sendEffect(TrainerModifyMyInfoEffect.NavigateToPrevious)
                }
            }
        }

        private fun isEnableModifyInfo(
            initializedInfo: User.Trainer?,
            name: String,
            image: String?,
        ): Boolean =
            isUpdateInfo(initializedInfo, name, image) &&
                name.isNotBlank() &&
                name.matches(UserProfilePolicy.USER_NAME_REGEX) &&
                name.length <= UserProfilePolicy.USER_NAME_MAX_LENGTH

        private fun isUpdateInfo(
            initializedInfo: User.Trainer?,
            name: String,
            image: String?,
        ): Boolean =
            initializedInfo?.let { it.name != name || it.image != image } ?: false
    }
