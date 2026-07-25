package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DigitDistribution
import com.example.ui.theme.*

@Composable
fun DigitAnalysisModal(
    digitDistribution: DigitDistribution,
    selectedMarketName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF131A2A),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("digit_analysis_modal")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LIVE ANALYSIS",
                            color = InfoBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Digit Distribution",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkSurfaceVariant,
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = "TICKS 1000",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selected Market Card & Current Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "SELECTED MARKET",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedMarketName,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D4A)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CURRENT PRICE",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format("%.2f", digitDistribution.currentPrice),
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1A3B8B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = digitDistribution.currentDigit.toString(),
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Digits Grid (0 to 9)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (0..4).forEach { digit ->
                        DigitStatPill(
                            digit = digit,
                            pct = digitDistribution.percentages[digit] ?: 10.0,
                            isCurrent = digit == digitDistribution.currentDigit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (5..9).forEach { digit ->
                        DigitStatPill(
                            digit = digit,
                            pct = digitDistribution.percentages[digit] ?: 10.0,
                            isCurrent = digit == digitDistribution.currentDigit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recent Ticks Tape
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(digitDistribution.recentDigits) { d ->
                        val isEven = d % 2 == 0
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isEven) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = d.toString(),
                                color = if (isEven) WinGreen else LossRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // EVEN vs ODD Bar
                RatioProgressBar(
                    labelLeft = "EVEN ${digitDistribution.evenPercent}%",
                    labelRight = "ODD ${digitDistribution.oddPercent}%",
                    leftRatio = (digitDistribution.evenPercent / 100.0).toFloat()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // RISE vs FALL Bar
                RatioProgressBar(
                    labelLeft = "RISE ${digitDistribution.risePercent}%",
                    labelRight = "FALL ${digitDistribution.fallPercent}%",
                    leftRatio = (digitDistribution.risePercent / 100.0).toFloat()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // OVER 4 vs UNDER 5 Bar
                RatioProgressBar(
                    labelLeft = "OVER 4 ${digitDistribution.over4Percent}%",
                    labelRight = "UNDER 5 ${digitDistribution.under5Percent}%",
                    leftRatio = (digitDistribution.over4Percent / 100.0).toFloat()
                )
            }
        }
    }
}

@Composable
private fun DigitStatPill(
    digit: Int,
    pct: Double,
    isCurrent: Boolean
) {
    val bg = if (isCurrent) Color(0xFF1E3A8A) else DarkSurfaceVariant
    val borderColor = if (isCurrent) InfoBlue else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(58.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = digit.toString(),
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            text = "${String.format("%.1f", pct)}%",
            color = TextGray,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RatioProgressBar(
    labelLeft: String,
    labelRight: String,
    leftRatio: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = labelLeft,
                color = WinGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = labelRight,
                color = LossRed,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(LossRed)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(leftRatio.coerceIn(0.05f, 0.95f))
                    .background(WinGreen)
            )
        }
    }
}
