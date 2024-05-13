package hardroid.pizza_mozzarella.rellarella.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient (
    val IngredientID : Int,
    val IngredientName: String,
    val IngredientPrice: Double
    ):Parcelable
