package com.example.giftcardsite.api.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftcardsite.GetCard
import com.example.giftcardsite.R
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerViewAdapter(val context: Context, private val productList: List<Product?>?, private val user: User?) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(product: Product?) {
            val image : CircleImageView = itemView.findViewById(R.id.image_view)
            val text : TextView = itemView.findViewById(R.id.name)
            if (product != null) {
                Glide.with(context).asBitmap().load("http://appsec.moyix.net/" + product.productImageLink).into(image)
            }
            if (product != null) {
                text.text = product.productName
            }

            image.setOnClickListener {
                if (product != null) {
                    val localProduct = product
                    val localUser = user
                    val intent = Intent(context, GetCard::class.java).apply{
                        putExtra("User", localUser)
                        putExtra("Product", localProduct)
                    }
                    Log.d("Intent", "About to intent.")
                    context.startActivity(intent)
                } else {
                    Log.d("Clicked something", "but it was null")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList?.get(position)
        holder.setData(product)
    }

    override fun getItemCount(): Int {
        if (productList == null)
            return 0
        return productList.size
    }

}
