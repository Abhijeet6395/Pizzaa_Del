package hardroid.pizza_mozzarella.rellarella.decorator

import android.content.Context
import hardroid.pizza_mozzarella.rellarella.R
import java.text.DecimalFormat

class UIDecorator {
    fun getTextPrice(price: Double, context: Context): String{
        var txtPrice = getFormatedDoubleAsPrice(price)
        return context.getString(R.string.food_price, txtPrice);
    }

    fun getFormatedDoubleAsPrice(price: Double): String{
         return if (price % 1.0 == 0.0) price.toInt().toString() else DecimalFormat("#.##").format(price)
    }

    fun getTextPrice(price: Double): String{
        return "$${getFormatedDoubleAsPrice(price)}"
    }

    fun fromPriceToDouble(price: String): Double{
        try{
            return price.replace("$", "").replace(",",".").toDouble()
        }
        catch (_:Exception){
            return  0.0;
        }
    }
}