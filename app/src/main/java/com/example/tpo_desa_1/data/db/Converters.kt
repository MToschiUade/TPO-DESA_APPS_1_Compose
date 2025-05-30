package com.example.tpo_desa_1.data.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListToString(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToList(data: String): List<Int> {
        return if (data.isBlank()) emptyList() else data.split(",").map { it.toInt() }
    }
}
