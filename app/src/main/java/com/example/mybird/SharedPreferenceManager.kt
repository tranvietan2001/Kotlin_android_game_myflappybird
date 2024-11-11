package com.example.mybird

import android.content.Context
import android.content.SharedPreferences

class  SharedPreferenceManager(context: Context){

    private var APP_NAME = "@MY_BIRD"
    private var PLAYER_MODE = "offline"
    private var CONF_LANG = "en"
    private var CONF_SOUND = "off"
    private var CONF_BIRD = ""

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

//    private val gson = Gson()

//    // Hàm lưu danh sách chim
//    fun saveListBird(birdList: List<String>) {
//        val json = sharedPreferences.toJson(birdList)
//        sharedPreferences.edit().putString("BIRD_LIST", json).apply()
//    }
//    // Hàm lấy danh sách chim
//    fun getListBird(): List<String> {
//        val json = prefs.getString("BIRD_LIST", null)
//        val type = object : TypeToken<List<String>>() {}.type
//        return gson.fromJson(json, type) ?: emptyList()
//    }


    // Hàm lưu trạng thái âm thanh
    fun saveStatusSoundConfig(isSoundOn: Boolean) {
        sharedPreferences.edit().putBoolean(CONF_SOUND, isSoundOn).apply()
    }

    // Hàm lấy trạng thái âm thanh
    fun getStatusSoundConfig(): Boolean {
        return sharedPreferences.getBoolean(CONF_SOUND, false) // Mặc định là false
    }
    // Hàm lưu ngôn ngữ
    fun saveLanguageConfig(language: String) {
        sharedPreferences.edit().putString(CONF_LANG, language).apply()
    }

    // Hàm lấy ngôn ngữ
    fun getLanguageConfig(): String {
        return sharedPreferences.getString(CONF_LANG, "en") ?: "en" // Mặc định là "en"
    }


    // Hàm lưu chế độ người chơi
    fun savePlayerMode(playerMode: String) {
        sharedPreferences.edit().putString(PLAYER_MODE, playerMode).apply()
    }

    // Hàm lấy chế độ người chơi
    fun getPlayerMode(): String {
        return sharedPreferences.getString(PLAYER_MODE, "offline") ?: "offline" // Mặc định là "en"
    }



}