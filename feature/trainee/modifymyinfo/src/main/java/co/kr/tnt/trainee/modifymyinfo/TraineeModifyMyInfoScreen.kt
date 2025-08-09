package co.kr.tnt.trainee.modifymyinfo

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.kr.tnt.core.ui.R.string.core_entered_wrong_text
import co.kr.tnt.core.ui.R.string.core_height_label
import co.kr.tnt.core.ui.R.string.core_height_unit
import co.kr.tnt.core.ui.R.string.core_name
import co.kr.tnt.core.ui.R.string.core_name_placeholder
import co.kr.tnt.core.ui.R.string.core_text_length_and_format_warning
import co.kr.tnt.core.ui.R.string.core_weight_label
import co.kr.tnt.core.ui.R.string.core_weight_unit
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.modifymyinfo.R
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoEffect
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiEvent
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState
import co.kr.tnt.trainee.modifymyinfo.model.TraineePtPurpose
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.utils.convertToAllowedImageFormat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val MAX_NAME_LENGTH = 15
private const val MAX_CAUTION_LENGTH = 100
private const val ROW_NUM = 3
private const val COLUMNS_NUM = 2

@Composable
internal fun TraineeModifyMyInfoRoute(
    viewModel: TraineeModifyMyInfoViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    TraineeModifyMyInfoScreen(
        state = state,
        onProfileImageSelect = { uri ->
            val profileImageFile = uri.convertToAllowedImageFormat(context)
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnProfileImageSelect(profileImageFile))
        },
        onNameChange = { name -> viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnNameChange(name)) },
        onBirthdayChange = { birthday ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnBirthdayChange(birthday))
        },
        onHeightChange = { height ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnHeightChange(height))
        },
        onWeightChange = { weight ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnWeightChange(weight))
        },
        onCautionChange = { caution ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnCautionChange(caution))
        },
        onPurposeSelected = { purpose ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnPurposeSelected(purpose))
        },
        onBackClick = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnBackClick) },
        onNextClick = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnNextClick) },
    )

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TraineeModifyMyInfoEffect.NavigateToBack -> navigateToPrevious()
                is TraineeModifyMyInfoEffect.ShowToast -> snackbar.show(effect.message.asString(context))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TraineeModifyMyInfoScreen(
    state: TraineeModifyMyInfoUiState,
    onProfileImageSelect: (uri: Uri) -> Unit,
    onNameChange: (name: String) -> Unit,
    onBirthdayChange: (birthday: LocalDate) -> Unit,
    onHeightChange: (height: String) -> Unit,
    onWeightChange: (weight: String) -> Unit,
    onPurposeSelected: (purpose: String) -> Unit,
    onCautionChange: (caution: String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    val today = LocalDate.now()

    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let(onProfileImageSelect)
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(state.profileImage)
            .placeholder(DefaultUserProfile.Trainee.image)
            .error(DefaultUserProfile.Trainee.image)
            .build(),
    )

    Scaffold(
        topBar = {
            TnTTopBarWithBackButton(
                title = stringResource(R.string.modifying_my_info),
                onBackClick = onBackClick,
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(TnTTheme.colors.commonColors.Common0)
                .verticalScroll(rememberScrollState()),
        ) {
            TnTProfileImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                defaultImage = painterResource(DefaultUserProfile.Trainee.image),
                image = painter,
                onEditClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )
            Spacer(Modifier.padding(top = 32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(48.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                TnTLabeledTextFieldWithCounter(
                    title = stringResource(core_name),
                    value = state.name,
                    onValueChange = { newValue ->
                        onNameChange(newValue)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    placeholder = stringResource(core_name_placeholder),
                    maxLength = MAX_NAME_LENGTH,
                    isSingleLine = true,
                    showWarning = !state.isNameValid,
                    isRequired = true,
                    warningMessage = stringResource(
                        core_text_length_and_format_warning,
                        MAX_NAME_LENGTH,
                    ),
                )
                Column {
                    Text(
                        text = stringResource(R.string.birthday_label),
                        color = TnTTheme.colors.neutralColors.Neutral900,
                        style = TnTTheme.typography.body1Bold,
                        modifier = Modifier.padding(start = 20.dp, bottom = 8.dp),
                    )
                    BirthdayPicker(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        context = context,
                        today = today,
                        selectedDate = state.birthday,
                        onDateSelected = onBirthdayChange,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = TnTTheme.colors.neutralColors.Neutral200,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                    ) {
                        TnTLabeledTextField(
                            title = stringResource(core_height_label),
                            value = state.height ?: "",
                            placeholder = "0",
                            isSingleLine = true,
                            showWarning = state.isHeightValid.not(),
                            warningMessage = stringResource(core_entered_wrong_text),
                            keyboardType = KeyboardType.Number,
                            trailingComponent = {
                                UnitLabel(core_height_unit)
                            },
                            onValueChange = onHeightChange,
                            modifier = Modifier.weight(1f),
                        )
                        TnTLabeledTextField(
                            title = stringResource(core_weight_label),
                            value = state.weight ?: "",
                            placeholder = "00.0",
                            isSingleLine = true,
                            showWarning = state.isWeightValid.not(),
                            warningMessage = stringResource(core_entered_wrong_text),
                            keyboardType = KeyboardType.Number,
                            trailingComponent = {
                                UnitLabel(core_weight_unit)
                            },
                            onValueChange = onWeightChange,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "PT 목적",
                        style = TnTTheme.typography.body1Bold,
                        color = TnTTheme.colors.neutralColors.Neutral900,
                    )
                    Spacer(Modifier.padding(top = 12.dp))
                    Column {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            maxItemsInEachRow = COLUMNS_NUM,
                            maxLines = ROW_NUM,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            TraineePtPurpose.entries.forEach { purpose ->
                                val purposeText = stringResource(purpose.textResId)
                                PurposeButton(
                                    text = purposeText,
                                    isSelected = state.ptPurpose?.contains(purposeText) == true,
                                    onClick = { onPurposeSelected(purposeText) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
                Column {
                    Text(
                        text = stringResource(R.string.caution_that_trainer_must_know),
                        style = TnTTheme.typography.body1Bold,
                        color = TnTTheme.colors.neutralColors.Neutral900,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    TnTOutlinedTextField(
                        value = state.caution ?: "",
                        onValueChange = { newValue ->
                            onCautionChange(newValue)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                        isError = (state.caution?.length ?: 0) >= MAX_CAUTION_LENGTH,
                        warningMessage = stringResource(R.string.caution_length_overflow),
                        maxLength = 100,
                    )
                }
            }
        }
    }
}

@Composable
private fun BirthdayPicker(
    modifier: Modifier = Modifier,
    context: Context,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = selectedDate ?: LocalDate.of(2001, 1, 1)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val newDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                        onDateSelected(newDate)
                    },
                    date.year,
                    date.monthValue - 1,
                    date.dayOfMonth,
                )
                    .apply {
                        // 오늘 이후는 선택 불가능
                        val todayMillis = today
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()

                        datePicker.maxDate = todayMillis - 1
                    }
                    .show()
            },
    ) {
        Text(
            text = selectedDate?.format(dateFormatter) ?: stringResource(R.string.birthday_placeholder),
            color = if (selectedDate == null) {
                TnTTheme.colors.neutralColors.Neutral400
            } else {
                TnTTheme.colors.neutralColors.Neutral600
            },
            style = TnTTheme.typography.body1Medium,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
private fun UnitLabel(stringResId: Int) {
    Text(
        text = stringResource(stringResId),
        style = TnTTheme.typography.body1Medium,
        color = TnTTheme.colors.neutralColors.Neutral400,
    )
}

@Composable
fun PurposeButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TnTTextButton(
        text = text,
        modifier = modifier,
        size = ButtonSize.XLarge,
        type = if (isSelected) ButtonType.RedOutline else ButtonType.GrayOutline,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun TraineeModifyMyScreenPreview() {
    TnTTheme {
        TraineeModifyMyInfoScreen(
            state = TraineeModifyMyInfoUiState(name = "김회원"),
            onProfileImageSelect = { },
            onNameChange = { },
            onBirthdayChange = { },
            onHeightChange = { },
            onWeightChange = { },
            onPurposeSelected = { },
            onCautionChange = { },
            onBackClick = { },
            onNextClick = { },
        )
    }
}
