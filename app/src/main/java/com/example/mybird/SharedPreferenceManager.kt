package com.example.mybird

import android.content.Context
import android.content.SharedPreferences

class  SharedPreferenceManager(context: Context){

    private var APP_NAME = "@MY_BIRD"
    private var PLAYER_MODE = "player_mode"
    private var CONF_LANG = "language"
    private var CONF_SOUND = "sound"
    private var PURCHASED_BIRDS = "purchased_birds"
    private var BIRD_UESD = "bird_used"
    private var COIN_SILVER = "coin"
    private val SCORE_PREF = "Score_pref"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

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

    // hàm lưu ds chim đã mua
    fun savePurchasedBird(nameBird: String) {
        val editor = sharedPreferences.edit()
        // Lấy danh sách hiện tại, nếu có
        val existingList = getPurchasedBirds().toMutableSet()
        existingList.add(nameBird) // Thêm tên mới vào danh sách
        editor.putStringSet(PURCHASED_BIRDS, existingList)
        editor.apply()
    }

    // hàm get ds chim đã mua
    fun getPurchasedBirds(): Set<String> {
        return sharedPreferences.getStringSet(PURCHASED_BIRDS, emptySet()) ?: emptySet()
    }

    fun setCoinSilver(coin:Int){
        sharedPreferences.edit().putInt(COIN_SILVER, coin).apply()
    }
    fun getCoinSilver(): Int{
        return sharedPreferences.getInt(COIN_SILVER, 0) ?: 0
    }

    fun setBirdUsed(nameBird:String){
        sharedPreferences.edit().putString(BIRD_UESD, nameBird).apply()
    }
    fun getBirdUsed(): String{
        return sharedPreferences.getString(BIRD_UESD, "bird1_down") ?: "bird1_down"
    }


    fun setMaxScore(score:Int){
        sharedPreferences.edit().putInt(SCORE_PREF, score).apply()
    }
    fun getMaxScore(): Int{
        return sharedPreferences.getInt(SCORE_PREF, 0) ?: 0
    }




}