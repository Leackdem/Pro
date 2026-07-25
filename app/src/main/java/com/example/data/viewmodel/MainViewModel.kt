package com.example.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.UserSettingsEntity
import com.example.data.model.*
import com.example.data.repository.DerivRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppTab(val title: String, val icon: String) {
    LANDING("Landing", "home"),
    DASHBOARD("Dashboard", "dashboard"),
    BOT_BUILDER("Bot Builder", "build"),
    FREE_BOTS("Free Bots", "smart_toy"),
    PREMIUM_BOTS("Premium Bots", "military_tech"),
    SIGNAL_AI("Signal AI", "track_changes"),
    MANUAL_TRADER("Manual Trader", "touch_app"),
    BULK_TRADER("Bulk Trader", "layers"),
    SMART_AI("Smart AI", "psychology"),
    COPY_TRADER("Copy Trader", "groups"),
    ANALYSIS_TOOLS("Analysis Tools", "analytics"),
    SPEEDBOT("Speedbot", "rocket_launch"),
    TRADE_HISTORY("Trade History", "history"),
    ADMIN("Admin Panel", "admin_panel_settings"),
    PROFILE("Profile", "person")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    val repository = DerivRepository(database)

    // Current Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.LANDING)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Modals
    private val _isAccountSwitcherOpen = MutableStateFlow(false)
    val isAccountSwitcherOpen: StateFlow<Boolean> = _isAccountSwitcherOpen.asStateFlow()

    private val _isDigitAnalysisModalOpen = MutableStateFlow(false)
    val isDigitAnalysisModalOpen: StateFlow<Boolean> = _isDigitAnalysisModalOpen.asStateFlow()

    // Account & Data flows
    val activeAccount = repository.activeAccount
    val demoAccount = repository.demoAccount
    val digitAnalysis = repository.digitAnalysis
    val selectedMarketSymbol = repository.selectedMarketSymbol
    val availableMarkets = repository.availableMarkets
    val allTrades = repository.allTrades
    val openContracts = repository.openContracts
    val savedBots = repository.savedBots
    val userSettings = repository.userSettings

    // Bot execution state
    private val _isBotRunning = MutableStateFlow(false)
    val isBotRunning: StateFlow<Boolean> = _isBotRunning.asStateFlow()

    private val _botExecutionSpeedFast = MutableStateFlow(true)
    val botExecutionSpeedFast: StateFlow<Boolean> = _botExecutionSpeedFast.asStateFlow()

    private val _botLogs = MutableStateFlow<List<String>>(listOf("System Ready. Connect Deriv OAuth or start Demo."))
    val botLogs: StateFlow<List<String>> = _botLogs.asStateFlow()

    private var botRunnerJob: Job? = null

    // Bot Config State (Matching Screenshot 5 & 6)
    val botTargetLoss = MutableStateFlow("9999")
    val botTargetProfit = MutableStateFlow("200")
    val botStake = MutableStateFlow("110")
    val botLossMultiple = MutableStateFlow("2.0")
    val botTradeType = MutableStateFlow("Even/Odd")
    val botContractType = MutableStateFlow("Odd")

    // AI Signals List
    val aiSignals = MutableStateFlow(
        listOf(
            AISignal("1", "R_100", "Volatility 100 Index", ContractType.ODD, 88, "Digit Distribution 8 Trend + RSI Oversold"),
            AISignal("2", "R_10", "Volatility 10 Index", ContractType.EVEN, 82, "High Frequency Even Digit Cluster"),
            AISignal("3", "BOOM1000", "Boom 1000 Index", ContractType.RISE, 91, "Boom Spike Momentum Indicator"),
            AISignal("4", "CRASH500", "Crash 500 Index", ContractType.FALL, 85, "Crash Drop Signal")
        )
    )

    // Master Traders
    val masterTraders = MutableStateFlow(
        listOf(
            MasterTrader("1", "Amina Hassan", "AH", 94, 18450.0, 1240, 2, true),
            MasterTrader("2", "Ochieng Deriv", "OD", 89, 12100.0, 850, 3, false),
            MasterTrader("3", "David M.", "DM", 86, 9540.0, 620, 2, false),
            MasterTrader("4", "Satoshi Bot", "SB", 92, 24100.0, 2150, 1, false)
        )
    )

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun toggleAccountSwitcher(open: Boolean) {
        _isAccountSwitcherOpen.value = open
    }

    fun toggleDigitAnalysisModal(open: Boolean) {
        _isDigitAnalysisModalOpen.value = open
    }

    fun setSelectedMarket(symbol: String) {
        repository.setSelectedMarket(symbol)
    }

    fun switchAccount(isDemo: Boolean) {
        viewModelScope.launch {
            repository.switchAccount(isDemo)
            _isAccountSwitcherOpen.value = false
            addBotLog("Switched to ${if (isDemo) "Demo ($10,000.00)" else "Real USD ($0.30)"}")
        }
    }

    fun placeManualTrade(symbol: String, type: ContractType, stake: Double) {
        viewModelScope.launch {
            addBotLog("Executing Manual $type on $symbol ($${stake})...")
            val result = repository.placeTrade(symbol, type, stake)
            if (result.isWin) {
                addBotLog("WON: +$${String.format("%.2f", result.profit)} (Payout: $${String.format("%.2f", result.payout)})")
            } else {
                addBotLog("LOST: -$${String.format("%.2f", stake)}")
            }
        }
    }

    fun toggleBotExecution() {
        if (_isBotRunning.value) {
            stopBotExecution()
        } else {
            startBotExecution()
        }
    }

    fun toggleExecutionSpeed() {
        _botExecutionSpeedFast.value = !_botExecutionSpeedFast.value
    }

    private fun startBotExecution() {
        _isBotRunning.value = true
        addBotLog("Starting Bot Strategy execution...")
        botRunnerJob?.cancel()
        botRunnerJob = viewModelScope.launch {
            var currentStake = botStake.value.toDoubleOrNull() ?: 10.0
            val targetProfitVal = botTargetProfit.value.toDoubleOrNull() ?: 200.0
            val targetLossVal = botTargetLoss.value.toDoubleOrNull() ?: 9999.0
            val lossMult = botLossMultiple.value.toDoubleOrNull() ?: 2.0
            var totalProfitAccumulated = 0.0

            while (isActive && _isBotRunning.value) {
                val speedDelay = if (_botExecutionSpeedFast.value) 1200L else 2500L
                delay(speedDelay)

                val contractType = if (botContractType.value == "Odd") ContractType.ODD else ContractType.EVEN
                val symbol = selectedMarketSymbol.value
                val tradeResult = repository.placeTrade(symbol, contractType, currentStake)

                if (tradeResult.isWin) {
                    totalProfitAccumulated += tradeResult.profit
                    addBotLog("BOT TRADE WON! Profit: +$${String.format("%.2f", tradeResult.profit)}. Resetting Stake to ${botStake.value}")
                    currentStake = botStake.value.toDoubleOrNull() ?: 10.0
                } else {
                    totalProfitAccumulated += tradeResult.profit
                    currentStake *= lossMult
                    addBotLog("BOT TRADE LOST! Martingale multiplying stake to $${String.format("%.2f", currentStake)}")
                }

                if (totalProfitAccumulated >= targetProfitVal) {
                    addBotLog("TARGET PROFIT REACHED (+$${String.format("%.2f", totalProfitAccumulated)})! Bot Stopping.")
                    stopBotExecution()
                    break
                } else if (kotlin.math.abs(totalProfitAccumulated) >= targetLossVal && totalProfitAccumulated < 0) {
                    addBotLog("STOP LOSS HIT (-$${String.format("%.2f", kotlin.math.abs(totalProfitAccumulated))})! Bot Stopping.")
                    stopBotExecution()
                    break
                }
            }
        }
    }

    private fun stopBotExecution() {
        _isBotRunning.value = false
        botRunnerJob?.cancel()
        addBotLog("Bot execution stopped.")
    }

    fun addBotLog(message: String) {
        val timeStr = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val updated = _botLogs.value.toMutableList()
        updated.add("[$timeStr] $message")
        if (updated.size > 100) updated.removeAt(0)
        _botLogs.value = updated
    }

    fun saveBot(name: String, desc: String, category: String) {
        viewModelScope.launch {
            val bot = SavedBot(
                name = name,
                description = desc,
                category = category,
                marketSymbol = selectedMarketSymbol.value,
                tradeType = botTradeType.value,
                targetProfit = botTargetProfit.value.toDoubleOrNull() ?: 200.0,
                stopLoss = botTargetLoss.value.toDoubleOrNull() ?: 9999.0,
                stake = botStake.value.toDoubleOrNull() ?: 110.0,
                lossMultiplier = botLossMultiple.value.toDoubleOrNull() ?: 2.0
            )
            repository.saveBotStrategy(bot)
            addBotLog("Bot Strategy '$name' saved successfully.")
        }
    }

    fun loadBot(bot: SavedBot) {
        botTargetProfit.value = bot.targetProfit.toString()
        botTargetLoss.value = bot.stopLoss.toString()
        botStake.value = bot.stake.toString()
        botLossMultiple.value = bot.lossMultiplier.toString()
        botTradeType.value = bot.tradeType
        setSelectedMarket(bot.marketSymbol)
        selectTab(AppTab.BOT_BUILDER)
        addBotLog("Loaded Bot Strategy '${bot.name}'")
    }

    fun updateUserSettings(appId: String, token: String) {
        viewModelScope.launch {
            val newSettings = UserSettingsEntity(
                appId = appId,
                derivOAuthToken = token
            )
            repository.updateSettings(newSettings)
            repository.wsManager.connect(appId, token)
            addBotLog("Updated Deriv App ID ($appId) and Token. Connected to WebSocket.")
        }
    }
}
