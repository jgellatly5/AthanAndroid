package com.gallopdevs.athanhelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gallopdevs.athanhelper.data.models.Converters
import com.gallopdevs.athanhelper.data.models.TimingsResponse

@Database(entities = [TimingsResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class TimingsResponsesDatabase : RoomDatabase() {

    abstract fun timingsDao(): TimingsDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var INSTANCE: TimingsResponsesDatabase? = null

        fun getDatabase(context: Context): TimingsResponsesDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimingsResponsesDatabase::class.java,
                    "timings_responses_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
