package com.example.giftcardsite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.giftcardsite.api.model.*
import com.example.giftcardsite.api.service.CardInterface

import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Integer.parseInt

class GetCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_card)
        setSupportActionBar(findViewById(R.id.toolbar))
        var image : CircleImageView = findViewById(R.id.image_view)
        val product : Product? = intent.getParcelableExtra("Product")
        findViewById<EditText>(R.id.amount).hint = product?.recommendedPrice.toString()
        Glide.with(this).asBitmap().load("http://appsec.moyix.net/" + product?.productImageLink).into(image)
        val productNumber : Int? = product?.productId
        val loggedInUser : User? = intent.getParcelableExtra("User")
        var token : String = "Token " + loggedInUser?.token.toString()
        Log.d("Token check", token)
        val outerContext = this

        findViewById<Button>(R.id.submit_buy).setOnClickListener{
            val amount : Int = parseInt(findViewById<EditText>(R.id.amount).text.toString())
            var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(GsonConverterFactory.create())
            var retrofit: Retrofit = builder.build()
            var client: CardInterface = retrofit.create(CardInterface::class.java)
            var card: Card? = null
            val buyCardInfo = BuyCardInfo(amount)
            Log.d("Buy Card Going", "Going to buy card now. Amount $amount")
            client.buyCard(productNumber, buyCardInfo, token)?.enqueue(object : Callback<Card?> {
                override fun onFailure(call: Call<Card?>, t: Throwable) {
                    Log.d("Buy Failure", "Buy Failure in onFailure")
                    Log.d("Buy Failure", t.message.toString())

                }

                override fun onResponse(call: Call<Card?>, response: Response<Card?>) {
                    if (!response.isSuccessful){
                        Log.d("Buy Failure", "Buy failure. Yay.")
                    } else {
                        val new_card: Card? = response.body()
                        Log.d("Buy failure?", new_card.toString())
                        Log.d("Buy Success", "Card: " + new_card.toString())
                        Log.d("Buy Success", "Buy success. Boo.")
                        Log.d("Buy Success", "Token:$token")
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