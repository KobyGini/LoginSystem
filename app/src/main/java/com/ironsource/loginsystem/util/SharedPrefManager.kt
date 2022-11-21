package com.ironsource.loginsystem.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ironsource.loginsystem.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val PREFERENCES_FILE_KEY = "preference_file_key"
const val SAVED_USER = "saved_user"

class SharedPrefManager
@Inject constructor(
    @ApplicationContext val applicationContext: Context
) {

    private val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(
        PREFERENCES_FILE_KEY, Context.MODE_PRIVATE
    )

    fun saveUser(user: User) {
        writeStringSharedPref(
            SAVED_USER,
            Gson().toJson(user)
        )
    }

    fun getUser(): User? {
        val userJson = sharedPref.getString(SAVED_USER, "").orEmpty()
        return if (userJson.isNotEmpty()) {
            Gson().fromJson(userJson, User::class.java)
        } else null
    }


    private fun writeBooleanSharedPref(
        key: String,
        value: Boolean
    ) {
        with(sharedPref.edit()) {
            putBoolean(key, value).commit()
        }
    }

    private fun writeIntSharedPref(
        key: String,
        value: Int
    ) {
        with(sharedPref.edit()) {
            putInt(key, value).commit()
        }
    }

    private fun writeStringSharedPref(
        key: String,
        value: String
    ) {
        with(sharedPref.edit()) {
            putString(key, value).commit()
        }
    }

    fun removeUser() {
        writeStringSharedPref(
            SAVED_USER,
            ""
        )
    }
}