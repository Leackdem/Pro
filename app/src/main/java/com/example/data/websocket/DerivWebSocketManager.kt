package com.example.data.websocket

import android.util.Log
import com.example.data.model.MarketTick
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import org.json.JSONObject
import kotlin.random.Random

class DerivWebSocketManager {
    private val client = OkHttpClient.Builder().build()
    private var webSocket: WebSocket? = null

    private val _connectionState = MutableStateFlow<Boolean>(false)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    private val _tickStream = MutableSharedFlow<MarketTick>(extraBufferCapacity = 64)
    val tickStream: SharedFlow<MarketTick> = _tickStream.asSharedFlow()

    private val _accountBalance = MutableStateFlow<Double>(10000.0)
    val accountBalance: StateFlow<Double> = _accountBalance.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var mockTickJob: Job? = null

    private val marketPrices = mutableMapOf(
        "R_100" to 541.78,
        "R_10" to 9345.45,
        "R_25" to 2643.26,
        "R_50" to 98.32,
        "R_75" to 45986.18,
        "1HZ10V" to 9344.59,
        "BOOM1000" to 1042.85,
        "CRASH500" to 482.10
    )

    init {
        startMockTickGenerator()
    }

    fun connect(appId: String = "1089", token: String? = null) {
        val request = Request.Builder()
            .url("wss://ws.derivws.com/websockets/v3?app_id=$appId")
            .build()

        webSocket?.close(1000, "Reconnecting")
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                _connectionState.value = true
                Log.d("DerivWS", "WebSocket Connected")
                token?.let {
                    if (it.isNotBlank()) {
                        ws.send(JSONObject().apply { put("authorize", it) }.toString())
                    }
                }
                // Subscribe to Volatility 100 Index ticks
                ws.send(JSONObject().apply { put("ticks", "R_100") }.toString())
            }

            override fun onMessage(ws: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    if (json.has("tick")) {
                        val tickObj = json.getJSONObject("tick")
                        val symbol = tickObj.optString("symbol", "R_100")
                        val quote = tickObj.optDouble("quote", 500.0)
                        val lastDigitStr = quote.toString().takeLast(1)
                        val lastDigit = lastDigitStr.toIntOrNull() ?: Random.nextInt(0, 10)
                        val isRise = quote > (marketPrices[symbol] ?: quote)
                        marketPrices[symbol] = quote

                        coroutineScope.launch {
                            _tickStream.emit(
                                MarketTick(
                                    symbol = symbol,
                                    name = getMarketName(symbol),
                                    price = quote,
                                    lastDigit = lastDigit,
                                    isRise = isRise
                                )
                            )
                        }
                    } else if (json.has("balance")) {
                        val balanceObj = json.getJSONObject("balance")
                        val balance = balanceObj.optDouble("balance", 10000.0)
                        _accountBalance.value = balance
                    }
                } catch (e: Exception) {
                    Log.e("DerivWS", "Error parsing message: ${e.message}")
                }
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = false
                Log.e("DerivWS", "WebSocket Failure: ${t.message}")
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                _connectionState.value = false
                Log.d("DerivWS", "WebSocket Closed: $reason")
            }
        })
    }

    fun subscribeTicks(symbol: String) {
        webSocket?.send(JSONObject().apply { put("ticks", symbol) }.toString())
    }

    fun updateBalance(newBalance: Double) {
        _accountBalance.value = newBalance
    }

    private fun startMockTickGenerator() {
        mockTickJob?.cancel()
        mockTickJob = coroutineScope.launch {
            while (isActive) {
                delay(800) // Emit ticks every 800ms
                marketPrices.keys.forEach { symbol ->
                    val currentPrice = marketPrices[symbol] ?: 100.0
                    val delta = (Random.nextDouble(-0.85, 0.85))
                    val newPrice = (currentPrice + delta).coerceAtLeast(1.0)
                    val roundedPrice = String.format(java.util.Locale.US, "%.2f", newPrice).toDouble()
                    marketPrices[symbol] = roundedPrice

                    val priceStr = String.format(java.util.Locale.US, "%.2f", roundedPrice)
                    val lastChar = priceStr.takeLast(1)
                    val lastDigit = lastChar.toIntOrNull() ?: Random.nextInt(0, 10)
                    val isRise = delta >= 0

                    _tickStream.emit(
                        MarketTick(
                            symbol = symbol,
                            name = getMarketName(symbol),
                            price = roundedPrice,
                            lastDigit = lastDigit,
                            isRise = isRise
                        )
                    )
                }
            }
        }
    }

    private fun getMarketName(symbol: String): String {
        return when (symbol) {
            "R_100" -> "Volatility 100 Index"
            "R_10" -> "Volatility 10 Index"
            "R_25" -> "Volatility 25 Index"
            "R_50" -> "Volatility 50 Index"
            "R_75" -> "Volatility 75 Index"
            "1HZ10V" -> "Volatility 10 (1s) Index"
            "BOOM1000" -> "Boom 1000 Index"
            "CRASH500" -> "Crash 500 Index"
            else -> symbol
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "App closed")
        mockTickJob?.cancel()
    }
}
