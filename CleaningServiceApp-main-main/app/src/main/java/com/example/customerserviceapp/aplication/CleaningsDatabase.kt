package com.example.customerserviceapp.aplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.customerserviceapp.dao.CleaningSdao
import com.example.customerserviceapp.model.CleaningS

@Database(entities = [CleaningS::class], version = 2, exportSchema = false)
abstract class CleaningsDatabase: RoomDatabase() {
    abstract fun cleaningsDao(): CleaningSdao

    companion object{
        private var INSTANCE: CleaningsDatabase? = null

        // migrasi database versi 1 ke 2,karena ada perubahan table tadi
        private val migration1To2: Migration = object :Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE CleaningS_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE CleaningS_table ADD COLUMN longitude Double DEFAULT 0.0")
            }

        }
        fun getDatabase(context: Context): CleaningsDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CleaningsDatabase::class.java,
                    "cleanings_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}
