package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.UserSettingsEntity
import com.example.data.model.*
import com.example.data.websocket.DerivWebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class DerivRepository(private val database: AppDatabase) {
    val wsManager = DerivWebSocketManager()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Database Flows
    val allTrades: Flow<List<TradeContract>> = database.tradeDao().getAllTrades()
    val openContracts: Flow<List<TradeContract>> = database.tradeDao().getOpenContracts()
    val savedBots: Flow<List<SavedBot>> = database.savedBotDao().getAllBots()
    val userSettings: Flow<UserSettingsEntity?> = database.userSettingsDao().getSettings()

    // Live State
    val accountBalance = wsManager.accountBalance
    private val _activeAccount = MutableStateFlow(
        Account(
            id = "ROT90938185",
            currency = "USD",
            balance = 0.30,
            isDemo = false,
            name = "USD Account"
        )
    )
    val activeAccount: StateFlow<Account> = _activeAccount.asStateFlow()

    private val _demoAccount = MutableStateFlow(
        Account(
            id = "VRTC90938185",
            currency = "USD",
            balance = 10000.00,
            isDemo = true,
            name = "Demo Account"
        )
    )
    val demoAccount: StateFlow<Account> = _demoAccount.asStateFlow()

    // Digit Analysis state
    private val recentDigitsMap = mutableMapOf<String, MutableList<Int>>()
    private val recentTickPricesMap = mutableMapOf<String, Double>()
    private val recentTickRisesMap = mutableMapOf<String, MutableList<Boolean>>()

    private val _digitAnalysis = MutableStateFlow(
        DigitDistribution(
            counts = (0..9).associateWith { 10 },
            percentages = (0..9).associateWith { 10.0 },
            recentDigits = listOf(2, 9, 2, 2, 9, 0, 4, 9, 8, 8),
            evenPercent = 50.8,
            oddPercent = 49.2,
            risePercent = 49.2,
            fallPercent = 50.8,
            over4Percent = 49.0,
            under5Percent = 51.0,
            currentPrice = 541.78,
            currentDigit = 8
        )
    )
    val digitAnalysis: StateFlow<DigitDistribution> = _digitAnalysis.asStateFlow()

    private val _selectedMarketSymbol = MutableStateFlow("R_100")
    val selectedMarketSymbol: StateFlow<String> = _selectedMarketSymbol.asStateFlow()

    // Markets list
    val availableMarkets = listOf(
        MarketSummary("R_100", "Volatility 100 Index", 541.78, 1.82, true),
        MarketSummary("R_10", "Volatility 10 Index", 9345.45, -0.41, false),
        MarketSummary("R_25", "Volatility 25 Index", 2643.26, 0.95, true),
        MarketSummary("R_50", "Volatility 50 Index", 98.32, -1.15, false),
        MarketSummary("R_75", "Volatility 75 Index", 45986.18, 0.64, true),
        MarketSummary("1HZ10V", "Volatility 10 (1s) Index", 9344.59, 0.12, true),
        MarketSummary("BOOM1000", "Boom 1000 Index", 1042.85, 2.15, true),
        MarketSummary("CRASH500", "Crash 500 Index", 482.10, -0.94, false)
    )

    init {
        scope.launch {
            wsManager.tickStream.collect { tick ->
                if (tick.symbol == _selectedMarketSymbol.value) {
                    processTickForDigitAnalysis(tick)
                }
            }
        }
    }

    fun setSelectedMarket(symbol: String) {
        _selectedMarketSymbol.value = symbol
        wsManager.subscribeTicks(symbol)
    }

    private fun processTickForDigitAnalysis(tick: MarketTick) {
        val symbol = tick.symbol
        val digitsList = recentDigitsMap.getOrPut(symbol) { mutableListOf() }
        digitsList.add(tick.lastDigit)
        if (digitsList.size > 100) {
            digitsList.removeAt(0)
        }

        val risesList = recentTickRisesMap.getOrPut(symbol) { mutableListOf() }
        risesList.add(tick.isRise)
        if (risesList.size > 100) {
            risesList.removeAt(0)
        }

        val total = digitsList.size
        val counts = (0..9).associateWith { d -> digitsList.count { it == d } }
        val percentages = (0..9).associateWith { d ->
            if (total > 0) ((counts[d] ?: 0).toDouble() / total) * 100.0 else 10.0
        }

        val evens = digitsList.count { it % 2 == 0 }
        val odds = total - evens
        val evenPct = if (total > 0) (evens.toDouble() / total) * 100.0 else 50.0
        val oddPct = 100.0 - evenPct

        val rises = risesList.count { it }
        val risePct = if (total > 0) (rises.toDouble() / total) * 100.0 else 50.0
        val fallPct = 100.0 - risePct

        val over4 = digitsList.count { it > 4 }
        val over4Pct = if (total > 0) (over4.toDouble() / total) * 100.0 else 50.0
        val under5Pct = 100.0 - over4Pct

        _digitAnalysis.value = DigitDistribution(
            counts = counts,
            percentages = percentages,
            recentDigits = digitsList.takeLast(10),
            evenPercent = String.format(java.util.Locale.US, "%.1f", evenPct).toDouble(),
            oddPercent = String.format(java.util.Locale.US, "%.1f", oddPct).toDouble(),
            risePercent = String.format(java.util.Locale.US, "%.1f", risePct).toDouble(),
            fallPercent = String.format(java.util.Locale.US, "%.1f", fallPct).toDouble(),
            over4Percent = String.format(java.util.Locale.US, "%.1f", over4Pct).toDouble(),
            under5Percent = String.format(java.util.Locale.US, "%.1f", under5Pct).toDouble(),
            currentPrice = tick.price,
            currentDigit = tick.lastDigit
        )
    }

    suspend fun switchAccount(isDemo: Boolean) {
        if (isDemo) {
            val curr = _activeAccount.value
            if (!curr.isDemo) {
                val demo = _demoAccount.value
                _activeAccount.value = demo
            }
        } else {
            _activeAccount.value = Account(
                id = "ROT90938185",
                currency = "USD",
                balance = 0.30,
                isDemo = false,
                name = "USD Account"
            )
        }
    }

    suspend fun placeTrade(
        symbol: String,
        contractType: ContractType,
        stake: Double,
        durationTicks: Int = 1
    ): TradeContract {
        val currAcc = _activeAccount.value
        val isWin = Random.nextFloat() > 0.35f // 65% simulation win rate
        val payoutRate = 0.95
        val payout = if (isWin) stake + (stake * payoutRate) else 0.0
        val profit = if (isWin) stake * payoutRate else -stake

        val newBalance = (currAcc.balance + profit).coerceAtLeast(0.0)
        _activeAccount.value = currAcc.copy(balance = newBalance)
        wsManager.updateBalance(newBalance)

        val symbolName = availableMarkets.find { it.symbol == symbol }?.name ?: symbol

        val contract = TradeContract(
            contractId = "CTR-" + System.currentTimeMillis().toString().takeLast(8),
            accountId = currAcc.id,
            symbol = symbol,
            symbolName = symbolName,
            contractType = contractType,
            stake = stake,
            buyPrice = stake,
            payout = payout,
            profit = profit,
            isWin = isWin,
            isCompleted = true
        )

        database.tradeDao().insertTrade(contract)
        return contract
    }

    suspend fun saveBotStrategy(bot: SavedBot) {
        database.savedBotDao().insertBot(bot)
    }

    suspend fun deleteBotStrategy(bot: SavedBot) {
        database.savedBotDao().deleteBot(bot)
    }

    suspend fun updateSettings(settings: UserSettingsEntity) {
        database.userSettingsDao().saveSettings(settings)
    }
}
