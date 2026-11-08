package com.example.fincare.db
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.fincare.domain.Accommodation
import com.example.fincare.domain.Contact
import com.example.fincare.domain.Investment
import com.example.fincare.domain.Transaction

@Database(
    version = 4,
    entities = [Accommodation::class, Contact::class, Transaction::class, Investment::class]
)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun accommodationDao(): AccommodationDao

    abstract fun contactDao(): ContactDao

    abstract fun transactionDao(): TransactionDao

    abstract fun investmentDao(): InvestmentDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(
                context,
                DatabaseHelper::class.java,
                "airbnb.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }
}