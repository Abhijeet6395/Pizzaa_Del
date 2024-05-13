package hardroid.pizza_mozzarella.rellarella.model_cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hardroid.pizza_mozzarella.rellarella.R
import hardroid.pizza_mozzarella.rellarella.databinding.OrderItemBinding
import hardroid.pizza_mozzarella.rellarella.decorator.UIDecorator
import hardroid.pizza_mozzarella.rellarella.orders

interface OrderActionListener{
    fun onOrderDeleted(order: Order)
    fun onOrderPlus(order: Order)
    fun onOrderMinus(order: Order)
}

class OrderAdapter(
    private val orderListener: OrderActionListener
) : RecyclerView.Adapter<OrderAdapter.OrdersViewHolder>(), View.OnClickListener {

    private var PositionGlobal = 0;
    private lateinit var GlobalBinding: OrderItemBinding;
    class OrdersViewHolder(val binding: OrderItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: OrderItemBinding = OrderItemBinding.inflate(inflater, parent, false)
        //listeners
        binding.root.setOnClickListener(this)
        binding.orderDelete.setOnClickListener(this)
        binding.orderPlus.setOnClickListener(this)
        binding.orderMinus.setOnClickListener(this)
        return OrdersViewHolder(binding)
    }



    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val CurrentOrder = orders.getOrderList().get(position)

        with(holder.binding) {
            holder.itemView.tag = CurrentOrder
            orderDelete.tag = CurrentOrder
            orderPlus.tag = CurrentOrder
            orderMinus.tag = CurrentOrder

            orderIngredients.text = CurrentOrder.order_ingredients.joinToString{it.IngredientName}
            orderQuantity.text = CurrentOrder.order_quantity.toString()

            val final_price = CurrentOrder.final_price
            val final_price_without_bonuses = CurrentOrder.final_price_without_bonuses
            val default_price = CurrentOrder.default_price
            val bonuses_applied = CurrentOrder.bonuses_applied

            if(bonuses_applied){
                //style applying for priceOrder
                priceOrder.setBackgroundResource(R.drawable.strike_through)
                priceOrder.text =  UIDecorator().getTextPrice(final_price_without_bonuses)

                //style applying for bonusPrice
                bonusPrice.text = UIDecorator().getTextPrice(final_price)
                bonusPrice.visibility = View.VISIBLE
            }
            else{
                //style applying for priceOrder
                priceOrder.setBackgroundResource(0);
                priceOrder.text =  UIDecorator().getTextPrice(final_price_without_bonuses)

                //style applying for bonusPrice
                bonusPrice.visibility = View.GONE
            }

            if (CurrentOrder.order_photo.isNotBlank()) {
                Glide.with(orderPhoto)
                    .load(CurrentOrder.order_photo)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerInside()
                    .placeholder(R.drawable.ic_default_pizza)
                    .error(R.drawable.ic_default_pizza)
                    .into(orderPhoto)
            }
            else {
                orderPhoto.setImageResource(R.drawable.ic_default_pizza)
            }

            orderMinus.isEnabled = CurrentOrder.order_quantity > 1
        }
    }

    override fun getItemCount(): Int {
       return orders.getOrderList().count()
    }

    override fun onClick(v: View?) {
        val order = v?.tag as Order
        when(v.id) {
            R.id.order_delete -> {
                orderListener.onOrderDeleted(order)
            }
            R.id.order_minus -> {
                orderListener.onOrderMinus(order)
            }
            R.id.order_plus ->{
                orderListener.onOrderPlus(order)
            }
    }
}





}