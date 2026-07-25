package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContractType
import com.example.data.model.DigitDistribution
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualTraderScreen(
    selectedMarket: String,
    onMarketChange: (String) -> Unit,
    digitDistribution: DigitDistribution,
    onTradeClick: (ContractType, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var stakeText by remember { mutableStateOf("10.0") }
    val stakeVal = stakeText.toDoubleOrNull() ?: 10.0
    val estPayout = stakeVal * 1.95

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
                text = "INSTANT EXECUTION",
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = "Manual Trading Terminal",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        // Live Price Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Volatility 100 Index",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Price: ${String.format("%.2f", digitDistribution.currentPrice)}",
                        color = WinGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceVariant
                ) {
                    Text(
                        text = "LAST DIGIT: ${digitDistribution.currentDigit}",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Stake Input Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "TRADE STAKE ($)",
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )

                OutlinedTextField(
                    value = stakeText,
                    onValueChange = { stakeText = it },
                    modifier = Modifier.fillMaxWidth().testTag("stake_input"),
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextWhite, fontSize = 16.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Net Payout: $${String.format("%.2f", estPayout)}", color = TextGray, fontSize = 12.sp)
                    Text("Return: +95%", color = WinGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Instant Action Trade Buttons
        Text(
            text = "EVEN / ODD CONTRACTS",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onTradeClick(ContractType.EVEN, stakeVal) },
                colors = ButtonDefaults.buttonColors(containerColor = WinGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("buy_even_button")
            ) {
                Text("PURCHASE EVEN", fontWeight = FontWeight.Bold, color = DarkBackground, fontSize = 14.sp)
            }

            Button(
                onClick = { onTradeClick(ContractType.ODD, stakeVal) },
                colors = ButtonDefaults.buttonColors(containerColor = LossRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("buy_odd_button")
            ) {
                Text("PURCHASE ODD", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "RISE / FALL CONTRACTS",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onTradeClick(ContractType.RISE, stakeVal) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("buy_rise_button")
            ) {
                Text("PURCHASE RISE 📈", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
            }

            Button(
                onClick = { onTradeClick(ContractType.FALL, stakeVal) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("buy_fall_button")
            ) {
                Text("PURCHASE FALL 📉", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
            }
        }
    }
}
