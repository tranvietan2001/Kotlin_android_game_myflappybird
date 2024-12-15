package com.example.mybird

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val context: Context,
    private val shopList: List<ShopInfor>,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val onBuyClick: (ShopInfor) -> Unit // Callback khi nhấn nút Buy
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageBird: ImageView = itemView.findViewById(R.id.imageBirdShop)
        val coinSilver: TextView = itemView.findViewById(R.id.coinSilverTxt)
        val coinGold: TextView = itemView.findViewById(R.id.coinGoldTxt)
        val buyBtn: ImageButton = itemView.findViewById(R.id.buyShopBtn)

        fun bind(shopInfo: ShopInfor) {
            // Giả sử các hình ảnh nằm trong thư mục drawable
            val resId =
                context.resources.getIdentifier(shopInfo.nameBird, "drawable", context.packageName)
            imageBird.setImageResource(resId)

            coinSilver.text = shopInfo.coinSilver.toString()
            coinGold.text = shopInfo.coinGold.toString()

            // Kiểm tra xem tên đã được mua chưa
            if (sharedPreferenceManager.getPlayerMode() == "offline") {
                if (sharedPreferenceManager.getPurchasedBirds().contains(shopInfo.nameBird)) {
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
                } else {
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
            } else {
                // test list ra ds bird trên firebase
                val firebaseManager: FirebaseManager = FirebaseManager()
                firebaseManager.initFirebase()
                firebaseManager.getListBird { result ->
                    val birdsList: List<String> = result.split(",").map { it.trim() }

                    if (birdsList.contains(shopInfo.nameBird))
                        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
                    else itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
                println("-----> LOAD VIEW SHOP ONLINE")
            }


            buyBtn.setOnClickListener {
                onBuyClick(shopInfo) // Gọi callback khi nút Buy được nhấn
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_shop_main, parent, false)

        return ShopViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
//        holder.nameBird.text = shopList[position].nameBird
//        holder.coinSilver.text = shopList[position].coinSilver.toString()
//        holder.coinGold.text = shopList[position].coinGold.toString()
        holder.bind(shopList[position])
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}