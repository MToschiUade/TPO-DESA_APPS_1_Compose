package com.example.tpo_desa_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.demo.demoRecetas
import com.example.tpo_desa_1.data.demo.demoUsuario
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Receta::class, Usuario::class], version = 6)
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
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            if (AppConfig.ENABLE_DEMO_SEEDING) {
                                seedDemoData(context)
                            }
                        }
                    })
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

        private fun seedDemoData(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = getDatabase(context)
                val recetaDao = db.recetaDao()
                val usuarioDao = db.usuarioDao()

                // Borra todo y recarga siempre que la demo esté habilitada
                recetaDao.borrarTodas()
                usuarioDao.insertar(demoUsuario)
                recetaDao.insertarTodas(demoRecetas)
            }
        }
    }
}
