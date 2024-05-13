package hardroid.pizza_mozzarella.rellarella

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hardroid.pizza_mozzarella.rellarella.decorator.UIDecorator
import hardroid.pizza_mozzarella.rellarella.model_cart.Order
import hardroid.pizza_mozzarella.rellarella.model_cart.OrderActionListener
import hardroid.pizza_mozzarella.rellarella.model_cart.OrderAdapter


class Cart : AppCompatActivity()  {

    private lateinit var OrderRecyclerView: RecyclerView
    private lateinit var CartOrderAdapter: OrderAdapter
    private lateinit var OverallPrice: TextView
    private lateinit var MakeOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_layout)

        OrderRecyclerView = findViewById(R.id.order_recyclerview)
        OverallPrice = findViewById(R.id.overall_price)
        MakeOrder = findViewById(R.id.confirm_order)


        CartOrderAdapter = OrderAdapter(object : OrderActionListener{
            override fun onOrderDeleted(order: Order) {
                val position = orders.getOrderPosition(order)
                if(position != -1){
                    orders.deleteOrder(order)
                    CartOrderAdapter.notifyItemRemoved(position)
                    CalculateOverallPrice()
                }
                else{
                    CartOrderAdapter.notifyDataSetChanged()
                }

            }

            override fun onOrderPlus(order: Order) {
                val position = orders.getOrderPosition(order)
                if(position != -1){
                    val currentValue = orders.getQuantity(order)
                    if(currentValue != -1){
                        orders.setQuantity(order,currentValue+1)
                        //CartOrderAdapter.notifyItemChanged(position)
                        CartOrderAdapter.notifyDataSetChanged()
                        CalculateOverallPrice()
                    }
                }
                else{
                    CartOrderAdapter.notifyDataSetChanged()
                }
            }

            override fun onOrderMinus(order: Order) {
                val position = orders.getOrderPosition(order)
                if(position != -1){
                    val currentValue = orders.getQuantity(order)
                    if(currentValue != -1){
                        val new_quantity = if(currentValue-1<1){
                            1
                        } else {
                            currentValue-1
                        }
                        orders.setQuantity(order,new_quantity)
                        CartOrderAdapter.notifyItemChanged(position)
                        CalculateOverallPrice()
                    }
                    else{
                        CartOrderAdapter.notifyDataSetChanged()
                    }
                }
            }

        })

        OrderRecyclerView.adapter = CartOrderAdapter
        OrderRecyclerView.layoutManager = LinearLayoutManager(this)
        CalculateOverallPrice()
//        LoadData()
        MakeOrder.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
               if(orders.getOrderList().count()>0){
                   val builder = AlertDialog.Builder(this@Cart)
                   builder.setCancelable(true)
                   builder.setMessage("You order for ${OverallPrice.text} was created!")
                   builder.create().show()
                   orders.clearList()
               }
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        gotoMainActivity()
    }

    fun gotoMainActivity(){
        try{
            var intent = Intent(this@Cart, MainActivity::class.java)
            startActivity(intent)
            finish()
        }catch (_:Exception){}
    }

    fun CalculateOverallPrice(){
        val list =  orders.getOrderList() //orders.getOrderList()
        var sum = 0.0
        for (i in 0 until list.count()){
            sum += list[i].final_price * list[i].order_quantity
        }
        OverallPrice.setText(UIDecorator().getTextPrice(sum))
    }







}