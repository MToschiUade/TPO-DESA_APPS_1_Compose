package com.example.tpo_desa_1

import android.app.Application
import com.example.tpo_desa_1.data.db.AppDatabase

class MainApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
    }
}
