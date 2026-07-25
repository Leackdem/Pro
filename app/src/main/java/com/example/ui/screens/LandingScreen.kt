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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onExploreDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A0303), // Dark fiery red background
                        Color(0xFF0F0404),
                        DarkBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pro Trader",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = GoldPrimary
                    )
                    Text(
                        text = "powered by deriv",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Log In", color = TextWhite, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onRegisterClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Get Started", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hero Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF3D2100),
                modifier = Modifier.border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = "FREE DERIV BOTS, AUTOMATION, AND TRADING TOOLS IN ONE WORKSPACE",
                    color = GoldPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Hero Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome to",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TextWhite
                )
                Text(
                    text = "Pro Trader",
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    color = GoldPrimary
                )
            }

            Text(
                text = "Trading tools for focused execution. Build, load, and run Deriv bot strategies from a focused workspace made for everyday traders.",
                color = TextGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // CTA Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_and_trade_button")
                ) {
                    Text(
                        text = "⚡ Log In and Trade (Deriv OAuth)",
                        color = DarkBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = onExploreDemo,
                    colors = ButtonDefaults.buttonColors(containerColor = TextWhite),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("create_free_account_button")
                ) {
                    Text(
                        text = "⚡ Create Free Account / Try Demo",
                        color = DarkBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Testimonials Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(WinGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AH", fontWeight = FontWeight.Bold, color = DarkBackground)
                    }

                    Column {
                        Text(
                            text = "Amina Hassan",
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Synthetic Indices Trader - Tanzania",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "\"The Smart AI recovery bot is absolutely insane. It pulled me out of drawdown instantly!\"",
                            color = TextGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Stats Pills Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("10", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextWhite)
                        Text("DIGIT OUTCOMES", fontSize = 9.sp, color = TextMuted)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("24/7", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextWhite)
                        Text("SYNTHETIC ACCESS", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Live", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = WinGreen)
                        Text("ANALYSIS WORKSPACE", fontSize = 9.sp, color = TextMuted)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Live", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = WinGreen)
                        Text("MARKET STATUS", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }

            // Risk Disclaimer Footer
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1700)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Risk Warning",
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "\"Risk Disclaimer. Deriv offers complex derivatives, such as options and contracts for difference (CFDs). These products may not be suitable for all clients... You should never trade with money that you cannot afford to lose.\"",
                        fontSize = 11.sp,
                        color = GoldPrimary
                    )
                }
            }
        }
    }
}
