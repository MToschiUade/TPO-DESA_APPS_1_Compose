package com.example.tpo_desa_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.Usuario

@Database(entities = [Receta::class, Usuario::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recetas_db"
                )
                    .fallbackToDestructiveMigration() // ← Esta línea
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
