package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.viewmodel.AppTab
import com.example.ui.theme.*

@Composable
fun TopNavigationTabs(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AppTab.values().filter { it != AppTab.LANDING }.forEach { tab ->
            val isSelected = selectedTab == tab
            val bg = if (isSelected) RedAccent else DarkSurface
            val contentColor = if (isSelected) TextWhite else TextGray
            val icon = getTabIcon(tab)

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bg)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) GoldPrimary else DarkBorder,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("nav_tab_${tab.name.lowercase()}"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tab.title,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = tab.title,
                    color = contentColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun getTabIcon(tab: AppTab): ImageVector {
    return when (tab) {
        AppTab.LANDING -> Icons.Default.Home
        AppTab.DASHBOARD -> Icons.Default.Dashboard
        AppTab.BOT_BUILDER -> Icons.Default.Build
        AppTab.FREE_BOTS -> Icons.Default.SmartToy
        AppTab.PREMIUM_BOTS -> Icons.Default.MilitaryTech
        AppTab.SIGNAL_AI -> Icons.Default.TrackChanges
        AppTab.MANUAL_TRADER -> Icons.Default.TouchApp
        AppTab.BULK_TRADER -> Icons.Default.Layers
        AppTab.SMART_AI -> Icons.Default.Psychology
        AppTab.COPY_TRADER -> Icons.Default.Groups
        AppTab.ANALYSIS_TOOLS -> Icons.Default.Analytics
        AppTab.SPEEDBOT -> Icons.Default.RocketLaunch
        AppTab.TRADE_HISTORY -> Icons.Default.History
        AppTab.ADMIN -> Icons.Default.AdminPanelSettings
        AppTab.PROFILE -> Icons.Default.Person
    }
}
