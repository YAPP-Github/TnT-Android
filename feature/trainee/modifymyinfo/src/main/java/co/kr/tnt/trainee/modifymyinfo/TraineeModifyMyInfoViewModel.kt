package co.kr.tnt.trainee.modifymyinfo

import co.kr.tnt.domain.model.ProfileImageUpdatePolicy
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoEffect
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiEvent
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState
import co.kr.tnt.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class TraineeModifyMyInfoViewModel @Inject constructor() :
    BaseViewModel<TraineeModifyMyInfoUiState, TraineeModifyMyInfoUiEvent, TraineeModifyMyInfoEffect>(
        TraineeModifyMyInfoUiState(),
    ) {
        private var profileImageUpdatePolicy: ProfileImageUpdatePolicy = ProfileImageUpdatePolicy.Keep

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
                TraineeModifyMyInfoUiEvent.OnClickBack -> navigateToBack()
                TraineeModifyMyInfoUiEvent.OnClickComplete -> updateUserInfo()
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
            sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
        }
    }
