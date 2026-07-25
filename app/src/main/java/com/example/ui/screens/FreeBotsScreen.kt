package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SavedBot
import com.example.ui.theme.*

@Composable
fun FreeBotsScreen(
    onLoadBot: (SavedBot) -> Unit,
    modifier: Modifier = Modifier
) {
    val curatedBots = listOf(
        SavedBot(1, "Synthetic Alpha Hunter", "High frequency Even/Odd digit strategy with auto Martingale recovery.", "FREE", "R_100", "Even/Odd", 150.0, 500.0, 10.0, 2.0, 89),
        SavedBot(2, "Speed Scalper 1s", "Sub-second Volatility 10 (1s) tick scalper designed for rapid $1 profit compounding.", "FREE", "1HZ10V", "Even/Odd", 100.0, 300.0, 5.0, 2.0, 92),
        SavedBot(3, "Spike Catcher Boom 1000", "Monitors Boom 1000 ticks for sudden spikes and executes instant RISE contracts.", "FREE", "BOOM1000", "Rise/Fall", 300.0, 600.0, 20.0, 1.8, 87),
        SavedBot(4, "Crash 500 Drop Hunter", "Rides Crash 500 downward momentum spikes safely.", "FREE", "CRASH500", "Rise/Fall", 250.0, 500.0, 15.0, 1.8, 85),
        SavedBot(5, "Digit Differs Smart AI", "Avoids last hot digit with 90% probability safety shield.", "PREMIUM", "R_100", "Matches/Differs", 500.0, 1000.0, 50.0, 3.0, 94)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "CURATED BOTS WORKSPACE",
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = "Free & Premium Bot Library",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(
                text = "Select any battle-tested strategy to immediately load it into your Bot Builder workspace.",
                color = TextGray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(curatedBots) { bot ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (bot.category == "PREMIUM") GoldPrimary else InfoBlue
                                ) {
                                    Text(
                                        text = bot.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkBackground,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = bot.name,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 15.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${bot.winRate}% WIN",
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = bot.description,
                            color = TextGray,
                            fontSize = 12.sp
                        )

                        HorizontalDivider(color = DarkBorder, thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Market: ${bot.marketSymbol} | Stake: $${bot.stake}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )

                            Button(
                                onClick = { onLoadBot(bot) },
                                colors = ButtonDefaults.buttonColors(containerColor = RedAccent),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Load Bot",
                                    tint = TextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LOAD BOT",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
