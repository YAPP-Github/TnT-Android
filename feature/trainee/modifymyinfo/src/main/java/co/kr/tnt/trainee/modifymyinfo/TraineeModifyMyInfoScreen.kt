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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import co.kr.tnt.core.ui.R.string.core_complete
import co.kr.tnt.core.ui.R.string.core_confirm_modify_info_exit
import co.kr.tnt.core.ui.R.string.core_entered_wrong_text
import co.kr.tnt.core.ui.R.string.core_height_label
import co.kr.tnt.core.ui.R.string.core_height_unit
import co.kr.tnt.core.ui.R.string.core_name
import co.kr.tnt.core.ui.R.string.core_name_placeholder
import co.kr.tnt.core.ui.R.string.core_text_length_and_format_warning
import co.kr.tnt.core.ui.R.string.core_unsaved_changes_warning
import co.kr.tnt.core.ui.R.string.core_weight_label
import co.kr.tnt.core.ui.R.string.core_weight_unit
import co.kr.tnt.designsystem.component.TnTIconPopupDialog
import co.kr.tnt.designsystem.component.TnTLabeledTextField
import co.kr.tnt.designsystem.component.TnTLabeledTextFieldWithCounter
import co.kr.tnt.designsystem.component.TnTModalBottomSheet
import co.kr.tnt.designsystem.component.TnTOutlinedTextField
import co.kr.tnt.designsystem.component.TnTProfileImage
import co.kr.tnt.designsystem.component.TnTTopBarWithBackButton
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.snackbar.LocalSnackbar
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.feature.trainee.modifymyinfo.R
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoEffect
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiEvent
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState
import co.kr.tnt.trainee.modifymyinfo.TraineeModifyMyInfoContract.TraineeModifyMyInfoUiState.DialogState
import co.kr.tnt.trainee.modifymyinfo.model.TraineePtPurpose
import co.kr.tnt.ui.extensions.clearFocusOnTap
import co.kr.tnt.ui.model.DefaultUserProfile
import co.kr.tnt.ui.utils.convertToAllowedImageFormat
import co.kr.tnt.ui.utils.throttled
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val MAX_NAME_LENGTH = 15
private const val MAX_CAUTION_LENGTH = 100
private const val ROW_NUM = 3
private const val COLUMNS_NUM = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TraineeModifyMyInfoRoute(
    viewModel: TraineeModifyMyInfoViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    TraineeModifyMyInfoScreen(
        state = state,
        onClickEditImage = { showBottomSheet = true },
        onChangeName = { name -> viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnChangeName(name)) },
        onChangeBirthday = { birthday ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnChangeBirthday(birthday))
        },
        onChangeHeight = { height ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnChangeHeight(height))
        },
        onChangeWeight = { weight ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnChangeWeight(weight))
        },
        onChangeCaution = { caution ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnChangeCaution(caution))
        },
        onSelectPurpose = { purpose ->
            viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnSelectPurpose(purpose))
        },
        onClickBack = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnClickBack) },
        onClickComplete = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnClickComplete) },
    )

    if (showBottomSheet) {
        TnTModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            content = {
                EditImageBottomSheetContent(
                    onClickDelete = {
                        viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnDeleteProfileImage)
                        showBottomSheet = false
                    },
                    onClickAlbum = { uri ->
                        val profileImageFile = uri.convertToAllowedImageFormat(context)
                        viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnProfileImageSelect(profileImageFile))
                        showBottomSheet = false
                    },
                )
            },
        )
    }

    when (state.dialogState) {
        DialogState.NONE -> Unit
        DialogState.CONFIRM_EXIT -> {
            TnTIconPopupDialog(
                title = stringResource(core_confirm_modify_info_exit),
                content = stringResource(core_unsaved_changes_warning),
                leftButtonText = stringResource(R.string.end),
                rightButtonText = stringResource(R.string.keep_edit),
                onLeftButtonClick = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnClickDialogConfirm) },
                onRightButtonClick = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnDismissDialog) },
                onDismiss = { viewModel.setEvent(TraineeModifyMyInfoUiEvent.OnDismissDialog) },
            )
        }
    }

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
    onClickEditImage: () -> Unit,
    onChangeName: (name: String) -> Unit,
    onChangeBirthday: (birthday: LocalDate) -> Unit,
    onChangeHeight: (height: String) -> Unit,
    onChangeWeight: (weight: String) -> Unit,
    onSelectPurpose: (purpose: String) -> Unit,
    onChangeCaution: (caution: String) -> Unit,
    onClickBack: () -> Unit,
    onClickComplete: () -> Unit,
) {
    BackHandler { onClickBack() }

    val context = LocalContext.current
    val today = LocalDate.now()

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
                onBackClick = onClickBack,
            )
        },
        bottomBar = {
            TnTBottomButton(
                text = stringResource(core_complete),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                enabled = state.isEnableComplete,
                onClick = throttled { onClickComplete() },
            )
        },
        containerColor = TnTTheme.colors.commonColors.Common0,
        modifier = Modifier.clearFocusOnTap(),
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(padding)
                    .imePadding()
                    .background(TnTTheme.colors.commonColors.Common0)
                    .verticalScroll(rememberScrollState()),
            ) {
                TnTProfileImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    defaultImage = painterResource(DefaultUserProfile.Trainee.image),
                    image = painter,
                    onEditClick = { onClickEditImage() },
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
                            onChangeName(newValue)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                        placeholder = stringResource(core_name_placeholder),
                        maxLength = MAX_NAME_LENGTH,
                        isSingleLine = true,
                        showWarning = state.isNameValid.not(),
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
                            onDateSelected = onChangeBirthday,
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
                                onValueChange = onChangeHeight,
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
                                onValueChange = onChangeWeight,
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
                                        onClick = { onSelectPurpose(purposeText) },
                                        modifier = Modifier.weight(1f),
                                    )
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = stringResource(R.string.edit_caution_that_trainer_must_know),
                            modifier = Modifier.fillMaxWidth(),
                            style = TnTTheme.typography.body1Bold,
                            color = TnTTheme.colors.neutralColors.Neutral900,
                        )
                        Spacer(Modifier.padding(top = 8.dp))
                        TnTOutlinedTextField(
                            value = state.caution ?: "",
                            onValueChange = { newValue ->
                                onChangeCaution(newValue)
                            },
                            isError = (state.caution?.length ?: 0) >= MAX_CAUTION_LENGTH,
                            warningMessage = stringResource(R.string.caution_length_overflow),
                            maxLength = 100,
                        )
                    }
                }
                Spacer(Modifier.padding(top = 32.dp))
            }
        }
    }
}

