package hardroid.pizza_mozzarella.rellarella

import android.app.ActivityOptions
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.joanzapata.iconify.widget.IconButton
import hardroid.pizza_mozzarella.rellarella.model.PizzaModel
import hardroid.pizza_mozzarella.rellarella.model.PizzaService
import hardroid.pizza_mozzarella.rellarella.model.SpecialPromotionModel
import hardroid.pizza_mozzarella.rellarella.model.SpecialPromotionService
import hardroid.pizza_mozzarella.rellarella.recycler_views_adapters.PizzaSelected
import hardroid.pizza_mozzarella.rellarella.recycler_views_adapters.RecyclerViewInterface


interface PromotionClick{
    fun onClick(promotion: SpecialPromotionModel, position: Int)


}



class MainActivity : AppCompatActivity() , RecyclerViewInterface {

    lateinit var service: ConnectivityManager
//    lateinit var CartButton: ImageView
//    lateinit var OrderQuantity: TextView

    private var PizzaModels: List<PizzaModel> = listOf()
    val PizzaService: PizzaService = PizzaService()

    val SpecialPromotionService = SpecialPromotionService()



    private lateinit var BottomNavigationMenu: BottomNavigationView

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var itemMessages = menu?.findItem(R.id.cart)
        var layout = itemMessages?.actionView
        var textview = layout?.findViewById<TextView>(R.id.cart_quantity)
        textview?.text = orders.getOrderList().count().toString()

        var icon = layout?.findViewById<IconButton>(R.id.cart_icon)
        icon?.setText(orders.getOrderList().count().toString())
        icon?.text = ""

        icon?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                gotoOrders()
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cart -> {
                gotoOrders()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //BackImage = findViewById(R.id.backimage)

//        CartButton = findViewById(R.id.add_to_cart)
//        OrderQuantity = findViewById(R.id.cart_quantity)

//        OrderQuantity.text = orders.getOrderList().count().toString()

        BottomNavigationMenu = findViewById(R.id.bottom_menu)
        //try to update bottom menu
        val cartItem: MenuItem = BottomNavigationMenu.menu.findItem(R.id.cart)
        cartItem.title = orders.getOrderList().count().toString()


        BottomNavigationMenu.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cart -> {
                    gotoOrders()
                    true
                }

                R.id.account -> {
                    gotoAccount()
                    true
                }

                else -> false
            }
        }



        setUpPizzaModels()

        var RecyclerView: RecyclerView = findViewById(R.id.recycler_view)

        var PizzaAdapter = PizzasAdapter(this, PizzaModels, this)

        RecyclerView.adapter = PizzaAdapter

        RecyclerView.layoutManager = LinearLayoutManager(this)


        //check is last position suits to recyclerview position
        val last_position_pizza: Int =
            if (App.AppLastPositionRecyclers.getLastPosition() != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                App.AppLastPositionRecyclers.getLastPosition()
            } else {
                0
            }
        RecyclerView.scrollToPosition(last_position_pizza)

    }


    private fun gotoOrders() {
        try {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
            finish()
        } catch (ex: Exception) {
            print(ex.message)
        }
    }

    private fun gotoAccount() {
        try {
            var intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        } catch (ex: Exception) {
            print(ex.message)
        }
    }


    private fun setUpPizzaModels() {
        PizzaModels = PizzaService.getPizza()
    }

    override fun OnItemClick(position: Int) {
        App.AppLastPositionRecyclers.setLastPosition(position)
        try {
            val intent = Intent(this@MainActivity, PizzaSelected::class.java)
            intent.putExtra("PIZZA_PHOTO", PizzaModels.get(position).photo)
            intent.putExtra("PIZZA_TITLE", PizzaModels.get(position).name)
            intent.putExtra("PIZZA_DESCRIPTION", PizzaModels.get(position).description)
            intent.putExtra("PIZZA_PRICE", PizzaModels.get(position).price)

            var bundle_dunble: Bundle = Bundle();
            bundle_dunble.putParcelableArray(
                "PIZZA_INGREDIENTS",
                PizzaModels.get(position).original_ingredients.toTypedArray()
            )

            intent.putExtra("data", bundle_dunble)
            val bundle: Bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, bundle)
            finish()
        } catch (_: Exception) {
        }
    }


    private var PromotionAlreadySeen = false

    private fun LoadData() {
        val Pref = prefs.getPref()
        val keySP = prefs.keySpecialPromotion
        if (Pref != null) {
            PromotionAlreadySeen = Pref.getBoolean(keySP, false)
        }
    }


    private fun IsOnline(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = service.getNetworkCapabilities(service.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }

                    else ->
                        return false

                }
            }
        } else {
            val activeNetworkInfo = service.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}


