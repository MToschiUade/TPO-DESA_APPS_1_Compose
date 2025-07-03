package com.example.tpo_desa_1.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload_", ".tmp", context.cacheDir)
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}