package com.inspi.app.ui.nickname

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inspi.app.R
import com.inspi.app.ui.theme.*

@Composable
fun NicknameScreen(
    onContinue: () -> Unit,
    viewModel: NicknameViewModel = hiltViewModel(),
) {
    val nickname by viewModel.nickname.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = InspyBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(80.dp))

            Image(
                painter = painterResource(R.drawable.mascot_inspi),
                contentDescription = null,
                modifier = Modifier.size(140.dp),
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "What's your name?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = InspyOnBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Pick a nickname — this is how\nInspi and your friends will know you.",
                fontSize = 16.sp,
                color = InspyOnBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )

            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = nickname,
                onValueChange = viewModel::onNicknameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text("e.g. PixelFox", color = InspyOnBackground.copy(alpha = 0.4f))
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = InspyPrimary,
                    unfocusedBorderColor = InspyOnBackground.copy(alpha = 0.2f),
                    focusedContainerColor = InspySurface,
                    unfocusedContainerColor = InspySurface,
                    cursorColor = InspyPrimary,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        viewModel.confirm(onContinue)
                    }
                ),
                supportingText = {
                    Text(
                        text = "${nickname.length}/20",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = InspyOnBackground.copy(alpha = 0.4f),
                        fontSize = 12.sp,
                    )
                }
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    keyboard?.hide()
                    viewModel.confirm(onContinue)
                },
                enabled = nickname.trim().isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InspyPrimary),
            ) {
                Text(
                    "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}