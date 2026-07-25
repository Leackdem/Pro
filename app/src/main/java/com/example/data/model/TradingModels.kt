package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Account(
    val id: String,
    val currency: String,
    val balance: Double,
    val isDemo: Boolean,
    val token: String = "",
    val name: String = if (isDemo) "Demo Account" else "USD Account"
)

data class MarketTick(
    val symbol: String,
    val name: String,
    val price: Double,
    val lastDigit: Int,
    val isRise: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class MarketSummary(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val isUp: Boolean
)

enum class ContractType {
    EVEN, ODD, RISE, FALL, OVER, UNDER, MATCHES, DIFFERS
}

@Entity(tableName = "trade_contracts")
data class TradeContract(
    @PrimaryKey val contractId: String,
    val accountId: String,
    val symbol: String,
    val symbolName: String,
    val contractType: ContractType,
    val stake: Double,
    val buyPrice: Double,
    val payout: Double,
    val profit: Double,
    val isWin: Boolean,
    val isCompleted: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_bots")
data class SavedBot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val category: String, // FREE, PREMIUM, AI, CUSTOM
    val marketSymbol: String,
    val tradeType: String,
    val targetProfit: Double,
    val stopLoss: Double,
    val stake: Double,
    val lossMultiplier: Double,
    val winRate: Int = 85,
    val isFavorite: Boolean = false
)

data class DigitDistribution(
    val counts: Map<Int, Int>,
    val percentages: Map<Int, Double>,
    val recentDigits: List<Int>,
    val evenPercent: Double,
    val oddPercent: Double,
    val risePercent: Double,
    val fallPercent: Double,
    val over4Percent: Double,
    val under5Percent: Double,
    val currentPrice: Double,
    val currentDigit: Int
)

data class AISignal(
    val id: String,
    val symbol: String,
    val symbolName: String,
    val type: ContractType,
    val confidence: Int, // e.g. 88%
    val indicator: String, // e.g. "RSI Oversold + Digit Pattern"
    val timestamp: Long = System.currentTimeMillis()
)

data class MasterTrader(
    val id: String,
    val name: String,
    val avatarInitials: String,
    val winRate: Int,
    val totalProfit: Double,
    val copiersCount: Int,
    val riskScore: Int,
    val isCopying: Boolean = false
)
