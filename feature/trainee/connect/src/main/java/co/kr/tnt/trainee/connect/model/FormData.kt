package co.kr.tnt.trainee.connect.model

import co.kr.tnt.domain.model.User
import java.time.LocalDate

sealed class FormData {
    data class ProfileData(
        val trainer: User.Trainer = User.Trainer.EMPTY,
        val trainee: User.Trainee = User.Trainee.EMPTY,
    ) : FormData()

    data class PTSessionData(
        val selectedStartDate: LocalDate = LocalDate.now(),
        val completedSession: Int = 0,
        val totalSession: Int = 0,
    ) : FormData()
}
