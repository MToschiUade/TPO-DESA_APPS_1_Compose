package com.example.tpo_desa_1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tpo_desa_1.data.model.Receta

@Database(entities = [Receta::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
}
