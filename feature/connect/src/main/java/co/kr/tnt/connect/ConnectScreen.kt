package co.kr.tnt.connect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.tnt.domain.model.UserType

@Composable
internal fun ConnectRoute(
    isTrainer: Boolean,
    navigateToPrevious: () -> Unit,
    navigateToHome: (Boolean) -> Unit,
    @Suppress("UnusedParameter")
    viewModel: ConnectViewModel = hiltViewModel(),
) {
    var page by rememberSaveable {
        mutableStateOf(
            if (isTrainer) {
                ConnectPage.CodeGeneration
            } else {
                ConnectPage.CodeEntry
            },
        )
    }

    SignUpScreen(
        page = page,
        onNextClick = nextClick@{
            if (page == ConnectPage.TraineeConnectComplete || page == ConnectPage.TraineeProfile) {
                navigateToHome(isTrainer)
                return@nextClick
            }

            page = ConnectPage.getNextPage(page)
        },
        onBackClick = backClick@{
            if (page == ConnectPage.CodeGeneration || page == ConnectPage.CodeEntry) {
                navigateToPrevious()
                return@backClick
            }

            page = ConnectPage.getPreviousPage(page)
        },
        onSkipClick = skipClick@{
            navigateToHome(isTrainer)
        },
    )
}

@Composable
fun SignUpScreen(
    page: ConnectPage,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit,
) {
    when (page) {
        ConnectPage.CodeGeneration -> CodeGenerationScreen(
            onSkipClick = onSkipClick,
        )

        ConnectPage.TrainerConnectComplete -> ConnectCompleteScreen(
            userType = UserType.Trainer,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        ConnectPage.TraineeProfile -> TraineeProfileScreen(
            onNextClick = onNextClick,
        )

        ConnectPage.CodeEntry -> CodeEntryScreen(
            onNextClick = onNextClick,
            onSkipClick = onSkipClick,
        )

        ConnectPage.PTSessionForm -> PTSessionFormScreen(
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )

        ConnectPage.TraineeConnectComplete -> ConnectCompleteScreen(
            userType = UserType.Trainee,
            onNextClick = onNextClick,
            onBackClick = onBackClick,
        )
    }
}

enum class ConnectPage {
    CodeGeneration,

    TrainerConnectComplete,
    TraineeProfile,

    CodeEntry,
    PTSessionForm,
    TraineeConnectComplete,
    ;

    companion object {
        fun getPreviousPage(currentPage: ConnectPage): ConnectPage {
            return when (currentPage) {
                TraineeProfile -> TrainerConnectComplete

                PTSessionForm -> CodeEntry
                TraineeConnectComplete -> PTSessionForm
                else -> throw IllegalStateException("No previous page defined for $currentPage")
            }
        }

        fun getNextPage(currentPage: ConnectPage): ConnectPage {
            return when (currentPage) {
                TrainerConnectComplete -> TraineeProfile

                CodeEntry -> PTSessionForm
                PTSessionForm -> TraineeConnectComplete
                else -> throw IllegalStateException("No previous page defined for $currentPage")
            }
        }
    }
}
