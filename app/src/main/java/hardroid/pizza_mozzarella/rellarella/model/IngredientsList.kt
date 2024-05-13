package hardroid.pizza_mozzarella.rellarella.model

class IngredientsList {
    private val IngredientsList: List<Ingredient> = listOf(



        //Meat
        Ingredient(1,"Italian sausages",1.2),
        Ingredient(2,"Beef",1.2),
        Ingredient(3,"Peperony",1.4),
        Ingredient(4,"Chicken breast",2.0),
        Ingredient(5,"Bacon",1.0),

        //Vegetables
        Ingredient(6,"Pineapples",0.5),
        Ingredient(7,"Onions",0.3),
        Ingredient(8,"Mushrooms",0.8),
        Ingredient(9,"Sweet peppers",0.9),
        Ingredient(10,"Jalapeno",1.2),

        //Cheese
        Ingredient(11,"3 cheese blend",1.5),
        Ingredient(12,"Parmesan",1.8),
        Ingredient(13,"Cheddar",2.2),
        Ingredient(14,"Mozzarella rella rella",2.5)
    )



    public fun getIngredientsList(): List<Ingredient> {
        return IngredientsList;
    }
}