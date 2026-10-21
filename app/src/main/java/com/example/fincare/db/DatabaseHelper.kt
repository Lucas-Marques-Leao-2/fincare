package com.example.fincare.db
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.fincare.domain.Accommodation
import com.example.fincare.domain.Contact

@Database(
    version = 1, entities = [Accommodation::class, Contact::class]
)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun accommodationDao(): AccommodationDao

    abstract fun contactDao(): ContactDao

    companion object {
        fun getInstance(context: Context): DatabaseHelper {
            return Room.databaseBuilder(
                context,
                DatabaseHelper::class.java,
                "airbnb.db"
            ).build()
        }
    }
}