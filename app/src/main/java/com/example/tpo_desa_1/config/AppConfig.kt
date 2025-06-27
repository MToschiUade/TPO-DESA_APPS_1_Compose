package com.example.tpo_desa_1.config

object AppConfig {
    const val USE_REMOTE_DATA_SOURCE = true // 🔁 cambiar a true cuando quieras probar con la API
    const val ALLOW_API_FAILURE_CACHE = false // (opcional futuro) Si cacheamos los últimos datos de la API

    // Cambiá esta flag según estés usando local o cloud
    const val USE_LOCAL_API = true

    // URL para correr localmente desde emulador de Android
    private const val LOCAL_BASE_URL = "http://10.0.2.2:4002/" // ¡Importantísimo usar 10.0.2.2 para localhost del host!

    // URL productiva (Azure)
    private const val CLOUD_BASE_URL = "https://ratatouille-gmecd7ema2c8bqfz.brazilsouth-01.azurewebsites.net/"

    const val useCloudLogin = true // cambiar a true cuando quieras usar el BE

    val BASE_URL: String
        get() = if (USE_LOCAL_API) LOCAL_BASE_URL else CLOUD_BASE_URL
}
