package hardroid.pizza_mozzarella.rellarella.db_context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hardroid.pizza_mozzarella.rellarella.model_cart.Order
import hardroid.pizza_mozzarella.rellarella.prefs

class DbContext {
    private val keyOrder = "orders"

    fun saveOrderListToDB(list: List<Order>) {
        val pref = prefs.getPref() ?: return
        val editor = pref.edit()

        val gson = Gson()
        val json = gson.toJson(list)

        editor.putString(keyOrder, json)
        editor.apply()
    }

    fun getOrderListFromDB(): List<Order> {
        val pref = prefs.getPref() ?: return emptyList()
        val json = pref.getString(keyOrder, null)
        val gson = Gson()                                        //email: abhijee0123@gmail.com
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun deleteAllOrdersFromDB(){
        val pref = prefs.getPref() ?: return
        val editor = pref.edit()
        editor.remove(keyOrder)
        editor.apply()
    }

    fun deleteItemFromDB(item: Order) {
        val pref = prefs.getPref() ?: return
        val editor = pref.edit()
        val orderList = getOrderListFromDB().toMutableList()
        orderList.remove(item)
        val gson = Gson()
        val json = gson.toJson(orderList)
        editor.putString(keyOrder, json)
        editor.apply()
    }



}