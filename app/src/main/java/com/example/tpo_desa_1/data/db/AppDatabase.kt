package com.example.tpo_desa_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tpo_desa_1.data.demo.usuarioDemo
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Receta::class, Usuario::class], version = 4)
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
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Inserción del usuario demo en un hilo separado
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getDatabase(context).usuarioDao()
                                dao.insertar(usuarioDemo)
                            }
                        }
                    })
                    .build().also {
                        INSTANCE = it
                    }
            }
        }

    }
}
