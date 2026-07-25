package com.example.data.db

import androidx.room.*
import com.example.data.model.SavedBot
import com.example.data.model.TradeContract
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val appId: String = "1089",
    val derivOAuthToken: String = "",
    val activeAccountId: String = "VRTC90938185",
    val isDemoAccount: Boolean = true,
    val maxSingleStake: Double = 500.0,
    val dailyLossLimit: Double = 1000.0,
    val executionSpeedFast: Boolean = true
)

@Dao
interface TradeDao {
    @Query("SELECT * FROM trade_contracts ORDER BY timestamp DESC")
    fun getAllTrades(): Flow<List<TradeContract>>

    @Query("SELECT * FROM trade_contracts WHERE isCompleted = 0 ORDER BY timestamp DESC")
    fun getOpenContracts(): Flow<List<TradeContract>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeContract)

    @Query("DELETE FROM trade_contracts")
    suspend fun clearHistory()
}

@Dao
interface SavedBotDao {
    @Query("SELECT * FROM saved_bots ORDER BY id DESC")
    fun getAllBots(): Flow<List<SavedBot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBot(bot: SavedBot)

    @Delete
    suspend fun deleteBot(bot: SavedBot)
}

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getSettings(): Flow<UserSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: UserSettingsEntity)
}

@Database(
    entities = [TradeContract::class, SavedBot::class, UserSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
    abstract fun savedBotDao(): SavedBotDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mkorean_deriv.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
