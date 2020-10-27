package com.iconic_dev.telleriumfarms.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iconic_dev.telleriumfarms.db.models.Farmer
import com.iconic_dev.telleriumfarms.utils.Converters

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
@Database(entities = [Farmer::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class FarmersDatabase : RoomDatabase(){
    abstract val sleepDatabaseDao: FarmersDao

    companion object {

        @Volatile
        private var INSTANCE: FarmersDatabase? = null

        fun getInstance(context: Context): FarmersDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FarmersDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


