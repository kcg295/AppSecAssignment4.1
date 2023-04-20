package com.example.giftcardsite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.giftcardsite.api.model.RegisterInfo
import com.example.giftcardsite.api.model.User
import com.example.giftcardsite.api.service.UserInterface
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private final val jsonType = "application/json; charset=utf-8".toMediaType()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_button_submit).setOnClickListener {
            var username : String = view.findViewById<EditText>(R.id.username).text.toString()
            var email : String = view.findViewById<EditText>(R.id.registerEmailAddress).text.toString()
            var password : String = view.findViewById<EditText>(R.id.registerPassword).text.toString()
            var password2 : String = view.findViewById<EditText>(R.id.registerConfirmPassword).text.toString()

            var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(GsonConverterFactory.create())
            var retrofit: Retrofit = builder.build()
            var client: UserInterface = retrofit.create(UserInterface::class.java)
            var loggedInUser: User? = null;
            val registerInfo = RegisterInfo(username, email, password, password2)
            Log.d("Register Going", "Going to register now.")
            client.registerUser(registerInfo)?.enqueue(object : Callback<User?> {
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d("Register Failure", "Register Failure in onFailure")
                    Log.d("Register Failure", t.message.toString())

                }

                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (!response.isSuccessful){
                        Log.d("Register Failure", "Register failure. Yay.")
                        Toast.makeText(activity, "Register Failed", Toast.LENGTH_LONG).show()
                    } else {
                        loggedInUser = response.body()
                        Log.d("Register Success", "Register success. Boo.")
                        Log.d("Register Success", "Token:" + loggedInUser?.token.toString())
                        var intent = Intent(Intent.ACTION_VIEW)
                        intent.type = "text/giftcards_browse"
                        intent.data = Uri.parse("https://appsec.moyix.net/api/index")
                        intent.putExtra("User", loggedInUser);
                        startActivity(intent)
                    }
                }
            })

        }
    }
}