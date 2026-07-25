package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketSummary
import com.example.ui.theme.*

@Composable
fun TickerMarquee(
    markets: List<MarketSummary>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF07090E))
            .padding(vertical = 4.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        markets.forEach { market ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = if (market.isUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (market.isUp) WinGreen else LossRed,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = market.name,
                    color = TextWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                )

                Text(
                    text = "${String.format(java.util.Locale.US, "%.2f", market.price)}",
                    color = TextGray,
                    fontSize = 11.sp
                )

                Text(
                    text = "${if (market.isUp) "+" else ""}${String.format(java.util.Locale.US, "%.2f", market.changePercent)}%",
                    color = if (market.isUp) WinGreen else LossRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Text(
                text = "•",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}
