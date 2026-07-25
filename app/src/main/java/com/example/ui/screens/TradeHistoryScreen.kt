package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradeContract
import com.example.ui.theme.*

@Composable
fun TradeHistoryScreen(
    trades: List<TradeContract>,
    modifier: Modifier = Modifier
) {
    val totalProfit = trades.sumOf { it.profit }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "AUDIT & PERFORMANCE",
                color = GoldPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = "Trade History & Log",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        // Summary Card
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
                    Text("Total Executed Contracts", color = TextMuted, fontSize = 11.sp)
                    Text("${trades.size}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Net Accumulated P&L", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = "${if (totalProfit >= 0) "+" else ""}$${String.format("%.2f", totalProfit)}",
                        color = if (totalProfit >= 0) WinGreen else LossRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(trades) { trade ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${trade.symbolName} (${trade.contractType.name})",
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Contract: ${trade.contractId} | Stake: $${String.format("%.2f", trade.stake)}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "${if (trade.isWin) "+" else ""}$${String.format("%.2f", trade.profit)}",
                            color = if (trade.isWin) WinGreen else LossRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
