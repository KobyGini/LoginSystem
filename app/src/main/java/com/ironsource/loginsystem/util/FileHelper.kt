package com.ironsource.loginsystem.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Inject

class FileHelper
@Inject constructor(){

    inline fun <reified T> readAssetFile(
        context: Context,
        fileName: String
    ): T? {
        try {
            val gsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val collectionType: Type = object : TypeToken<T>() {}.type

            return Gson().newBuilder().enableComplexMapKeySerialization().create()
                .fromJson(gsonString, collectionType)
        } catch (ioException: IOException) {
        }
        return null
    }
}