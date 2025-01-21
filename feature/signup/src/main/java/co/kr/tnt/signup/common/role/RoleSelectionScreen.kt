package co.kr.tnt.signup.common.role

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.kr.tnt.core.designsystem.R
import co.kr.tnt.designsystem.component.button.TnTBottomButton
import co.kr.tnt.designsystem.component.button.TnTTextButton
import co.kr.tnt.designsystem.component.button.model.ButtonSize
import co.kr.tnt.designsystem.component.button.model.ButtonType
import co.kr.tnt.designsystem.theme.TnTTheme
import co.kr.tnt.domain.model.UserType
import co.kr.tnt.signup.common.role.model.RoleState

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
) {
    var selectedRole by remember { mutableStateOf(UserType.Trainer) }
    val roleState = RoleState.fromDomain(selectedRole)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TnTTheme.colors.commonColors.Common0),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, top = 60.dp)) {
            Text(
                text = stringResource(R.string.signup_select_role_title),
                color = TnTTheme.colors.neutralColors.Neutral950,
                style = TnTTheme.typography.h2,
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(
                text = stringResource(R.string.signup_select_role_subtitle),
                color = TnTTheme.colors.neutralColors.Neutral500,
                style = TnTTheme.typography.body1Medium,
            )
        }
        Image(
            painter = painterResource(roleState.imageResId),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        // TODO 선택한 버튼 정보 저장
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 20.dp),
        ) {
            TnTTextButton(
                text = stringResource(RoleState.Trainer.textResId),
                modifier = Modifier.weight(1f),
                size = ButtonSize.Large,
                type = if (selectedRole == UserType.Trainer) ButtonType.RedOutline else ButtonType.GrayOutline,
                onClick = { selectedRole = UserType.Trainer },
            )
            TnTTextButton(
                text = stringResource(RoleState.Trainee.textResId),
                modifier = Modifier.weight(1f),
                size = ButtonSize.Large,
                type = if (selectedRole == UserType.Trainee) ButtonType.RedOutline else ButtonType.GrayOutline,
                onClick = { selectedRole = UserType.Trainee },
            )
        }
        // TODO 클릭 시 이름 입력 화면으로 이동
        TnTBottomButton(
            text = stringResource(R.string.next),
            enabled = true,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoleScreenPreview() {
    TnTTheme {
        RoleSelectionScreen(modifier = Modifier.fillMaxSize())
    }
}
