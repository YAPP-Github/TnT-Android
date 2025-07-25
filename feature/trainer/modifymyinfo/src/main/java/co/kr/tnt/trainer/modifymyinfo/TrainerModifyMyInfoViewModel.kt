package co.kr.tnt.trainer.modifymyinfo

import androidx.lifecycle.viewModelScope
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
                        checkUpdatedInfo(
                            compareName = currentState.name,
                            compareImage = currentState.profileImage,
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
                            isEnableComplete = checkUpdatedInfo(
                                compareName = event.name,
                                compareImage = currentState.profileImage,
                            ),
                        )
                    }
                }

                is TrainerModifyMyInfoUiEvent.OnProfileImageSelect -> {
                    profileImageUpdatePolicy = ProfileImageUpdatePolicy.Change(File(event.image.path))
                    updateState {
                        copy(
                            profileImage = event.image.path,
                            isEnableComplete = checkUpdatedInfo(
                                compareName = currentState.name,
                                compareImage = event.image.path,
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

        private fun checkUpdatedInfo(
            compareName: String,
            compareImage: String?,
        ): Boolean = initializedInfo?.let {
            it.name != compareName || it.image != compareImage
        } ?: false
    }
