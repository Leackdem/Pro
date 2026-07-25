package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.Account
import com.example.ui.theme.*

@Composable
fun AccountSwitcherDialog(
    activeAccount: Account,
    demoAccount: Account,
    onSwitchAccount: (isDemo: Boolean) -> Unit,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTabIsDemo by remember { mutableStateOf(activeAccount.isDemo) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("account_switcher_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Real vs Demo Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    // Real Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                selectedTabIsDemo = false
                                onSwitchAccount(false)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Real",
                                fontWeight = if (!selectedTabIsDemo) FontWeight.Bold else FontWeight.Medium,
                                color = if (!selectedTabIsDemo) Color.Black else Color.Gray,
                                fontSize = 15.sp
                            )
                            if (!selectedTabIsDemo) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .background(RedAccent)
                                )
                            }
                        }
                    }

                    // Demo Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                selectedTabIsDemo = true
                                onSwitchAccount(true)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Demo",
                                fontWeight = if (selectedTabIsDemo) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTabIsDemo) Color.Black else Color.Gray,
                                fontSize = 15.sp
                            )
                            if (selectedTabIsDemo) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .background(InfoBlue)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Section Title: Deriv accounts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Deriv accounts",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Account Row Card
                val currentDisplayAcc = if (selectedTabIsDemo) demoAccount else activeAccount
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEBF1F5))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (selectedTabIsDemo) InfoBlue else Color(0xFF1976D2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedTabIsDemo) "D" else "🇺🇸",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = currentDisplayAcc.currency,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                            Text(
                                text = currentDisplayAcc.id,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = "${String.format("%.2f", currentDisplayAcc.balance)} ${currentDisplayAcc.currency}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CFD accounts link
                Text(
                    text = "Looking for CFD accounts? Go to Trader's Hub",
                    color = RedAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Logout button
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onLogout() }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
