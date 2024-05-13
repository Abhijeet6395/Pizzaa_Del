package hardroid.pizza_mozzarella.rellarella.model

import java.util.Collections

/*New type declaration of variables
Similarly in C++ typedef keyword
PizzasListeners is a function which takes List of Pizzas and returns nothing
 */
typealias PizzasListeners = (Pizzas: List<PizzaModel>) -> Unit

//This class is used to manipulate data

class PizzaService {
    private var PizzasList: MutableList<PizzaModel> = mutableListOf<PizzaModel>() //List of pizzas
    private val listeners: MutableSet<PizzasListeners> = mutableSetOf<PizzasListeners>()
    //a variable that saves all listener of pizza


    init {
        val AllIngredients = IngredientsList().getIngredientsList()
        PizzasList.add(
            PizzaModel(
                1,
                IMAGES[0],
                "PIZZA MOZZARELLA",
                "Description: Indulge in the classic \"Pizza Mozzarella\" adorned with Italian sausages, crispy bacon, a blend of cheddar and mozzarella cheese, and a spicy kick of jalapenos. This pizza is not complete without a nod to the talented Gyro Zeppeli.",
                11.99,
                listOf(AllIngredients[0], AllIngredients[4], AllIngredients[13], AllIngredients[10])
            )
        )
        PizzasList.add(
            PizzaModel(
                2,
                IMAGES[1],
                "Six Shooter Special",
                "Experience a taste sensation inspired by Mista and his Stand, Sex Pistols, with the \"Six Shooter Special\" pizza. This flavor-packed delight combines a symphony of Italian sausages, cheddar cheese, crispy bacon, and tender beef, delivering a culinary performance that hits the bullseye every time.",
                9.99,
                listOf(AllIngredients[11], AllIngredients[13], AllIngredients[5], AllIngredients[1])
            )
        )
        PizzasList.add(
            PizzaModel(
                3,
                IMAGES[2],
                "Golden Wind Delight",
                "Taste the golden breeze with the \"Golden Wind Delight\" pizza, topped with sweet pineapples, savory onions, a generous blend of mozzarella and cheddar cheese, and tender chicken breast. This pizza is a gust of flavor you won't forget.",
                11.50,
                listOf(AllIngredients[7], AllIngredients[6], AllIngredients[13], AllIngredients[3])
            )
        )
        PizzasList.add(
            PizzaModel(
                4,
                IMAGES[3],
                "Jojolicious Jalapeno",
                "Brace yourself for the spicy delight of the \"Jojolicious Jalapeno\" pizza, featuring the fiery kick of jalapenos, earthy mushrooms, tantalizing cheddar cheese, and the creaminess of mozzarella. Each bite is an explosion of flavor worthy of JoJo's epic battles.",
                8.99,
                listOf(
                    AllIngredients[0],
                    AllIngredients[1],
                    AllIngredients[2],
                    AllIngredients[9],
                    AllIngredients[11]
                )
            )
        )
        PizzasList.add(
            PizzaModel(
                5,
                IMAGES[4],
                "Stone Ocean Supreme",
                "Experience a supreme flavor adventure with the \"Stone Ocean Supreme\" pizza, featuring a tantalizing combination of beef, pepperoni, cheddar cheese, and the zing of jalapenos. Get ready to ride the waves of taste!",
                9.50,
                listOf(AllIngredients[10], AllIngredients[9], AllIngredients[5], AllIngredients[13])
            )
        )
    }

    //get list
    fun getPizza(): List<PizzaModel> {
        return PizzasList
    }

    //delete pizza from list
    fun deletePizza(pizza: PizzaModel) {
        val indexToDelete: Int = PizzasList.indexOfFirst { it.id == pizza.id }
        if (indexToDelete != -1) {
            PizzasList.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    //move pizza to up or down
    fun movePizza(pizza: PizzaModel, moveBy: Int) {
        val oldIndex: Int = PizzasList.indexOfFirst { it.id == pizza.id }
        if (oldIndex == -1) return
        val newIndex: Int = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= PizzasList.size) return
        Collections.swap(PizzasList, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: PizzasListeners) {
        listeners.add(listener)
        listener.invoke(PizzasList)
    }

    fun removeListener(listener: PizzasListeners) {
        listeners.remove(listener)
    }


    private fun notifyChanges() {
        listeners.forEach { it.invoke(PizzasList) }
    }


    //instead of static
    public companion object {


        private val IMAGES = mutableListOf(
            "https://lh3.googleusercontent.com/pw/AL9nZEUzXvAWnS4djr16cxX3zovVFa0-GiQt9IzzsIvHS4A4lTGCPCGKCxJCA7Zx-XL7eKVZhuv3rGt8KWoYE8dBABWvuLlscdMQ7D-BR3dDcu1sGDpNbhKUxpEdXMqwEuUBen9yK7Cg7ZpTZjBCaldU0Kdk=w728-h410-no?authuser=0",
            "https://lh3.googleusercontent.com/pw/AL9nZEXnRcYLdyC3Al2YfJpXqmqutp3iZo2ojSDU8nLaXcbbPsYPkn23-hjGeNE28HAQO0EuKk0OTuU8xRrMjrfqyYeVDGPZioE2IxfHzVHTfzfFcREmeY1ULS-5K3gsHECBQTgwzuxdnEKDwe4xSQZ5ASRC=w960-h540-no?authuser=0",
            "https://lh3.googleusercontent.com/pw/AL9nZEWk-Txleato4O-tlGyJLTkiEX2Qx0-KCvpPbIXRrc_afzr2350zVJDkAI01LIuunwCB77uPDN0-P_D6NL635ewj9wPhGyUAJaCwI6vVu1tb5zxolQod8iNm1Ae00lRmvx5SMQVUhv82-DdGvhNFuATZ=w960-h600-no?authuser=0",
            "https://lh3.googleusercontent.com/pw/AL9nZEVRSycJA3MHWT4Mk_UniIJAJBJonGUfTwb20hDqEZFPTwa22UWJ-9fgnkMn8Ct_zwz9mzhQ0yC7zrm7u2ZpkRilFhDZ8YlsC4T4kXYg_weZuuz3VJjOs4CB291oeFnWK2Z4mHr4r3SeMH2vKe54WEb2=w960-h540-no?authuser=0",
            "https://lh3.googleusercontent.com/pw/AL9nZEXK-vQUr4rAI75aN-kbfuBzlJtl8g1XAKhhq3_XnxD_dwpaaB1w0Kj9SuxbrWAwzq1HvFmjUrniSvJXkEuB_IUr4OT6u5KcdtplnJw7gx_QMHDBLjKvAXETenQfivBekaHKzhFeREuCNQXBMCW42MDh=w960-h540-no?authuser=0"
        )


//        private val IMAGES = mutableListOf(
//        "https://drive.google.com/drive/folders/1DioLOs7TeEjUXx2Hc1Ouihz-ykJzAJDs?usp=sharing"
//        )
    }


}