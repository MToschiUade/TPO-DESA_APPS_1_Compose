package com.example.tpo_desa_1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.demo.demoComentarios
import com.example.tpo_desa_1.data.demo.demoPasos
import com.example.tpo_desa_1.data.demo.demoRecetas
import com.example.tpo_desa_1.data.demo.demoUsuarios
import com.example.tpo_desa_1.data.model.Comentario
import com.example.tpo_desa_1.data.model.PasoReceta
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.model.RecetaGuardada
import com.example.tpo_desa_1.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(    entities = [
    Receta::class,
    Usuario::class,
    Comentario::class,
    PasoReceta::class ,
    RecetaGuardada::class
],version = 11)
@TypeConverters(Converters::class) // ✅ IMPORTANTE
abstract class AppDatabase : RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun comentarioDao(): ComentarioDao
    abstract fun pasoRecetaDao(): PasoRecetaDao
    abstract fun recetaGuardadaDao(): RecetaGuardadaDao
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
                val comentarioDao = db.comentarioDao()
                val pasoRecetaDao = db.pasoRecetaDao()

                // Insertar usuarios demo (evita duplicados)
                demoUsuarios.forEach { usuario ->
                    val existe = usuarioDao.obtenerPorAlias(usuario.alias)
                    if (existe == null) {
                        usuarioDao.insertar(usuario)
                    }
                }

                recetaDao.borrarTodas()
                recetaDao.insertarTodas(demoRecetas)

                comentarioDao.borrarTodos()
                comentarioDao.insertarTodos(demoComentarios)

                pasoRecetaDao.borrarTodos()
                pasoRecetaDao.insertarTodos(demoPasos)
            }
        }
    }
}
