package hardroid.pizza_mozzarella.rellarella.model_cart

import hardroid.pizza_mozzarella.rellarella.db_context.DbContext

class OrderList() {
    private var OrderList: MutableList<Order> = mutableListOf()

    fun setOrderList(newOrderList: List<Order>){
        OrderList = newOrderList.toMutableList()
    }

    fun clearList(){
        OrderList.clear()
        DbContext().deleteAllOrdersFromDB()
    }


    fun getOrderList():List<Order>{
        return OrderList.toList()
    }

    fun getOrderListFromDB():List<Order>{
        OrderList = DbContext().getOrderListFromDB().toMutableList()
        return OrderList.toList()
    }

    fun saveOrdersToDB(){
        DbContext().saveOrderListToDB(OrderList)
    }


    fun addNewOrder(order: Order){
        OrderList.add(order)
        DbContext().saveOrderListToDB(OrderList)
    }

    fun deleteOrder(order: Order){
        val indexToDelete: Int = getIndexOfOrder(order)
        if (indexToDelete != -1){
            OrderList.removeAt(indexToDelete)
            DbContext().deleteItemFromDB(order)
        }
    }

    fun setNewPrice(order: Order, new_price: Double){
        val indexToUpdate: Int = getIndexOfOrder(order)
        if(indexToUpdate != -1){
            OrderList.get(indexToUpdate).final_price = new_price
        }
    }

    fun setQuantity(order: Order, quantity: Int){
        val indexQuantity: Int = getIndexOfOrder(order)
        if(indexQuantity != -1){
            OrderList.get(indexQuantity).order_quantity = quantity
        }
    }

    fun getQuantity(order: Order):Int{
        val index = getIndexOfOrder(order)
        if(index != -1){
            return OrderList.get(index).order_quantity
        }else{
            return  -1
        }
    }

    fun getOrderPosition(order: Order): Int{
         return getIndexOfOrder(order)
    }

    private fun getIndexOfOrder(order:Order): Int{
        val index: Int = OrderList.indexOfFirst {
            it.order_photo == order.order_photo &&
                    it.final_price == order.final_price &&
                    it.order_quantity == order.order_quantity &&
                    it.final_price_without_bonuses == order.final_price_without_bonuses &&
                    it.order_ingredients == order.order_ingredients &&
                    it.default_price == it.default_price}
        return  index
    }
}