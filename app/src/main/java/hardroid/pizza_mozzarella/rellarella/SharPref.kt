package hardroid.pizza_mozzarella.rellarella

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class SharPref(private val context: Context) {
    private val keyPref = "PIZZA_PREF"
    private val pref = context.getSharedPreferences(keyPref,Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    val keySpecialPromotion = "FIRST_ORDER"
    val keyOrders = "PIZZA_ORDERS"

    fun getPref(): SharedPreferences? {
        return pref
    }

    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        pref.edit().putString(key, jsonString).apply()
    }

    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = getPref()?.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }

}