package hardroid.pizza_mozzarella.rellarella.recycler_views_adapters

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hardroid.pizza_mozzarella.rellarella.MainActivity
import hardroid.pizza_mozzarella.rellarella.R
import hardroid.pizza_mozzarella.rellarella.decorator.UIDecorator
import hardroid.pizza_mozzarella.rellarella.model.Ingredient
import hardroid.pizza_mozzarella.rellarella.model.IngredientsActionListener
import hardroid.pizza_mozzarella.rellarella.model.IngredientsAdapter
import hardroid.pizza_mozzarella.rellarella.model.IngredientsList
import hardroid.pizza_mozzarella.rellarella.model.PizzaService
import hardroid.pizza_mozzarella.rellarella.model_cart.Order
import hardroid.pizza_mozzarella.rellarella.orders
import hardroid.pizza_mozzarella.rellarella.prefs

class PizzaSelected : AppCompatActivity() {
    private lateinit var PizzaProfilePhoto: ImageView
    private lateinit var BackButton: Button
    private lateinit var TitlePizza: TextView
    private lateinit var DescriptionPizza: TextView
    private lateinit var IngredientsPizza:TextView
    private lateinit var PizzaPrice:TextView
    private lateinit var AddToCart: Button
    private lateinit var IngredientsButton:ImageView

    private lateinit var IngredientsDialog: Dialog
    private lateinit var IngredientRecyclerView: RecyclerView

    private var IngredientsListWithChanges: MutableList<Ingredient> = mutableListOf()

    private lateinit var IngredientAdapter:  IngredientsAdapter

    private var AllIngredients: MutableList<Ingredient> = IngredientsList().getIngredientsList().toMutableList()



