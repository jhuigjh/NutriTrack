package com.fit2081.Gan35090251.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.Gan35090251.nutritrack.data.dao.FoodIntakeDao
import com.fit2081.Gan35090251.nutritrack.data.dao.NutriCoachDao
import com.fit2081.Gan35090251.nutritrack.data.dao.PatientDao
import com.fit2081.Gan35090251.nutritrack.data.entity.FoodIntake
import com.fit2081.Gan35090251.nutritrack.data.entity.NutriCoach
import com.fit2081.Gan35090251.nutritrack.data.entity.Patient

@Database(entities = [Patient::class, FoodIntake::class, NutriCoach::class], version = 11, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachDao(): NutriCoachDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutritrack_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
