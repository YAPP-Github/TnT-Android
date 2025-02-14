package co.kr.tnt.domain.model

import co.kr.tnt.domain.model.trainer.TrainerManagementMemberCount
import java.time.LocalDate

sealed class User {
    abstract val id: String
    abstract val name: String
    abstract val image: String?

    // TODO [membersCount] 가 이 도메인 모델에 위치해 있는게 어색함.
    data class Trainer(
        override val id: String,
        override val name: String,
        override val image: String?,
        val memberCounts: TrainerManagementMemberCount,
    ) : User() {
        companion object {
            val EMPTY = Trainer(
                id = "",
                name = "",
                image = null,
                memberCounts = TrainerManagementMemberCount.ZERO,
            )
        }
    }

    // TODO 도메인 모델 개선
    data class Trainee(
        override val id: String,
        override val name: String,
        override val image: String?,
        val birthday: LocalDate?,
        val age: Int? = 0,
        val weight: Double?,
        val height: Int?,
        val ptPurpose: List<String>?,
        val caution: String?,
        val isConnected: Boolean,
    ) : User() {
        companion object {
            val EMPTY = Trainee(
                id = "",
                name = "",
                image = null,
                birthday = null,
                age = null,
                weight = null,
                height = null,
                ptPurpose = emptyList(),
                caution = null,
                isConnected = false,
            )
        }
    }
}
