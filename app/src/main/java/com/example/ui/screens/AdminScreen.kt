package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.UserSettingsEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    userSettings: UserSettingsEntity?,
    onSaveSettings: (appId: String, token: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var appIdText by remember(userSettings) { mutableStateOf(userSettings?.appId ?: "1089") }
    var tokenText by remember(userSettings) { mutableStateOf(userSettings?.derivOAuthToken ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "PLATFORM SETTINGS",
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = "Admin & Deriv Integration Panel",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Deriv WebSocket & OAuth Credentials",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                OutlinedTextField(
                    value = appIdText,
                    onValueChange = { appIdText = it },
                    label = { Text("Deriv App ID") },
                    modifier = Modifier.fillMaxWidth().testTag("app_id_input"),
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextWhite, fontSize = 14.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                OutlinedTextField(
                    value = tokenText,
                    onValueChange = { tokenText = it },
                    label = { Text("Deriv OAuth API Token") },
                    modifier = Modifier.fillMaxWidth().testTag("token_input"),
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextWhite, fontSize = 14.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Button(
                    onClick = { onSaveSettings(appIdText, tokenText) },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_admin_settings_button")
                ) {
                    Text("Save & Connect WebSocket", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
