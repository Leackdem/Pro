package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.viewmodel.AppTab
import com.example.data.viewmodel.MainViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MkoreanTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MkoreanTheme {
                val currentTab by viewModel.currentTab.collectAsState()
                val activeAccount by viewModel.activeAccount.collectAsState()
                val demoAccount by viewModel.demoAccount.collectAsState()
                val isAccountSwitcherOpen by viewModel.isAccountSwitcherOpen.collectAsState()
                val isDigitAnalysisModalOpen by viewModel.isDigitAnalysisModalOpen.collectAsState()
                val digitAnalysis by viewModel.digitAnalysis.collectAsState()
                val isBotRunning by viewModel.isBotRunning.collectAsState()
                val isFastSpeed by viewModel.botExecutionSpeedFast.collectAsState()
                val botLogs by viewModel.botLogs.collectAsState()
                val selectedMarket by viewModel.selectedMarketSymbol.collectAsState()
                val allTrades by viewModel.allTrades.collectAsState(initial = emptyList())
                val userSettings by viewModel.userSettings.collectAsState(initial = null)
                val aiSignals by viewModel.aiSignals.collectAsState()

                val targetLoss by viewModel.botTargetLoss.collectAsState()
                val targetProfit by viewModel.botTargetProfit.collectAsState()
                val stake by viewModel.botStake.collectAsState()
                val lossMultiple by viewModel.botLossMultiple.collectAsState()

                Scaffold(
                    topBar = {
                        Column {
                            HeaderBar(
                                account = activeAccount,
                                onAccountClick = { viewModel.toggleAccountSwitcher(true) },
                                onRefreshClick = { viewModel.addBotLog("Refreshed WebSocket connection.") },
                                onMenuClick = { viewModel.selectTab(if (currentTab == AppTab.LANDING) AppTab.DASHBOARD else AppTab.LANDING) }
                            )

                            if (currentTab != AppTab.LANDING) {
                                TopNavigationTabs(
                                    selectedTab = currentTab,
                                    onTabSelected = { viewModel.selectTab(it) }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(DarkBackground)
                    ) {
                        when (currentTab) {
                            AppTab.LANDING -> LandingScreen(
                                onLoginClick = { viewModel.selectTab(AppTab.DASHBOARD) },
                                onRegisterClick = { viewModel.selectTab(AppTab.DASHBOARD) },
                                onExploreDemo = {
                                    viewModel.switchAccount(true)
                                    viewModel.selectTab(AppTab.DASHBOARD)
                                }
                            )

                            AppTab.DASHBOARD -> DashboardScreen(
                                markets = viewModel.availableMarkets,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenDigitAnalysis = { viewModel.toggleDigitAnalysisModal(true) }
                            )

                            AppTab.BOT_BUILDER -> BotBlockCard(
                                targetLoss = targetLoss,
                                onTargetLossChange = { viewModel.botTargetLoss.value = it },
                                targetProfit = targetProfit,
                                onTargetProfitChange = { viewModel.botTargetProfit.value = it },
                                stake = stake,
                                onStakeChange = { viewModel.botStake.value = it },
                                lossMultiple = lossMultiple,
                                onLossMultipleChange = { viewModel.botLossMultiple.value = it },
                                selectedMarket = selectedMarket,
                                onMarketChange = { viewModel.setSelectedMarket(it) },
                                isRunning = isBotRunning,
                                onToggleRun = { viewModel.toggleBotExecution() },
                                isFastSpeed = isFastSpeed,
                                onToggleSpeed = { viewModel.toggleExecutionSpeed() }
                            )

                            AppTab.FREE_BOTS, AppTab.PREMIUM_BOTS -> FreeBotsScreen(
                                onLoadBot = { bot -> viewModel.loadBot(bot) }
                            )

                            AppTab.SIGNAL_AI, AppTab.SMART_AI -> SignalAIScreen(
                                signals = aiSignals,
                                onExecuteSignal = { symbol, type ->
                                    viewModel.placeManualTrade(symbol, type, stake.toDoubleOrNull() ?: 10.0)
                                }
                            )

                            AppTab.MANUAL_TRADER, AppTab.BULK_TRADER -> ManualTraderScreen(
                                selectedMarket = selectedMarket,
                                onMarketChange = { viewModel.setSelectedMarket(it) },
                                digitDistribution = digitAnalysis,
                                onTradeClick = { type, tradeStake ->
                                    viewModel.placeManualTrade(selectedMarket, type, tradeStake)
                                }
                            )

                            AppTab.SPEEDBOT -> SpeedBotScreen(
                                isRunning = isBotRunning,
                                onToggleRun = { viewModel.toggleBotExecution() },
                                logs = botLogs
                            )

                            AppTab.TRADE_HISTORY -> TradeHistoryScreen(
                                trades = allTrades
                            )

                            AppTab.ADMIN -> AdminScreen(
                                userSettings = userSettings,
                                onSaveSettings = { appId, token -> viewModel.updateUserSettings(appId, token) }
                            )

                            AppTab.PROFILE -> ProfileScreen(
                                account = activeAccount,
                                onLogout = { viewModel.selectTab(AppTab.LANDING) }
                            )

                            else -> DashboardScreen(
                                markets = viewModel.availableMarkets,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenDigitAnalysis = { viewModel.toggleDigitAnalysisModal(true) }
                            )
                        }

                        // Dialog Overlays
                        if (isAccountSwitcherOpen) {
                            AccountSwitcherDialog(
                                activeAccount = activeAccount,
                                demoAccount = demoAccount,
                                onSwitchAccount = { isDemo -> viewModel.switchAccount(isDemo) },
                                onDismiss = { viewModel.toggleAccountSwitcher(false) },
                                onLogout = {
                                    viewModel.toggleAccountSwitcher(false)
                                    viewModel.selectTab(AppTab.LANDING)
                                }
                            )
                        }

                        if (isDigitAnalysisModalOpen) {
                            DigitAnalysisModal(
                                digitDistribution = digitAnalysis,
                                selectedMarketName = "Volatility 100 Index",
                                onDismiss = { viewModel.toggleDigitAnalysisModal(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
