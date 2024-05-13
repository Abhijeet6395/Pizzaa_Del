package hardroid.pizza_mozzarella.rellarella.model

// Pizzas class just save data
//"data" clause that says this class is a constructor of the data


 data class PizzaModel(
    val id:  Long,
    val photo: String,
    val name: String,
    val description: String,
    val price: Double,
    val original_ingredients: List<Ingredient>
)