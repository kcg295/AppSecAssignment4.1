package com.example.giftcardsite.api.model

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giftcardsite.R
import com.example.giftcardsite.UseCard
import de.hdodenhof.circleimageview.CircleImageView

class CardRecyclerViewAdapter(val context: Context, private val cardList: List<Card?>?, private val user: User?) : RecyclerView.Adapter<CardRecyclerViewAdapter.CardViewHolder>() {
    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(card: Card?) {
            val image : CircleImageView = itemView.findViewById(R.id.image_view)
            val text : TextView = itemView.findViewById(R.id.name)
            if (card != null) {
                Glide.with(context).asBitmap().load("http://appsec.moyix.net/" + card.product?.productImageLink).into(image)
            }
            if (card != null) {
                text.text = card.amount.toString()
            }

            image.setOnClickListener {
                if (card != null) {
                    val localUser = user
                    val intent = Intent(context, UseCard::class.java).apply{
                        putExtra("User", localUser)
                        putExtra("Card", card)
                    }
                    Log.d("Intent", "About to intent.")
                    context.startActivity(intent)
                } else {
                    Log.d("Clicked something", "but it was null")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (cardList == null)
            return 0
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardRecyclerViewAdapter.CardViewHolder, position: Int) {
        val card = cardList?.get(position)
        holder.setData(card)
    }
}
