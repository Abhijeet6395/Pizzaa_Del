package hardroid.pizza_mozzarella.rellarella.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hardroid.pizza_mozzarella.rellarella.R
import hardroid.pizza_mozzarella.rellarella.databinding.IngredientItemBinding


interface IngredientsActionListener{
    fun onIngredientAdded(ingredient: Ingredient)
    fun onIngredientDeleted(ingredient: Ingredient)
}

class IngredientsAdapter(
    private val ingredientsListener:IngredientsActionListener,
    private var IngredientsList: List<Ingredient> )
    : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>(),
    View.OnClickListener {


    fun UpdateListElements(newListItems: List<Ingredient>) {
        this.IngredientsList =  newListItems
    }


    private var RemainIngredients: MutableList<Ingredient> = mutableListOf()


    override fun onClick(v: View) {
        val ingredient = v.tag as Ingredient
        when(v.id){
            R.id.delete_ingredient ->{
                //when user clicked delete
                ingredientsListener.onIngredientDeleted(ingredient)
            }
            R.id.add_ingredient ->{
                //when the user want to add the ingredient
                ingredientsListener.onIngredientAdded(ingredient)
            }
            else -> {
                //else ids
            }
        }
    }

    class IngredientsViewHolder(val binding: IngredientItemBinding):RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: IngredientItemBinding = IngredientItemBinding.inflate(inflater, parent, false)
        //listeners
        binding.root.setOnClickListener(this)
        binding.deleteIngredient.setOnClickListener(this)
        binding.addIngredient.setOnClickListener(this)

        return  IngredientsViewHolder(binding)
    }





    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val CurrentIngredient = IngredientsList[position]
        with(holder.binding){
            holder.itemView.tag = CurrentIngredient
            deleteIngredient.tag = CurrentIngredient
            addIngredient.tag = CurrentIngredient
            ingredient.text =  "name: ${CurrentIngredient.IngredientName}\ncost: $${CurrentIngredient.IngredientPrice}"

            //threshold active or passive?
            if(CurrentIngredient.IngredientName.equals("Threshold")){
                separationLayout.visibility = View.VISIBLE
                contentLayout.visibility = View.GONE

            }
            else{
                separationLayout.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
            }

            //change from delete to add and vice-versa
            var ThresholdPosition = IngredientsList.indexOfFirst { it.IngredientName.equals("Threshold") }


            if(position>ThresholdPosition){
                deleteIngredient.visibility = View.GONE
                addIngredient.visibility = View.VISIBLE
            }
            else{
                deleteIngredient.visibility = View.VISIBLE
                addIngredient.visibility = View.GONE
            }

        }


    }

    override fun getItemCount(): Int {
       return  IngredientsList.size
    }

    private fun FillRemainIngredients(){
        val AllIngredients = IngredientsList().getIngredientsList()
        for(i in 0 until AllIngredients.size){
            if(!IngredientsList.contains(AllIngredients[i])){
                //if the current list doesn't contain the current AllIngredient item
                RemainIngredients.add(AllIngredients[i])
            }
        }
    }



}