@Composable
private fun EditImageBottomSheetContent(
    onClickDelete: () -> Unit,
    onClickAlbum: (uri: Uri) -> Unit,
) {
    val pickMediaLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let(onClickAlbum)
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.delete_image),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable(onClick = onClickDelete),
            style = TnTTheme.typography.h4,
            color = TnTTheme.colors.neutralColors.Neutral600,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.select_image_from_album),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable(
                    onClick = {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = PickVisualMedia.ImageOnly,
                            ),
                        )
                    },
                ),
            style = TnTTheme.typography.h4,
            color = TnTTheme.colors.neutralColors.Neutral600,
        )
        Spacer(Modifier.height(54.dp))
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
            text = selectedDate?.format(dateFormatter)
                ?: stringResource(R.string.birthday_placeholder),
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

@Preview(heightDp = 1000)
@Composable
private fun TraineeModifyMyScreenPreview() {
    TnTTheme {
        TraineeModifyMyInfoScreen(
            state = TraineeModifyMyInfoUiState(name = "김회원"),
            onClickEditImage = { },
            onChangeName = { },
            onChangeBirthday = { },
            onChangeHeight = { },
            onChangeWeight = { },
            onSelectPurpose = { },
            onChangeCaution = { },
            onClickBack = { },
            onClickComplete = { },
        )
    }
}

@Preview
@Composable
private fun ModifyMyInfoBottomSheetContentPreview() {
    TnTTheme {
        EditImageBottomSheetContent(
            onClickDelete = { },
            onClickAlbum = { },
        )
    }
}