    override fun onCreate(savedInstanceState: Bundle?) {

        val threshold = Ingredient(0,"Threshold",0.0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_pizza_selected)


        PizzaProfilePhoto = findViewById(R.id.pizza_photo_selected)
        BackButton = findViewById(R.id.button_back)
        TitlePizza = findViewById(R.id.title_pizza)
        DescriptionPizza = findViewById(R.id.description_pizza)
        IngredientsPizza = findViewById(R.id.ingredients_description)
        PizzaPrice = findViewById(R.id.final_price)
        AddToCart = findViewById(R.id.add_to_cart)
        IngredientsButton = findViewById(R.id.icon_filter)


        TitlePizza.text = intent.getStringExtra("PIZZA_TITLE").toString()
        DescriptionPizza.text = intent.getStringExtra("PIZZA_DESCRIPTION").toString()

        val Price = intent.getDoubleExtra("PIZZA_PRICE",7.0)
        PizzaPrice.text = "$${UIDecorator().getFormatedDoubleAsPrice(Price)}"


        val bundle = intent.getBundleExtra("data")
        val IngredientsList = bundle?.getParcelableArray("PIZZA_INGREDIENTS")?.map { it as Ingredient }?.toTypedArray()

        var IngredientString: String = ""
        if(IngredientsList != null){
            for(i in 0 until IngredientsList.size){
                IngredientString += "${IngredientsList.get(i).IngredientName} ${ if (i!=IngredientsList.size-1) "\n" else ""}"
            }
        }
        IngredientsPizza.text = IngredientString
        val position = intent.getIntExtra("LAST_POSITION",0)
        PizzaService()

        var current_pizza_photo = intent.getStringExtra("PIZZA_PHOTO")
        Glide.with(PizzaProfilePhoto)
            .load(current_pizza_photo)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .centerCrop()
            .placeholder(R.drawable.ic_default_pizza)
            .error(R.drawable.ic_default_pizza)
            .into(PizzaProfilePhoto)

        BackButton.setOnClickListener{
            onBackPressed()
        }

        //dialog ingredients changes -- start
        //Dialog when a user wants to change ingredients
        IngredientsDialog  = Dialog(this,R.style.Theme_DialogAnimation)//Create new dialog window
        IngredientsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)//hide tittle
        IngredientsDialog.setContentView(R.layout.ingredients_choice)//path to dialog content
        IngredientsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))//Transperent
        //transparent background of the dialog window
        IngredientsDialog.setCancelable(true)//window CAN BE be closed by back button
        IngredientsDialog.window!!.setGravity(Gravity.BOTTOM)
        IngredientsDialog.window!!.attributes.windowAnimations = R.style.Theme_DialogAnimation
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        IngredientsDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,(height.toDouble()*0.7).toInt())

        IngredientRecyclerView = IngredientsDialog.findViewById(R.id.ingredients_list)
        if(IngredientsList!=null){

            IngredientsListWithChanges = IngredientsList.toMutableList()
            IngredientsListWithChanges.add(threshold)
            JoinIngredients()


            IngredientAdapter = IngredientsAdapter(object: IngredientsActionListener{

                override fun onIngredientDeleted(ingredient: Ingredient) {
//                    Toast.makeText(this@PizzaSelected, "you picked: ${ingredient.IngredientName} and button delete",
//                        Toast.LENGTH_LONG).show()

                    //reflect the old price
                    val total = UIDecorator().fromPriceToDouble(PizzaPrice.text.toString())
                    PizzaPrice.text = "$${UIDecorator().getFormatedDoubleAsPrice(total)}"

                    val index = IngredientsListWithChanges.indexOfFirst { it.IngredientID == ingredient.IngredientID }
                    IngredientsListWithChanges.removeAt(index)
                    JoinIngredients()

                    //reflect the new price
                    PizzaPrice.text =  "$${UIDecorator().getFormatedDoubleAsPrice(total - ingredient.IngredientPrice)}"

                    UpdateAdapterList(IngredientsListWithChanges.toList(), index, "remove")

                    if(IngredientsListWithChanges[0].IngredientName.equals("Threshold")){
                        //when the threshold at the top
                        //we need to say that pizza can't be without ingredients and force to reload the list
                        IngredientsListWithChanges.clear()
                        IngredientsListWithChanges = IngredientsList.toMutableList()
                        IngredientsListWithChanges.add(threshold)
                        JoinIngredients()
                        UpdateAdapterList(IngredientsListWithChanges,position,"force to reload")
                        Toast.makeText(this@PizzaSelected, "A pizza should have at least one ingredient except dough!",Toast.LENGTH_LONG).show()
                        PizzaPrice.text = "$${UIDecorator().getFormatedDoubleAsPrice(Price)}"
                    }
                }

                override fun onIngredientAdded(ingredient: Ingredient) {
                    val ThresholdPosition = IngredientsListWithChanges.indexOfFirst { it.IngredientName.equals("Threshold")}
                    IngredientsListWithChanges = IngredientsListWithChanges.subList(0,ThresholdPosition).toMutableList()
                    IngredientsListWithChanges.add(ingredient)
                    IngredientsListWithChanges.add(threshold)
                    JoinIngredients()

                    //reflect the new price
                    val total = UIDecorator().fromPriceToDouble(PizzaPrice.text.toString()) + ingredient.IngredientPrice
                    PizzaPrice.text = "$${UIDecorator().getFormatedDoubleAsPrice(total)}"

                    //update adapter
                    UpdateAdapterList(IngredientsListWithChanges.toList(),ThresholdPosition,"add")
                }
            },IngredientsListWithChanges.toList())
            IngredientRecyclerView.adapter = IngredientAdapter
            IngredientRecyclerView.layoutManager = LinearLayoutManager(IngredientsDialog.context)

        }



        //dialog ingredients changes -- end

        val LayoutChangeIngredients: RelativeLayout = findViewById(R.id.change_ingredients_button)
        LayoutChangeIngredients.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View?) {
                IngredientsDialog.show()
            }
        })


        AddToCart.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val from = 0
                val to = IngredientsListWithChanges.indexOfFirst { it.IngredientName.equals("Threshold") }
                val Pref = prefs.getPref()
                val keySP = prefs.keySpecialPromotion
                var HasDiscount = false
                if (Pref != null) {
                    HasDiscount = Pref.getBoolean(keySP, false)
                }
                val default_price = intent.getDoubleExtra("PIZZA_PRICE",7.0);
                val total: Double = UIDecorator().fromPriceToDouble(PizzaPrice.text.toString())
                val newPrice: Double = if(HasDiscount) total*0.5 else total
                val newOrder = Order(newPrice, total, default_price, HasDiscount, current_pizza_photo.toString(), IngredientsListWithChanges.subList(from,to), 1)
                orders.addNewOrder(newOrder)
                gotoMainActivity()
            }

        })



    }
    override fun onBackPressed() {
        super.onBackPressed()
        gotoMainActivity()
    }

    private fun gotoMainActivity() {
       try{
           val intent = Intent(this@PizzaSelected,MainActivity::class.java)
           val bundle:Bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()

           startActivity(intent,bundle)
           finish()
       }catch (_:Exception){}
    }

    private fun UpdateAdapterList(newList: List<Ingredient>, position: Int, type: String) {
        if(IngredientAdapter!=null){
            IngredientAdapter.UpdateListElements(newList)
            when(type){
                "remove" ->{
                    IngredientAdapter.notifyItemRemoved(position)
                }
                "add"->{
                    IngredientAdapter.notifyDataSetChanged()
                }
                "force to reload"->{
                    IngredientAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun JoinIngredients(){
        for(i in 0 until AllIngredients.size){
            if(!IngredientsListWithChanges.contains(AllIngredients[i])){
                //if the current list doesn't contain the current AllIngredient item
                IngredientsListWithChanges.add(AllIngredients[i])
            }
        }
    }






}


