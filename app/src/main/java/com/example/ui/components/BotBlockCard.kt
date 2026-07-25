package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotBlockCard(
    targetLoss: String,
    onTargetLossChange: (String) -> Unit,
    targetProfit: String,
    onTargetProfitChange: (String) -> Unit,
    stake: String,
    onStakeChange: (String) -> Unit,
    lossMultiple: String,
    onLossMultipleChange: (String) -> Unit,
    selectedMarket: String,
    onMarketChange: (String) -> Unit,
    isRunning: Boolean,
    onToggleRun: () -> Unit,
    isFastSpeed: Boolean,
    onToggleSpeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
            ) {
                Text(
                    text = "Blocks Menu ▼",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF1D4ED8),
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = "Quick strategy",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Divider(color = Color(0xFFE2E8F0))

        // Block Editor Scrollable Canvas
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Block 1: Trade parameters
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Header Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1D4ED8))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "📜 1. Trade parameters",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Market Row
                        Text(
                            text = "Market: synthetic_index > Volatility 100 Index",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )

                        // Trade Type Row
                        Text(
                            text = "Trade Type: Digits > Even/Odd (Odd)",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )

                        // Run once at start parameters
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Run once at start:",
                                    color = Color(0xFF1D4ED8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )

                                OutlinedTextField(
                                    value = targetLoss,
                                    onValueChange = onTargetLossChange,
                                    label = { Text("TARGET LOSS ($)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                )

                                OutlinedTextField(
                                    value = targetProfit,
                                    onValueChange = onTargetProfitChange,
                                    label = { Text("TARGET PROFIT ($)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                )

                                OutlinedTextField(
                                    value = stake,
                                    onValueChange = onStakeChange,
                                    label = { Text("YOUR STAKE ($)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                )

                                OutlinedTextField(
                                    value = lossMultiple,
                                    onValueChange = onLossMultipleChange,
                                    label = { Text("MARTINGALE LOSS MULTIPLE") },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                                )
                            }
                        }

                        // Trade options
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Duration: 1 Tick | Stake: USD $stake",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A8A),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Block 2: Purchase Conditions
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2563EB))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "🛍️ 2. Purchase conditions (Buy Odd / Even)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Block 3: Restart Trading Conditions
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1D4ED8))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "🏁 4. Restart trading conditions (Martingale Rule)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "• IF Contract WON: Notify 'green', Reset stake to YOUR STAKE.",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "• IF Contract LOST: Multiply stake by $lossMultiple, Trade again.",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // Bottom Run Control Bar (Matching Screenshot 5 & 6)
        Surface(
            color = Color(0xFFEFF6FF),
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Run/Stop Button
                Button(
                    onClick = onToggleRun,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) LossRed else Color(0xFF1D4ED8)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("run_bot_button")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "Stop" else "▶ Run",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                // Speed Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Execution",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = if (isFastSpeed) "FAST" else "NORMAL",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 12.sp
                        )
                    }

                    Switch(
                        checked = isFastSpeed,
                        onCheckedChange = { onToggleSpeed() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = WinGreen
                        )
                    )
                }
            }
        }
    }
}
