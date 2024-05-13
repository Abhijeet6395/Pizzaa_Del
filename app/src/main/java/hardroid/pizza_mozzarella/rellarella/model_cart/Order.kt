package hardroid.pizza_mozzarella.rellarella.model_cart

import hardroid.pizza_mozzarella.rellarella.model.Ingredient

data class Order(
    var final_price: Double, //a final price including bonuses and ingredients
    var final_price_without_bonuses: Double, // a final price but bonuses are not included
    var default_price: Double, // an original price for a product: bonuses and ingredients are not included
    var bonuses_applied: Boolean = false,
    var order_photo: String,
    var order_ingredients: List<Ingredient>,
    var order_quantity: Int

)