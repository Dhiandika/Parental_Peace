package com.dicoding.parentalpeaceapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.parentalpeaceapp.response.DataItem

@Database(entities = [DataItem::class], version = 1, exportSchema = false)
abstract class ParentalPeaceDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ParentalPeaceDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ParentalPeaceDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ParentalPeaceDatabase::class.java, "Parental_Peace_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}