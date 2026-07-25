package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketSummary
import com.example.data.viewmodel.AppTab
import com.example.ui.components.TickerMarquee
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    markets: List<MarketSummary>,
    onNavigateTab: (AppTab) -> Unit,
    onOpenDigitAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Live Markets Marquee Bar
            TickerMarquee(markets = markets)

            Spacer(modifier = Modifier.height(16.dp))

            // Workspace Hero Chart / Grid Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick Shortcuts Grid (4 Cards as in Screenshot 2 & 4)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // LOAD BOT
                    DashboardShortcutCard(
                        title = "LOAD BOT",
                        icon = Icons.Default.Folder,
                        iconColor = Color(0xFF29B6F6),
                        onClick = { onNavigateTab(AppTab.BOT_BUILDER) },
                        modifier = Modifier.weight(1f)
                    )

                    // PREMIUM BOTS
                    DashboardShortcutCard(
                        title = "PREMIUM BOTS",
                        icon = Icons.Default.SmartToy,
                        iconColor = GoldPrimary,
                        onClick = { onNavigateTab(AppTab.PREMIUM_BOTS) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // SPEED BOT
                    DashboardShortcutCard(
                        title = "SPEED BOT",
                        icon = Icons.Default.RocketLaunch,
                        iconColor = GoldPrimary,
                        onClick = { onNavigateTab(AppTab.SPEEDBOT) },
                        modifier = Modifier.weight(1f)
                    )

                    // MANUAL TRADING
                    DashboardShortcutCard(
                        title = "MANUAL TRADING",
                        icon = Icons.Default.TouchApp,
                        iconColor = PurpleAccent,
                        onClick = { onNavigateTab(AppTab.MANUAL_TRADER) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Chart Preview Canvas Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShowChart,
                                    contentDescription = null,
                                    tint = GoldPrimary
                                )
                                Text(
                                    text = "LIVE SYNTHETIC MARKET STREAM",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = onOpenDigitAnalysis,
                                colors = ButtonDefaults.buttonColors(containerColor = RedAccent),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "📊 Live Analysis",
                                    color = TextWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Simulated Live Candlestick Canvas Visual
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF090D15))
                                .border(1.dp, DarkBorder, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Volatility 100 Index: 541.78",
                                    color = WinGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Streaming 1-tick real time WebSocket quotes",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Glowing AI Button (Bottom Right matching Screenshots)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(54.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(PurpleAccent, Color(0xFF4A148C))
                    )
                )
                .border(2.dp, GoldPrimary, CircleShape)
                .clickable { onNavigateTab(AppTab.SIGNAL_AI) }
                .testTag("floating_ai_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                color = TextWhite,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        }

        // Yellow Warning Icon (Bottom Left matching Screenshots)
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = GoldPrimary,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(28.dp)
                .clickable { onOpenDigitAnalysis() }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "⚠️", fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun DashboardShortcutCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(100.dp)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}
