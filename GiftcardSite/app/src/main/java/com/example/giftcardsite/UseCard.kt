package com.example.giftcardsite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.giftcardsite.R
import com.example.giftcardsite.api.model.BuyCardInfo
import com.example.giftcardsite.api.model.Card
import com.example.giftcardsite.api.model.Product
import com.example.giftcardsite.api.model.User
import com.example.giftcardsite.api.service.CardInterface
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UseCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_card)
        setSupportActionBar(findViewById(R.id.toolbar))
        var image : CircleImageView = findViewById(R.id.image_view)
        val card : Card? = intent.getParcelableExtra("Card")
        findViewById<EditText>(R.id.amount).setText(card?.amount.toString())
        Glide.with(this).asBitmap().load("http://appsec.moyix.net/" + card?.product?.productImageLink).into(image)
        val loggedInUser : User? = intent.getParcelableExtra("User")
        var token : String = "Token " + loggedInUser?.token.toString()
        Log.d("Token check", token)
        val outerContext = this
        var button: Button = findViewById(R.id.submit_buy)
        button.text = "Use Card"
        button.setOnClickListener{
            var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(
                GsonConverterFactory.create())
            var retrofit: Retrofit = builder.build()
            var client: CardInterface = retrofit.create(CardInterface::class.java)
            Log.d("Use Card Going", "Going to use card now.")
            client.useCard(card?.id, token)?.enqueue(object : Callback<Card?> {
                override fun onFailure(call: Call<Card?>, t: Throwable) {
                    Log.d("Use Failure", "Use Failure in onFailure")
                    Log.d("Use Failure", "Card: ${card.toString()}")
                    Log.d("Use Failure", t.message.toString())

                }

                override fun onResponse(call: Call<Card?>, response: Response<Card?>) {
                    if (!response.isSuccessful){
                        Log.d("Use Failure", "Use failure. Yay.")
                    } else {
                        Log.d("Use Success", "Use success. Boo.")
                        Log.d("Use Success", "Card: ${card.toString()}")
                        Log.d("Use Success", "Token:$token")
                    }
                    var intent = Intent(outerContext, ProductScrollingActivity::class.java)
                    //intent.type = "text/giftcards"
                    intent.putExtra("User", loggedInUser);
                    startActivity(intent)
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
