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
                TraineeModifyMyInfoUiEvent.OnBackClick -> navigateToBack()
                is TraineeModifyMyInfoUiEvent.OnProfileImageSelect -> {
                    profileImageUpdatePolicy = ProfileImageUpdatePolicy.Change(File(event.image.path))
                    updateProfileImage(event.image.path)
                }

                is TraineeModifyMyInfoUiEvent.OnNameChange -> updateName(event.name)
                is TraineeModifyMyInfoUiEvent.OnBirthdayChange -> updateBirthday(event.birthday)
                is TraineeModifyMyInfoUiEvent.OnHeightChange -> updateHeight(event.height)
                is TraineeModifyMyInfoUiEvent.OnWeightChange -> updateWeight(event.weight)
                is TraineeModifyMyInfoUiEvent.OnPurposeSelected -> updateSelectedPurposes(event.purpose)
                is TraineeModifyMyInfoUiEvent.OnCautionChange -> updateCaution(event.text)
                TraineeModifyMyInfoUiEvent.OnNextClick -> navigateToBack()
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

        private fun navigateToBack() {
            sendEffect(TraineeModifyMyInfoEffect.NavigateToBack)
        }
    }
