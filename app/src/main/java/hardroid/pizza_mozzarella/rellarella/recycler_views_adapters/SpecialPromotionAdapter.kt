package hardroid.pizza_mozzarella.rellarella.recycler_views_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hardroid.pizza_mozzarella.rellarella.PromotionClick
import hardroid.pizza_mozzarella.rellarella.R
import hardroid.pizza_mozzarella.rellarella.model.SpecialPromotionModel


class SpecialPromotionAdapter(private val context: Context, private val PromotionsList: List<SpecialPromotionModel>,
                              var promotionClick: PromotionClick, private val left:ImageView, private val right:ImageView
): RecyclerView.Adapter<SpecialPromotionAdapter.SpecialPromotionViewHolder>(){




    inner class SpecialPromotionViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var PromotionPhoto: ImageView
        init {
            super.itemView
            PromotionPhoto = itemView.findViewById(R.id.special_promotion_photo)
            itemView.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    val position = adapterPosition
                    val promotion = PromotionsList[position]
                    if(position != RecyclerView.NO_POSITION){
                        promotionClick.onClick(promotion,position)
                }
            }
            })
            left.setOnClickListener(View.OnClickListener { view ->
                val position = if(absoluteAdapterPosition < 0) getCurrentPosition()-1 else absoluteAdapterPosition
                val count = PromotionsList.size - 1
                val desirePosition = if (position-1 < 0) count else position-1;

                (itemView.parent as? RecyclerView)?.scrollToPosition(desirePosition)
                notifyDataSetChanged()
            })

            right.setOnClickListener(View.OnClickListener { view ->
                val count = PromotionsList.size - 1
                val position = if(absoluteAdapterPosition < 0) getCurrentPosition()-1 else absoluteAdapterPosition
                val desirePosition = if (position+1 > count) 0 else position+1
                (itemView.parent as? RecyclerView)?.scrollToPosition(desirePosition)
                notifyDataSetChanged()
            })

        }
        fun getCurrentPosition(): Int {
            return CurrentPosition
        }

        fun bind(position: Int){
            CurrentPosition = position
        }
    }

    private var CurrentPosition: Int = 0

    override fun getItemCount(): Int {
        return  PromotionsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialPromotionViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_special_promotion, parent, false)
        return  SpecialPromotionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecialPromotionViewHolder, position: Int) {
        holder.bind(position)
        val CurrentPromotion = PromotionsList[position]
        if (CurrentPromotion.photo.isNotBlank()) {
            Glide.with(holder.PromotionPhoto.context)
                .load(CurrentPromotion.photo)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()
                .placeholder(R.drawable.sale_icon)
                .error(R.drawable.sale_icon)
                .into(holder.PromotionPhoto)
        }else{
            holder.PromotionPhoto.setImageResource(R.drawable.sale_icon)
        }

//        left.setOnClickListener(View.OnClickListener { view ->
//            val var1:Int = holder.absoluteAdapterPosition
//            val var2:Int = holder.position
//            val var3:Int = holder.bindingAdapterPosition
//            val var4:Int = holder.layoutPosition
//            val var5:Int = holder.oldPosition
//            val var6:Int = holder.adapterPosition
//            val var7:Int  = holder.getCurrentPosition()
//        })
//
//        right.setOnClickListener(View.OnClickListener { view ->
//            val var1:Int = holder.absoluteAdapterPosition
//            val var2:Int = holder.position
//            val var3:Int = holder.bindingAdapterPosition
//            val var4:Int = holder.layoutPosition
//            val var5:Int = holder.oldPosition
//            val var6:Int = holder.adapterPosition
//            val var7:Int  = holder.getCurrentPosition()
//        })

        //notifyDataSetChanged()
        holder.setIsRecyclable(false)
    }






}