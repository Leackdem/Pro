package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Account
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    account: Account,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
                text = "TRADER IDENTIFICATION",
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = "User Profile & Account",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8D1515)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("TH", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }

                Text(
                    text = account.name,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "Deriv CR ID: ${account.id}",
                    color = TextGray,
                    fontSize = 13.sp
                )

                Text(
                    text = "Current Balance: ${String.format("%.2f", account.balance)} ${account.currency}",
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = LossRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout Deriv Account", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
