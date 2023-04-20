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
import com.example.giftcardsite.api.model.LoginInfo
import com.example.giftcardsite.api.model.User
import com.example.giftcardsite.api.service.UserInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_button_submit).setOnClickListener {
            // Send JSON of values to server.
            // If auth successful, move to new activity: logged in activity.
            var username : String = view.findViewById<EditText>(R.id.username).text.toString()
            var password : String = view.findViewById<EditText>(R.id.registerPassword).text.toString()

            var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(
                GsonConverterFactory.create())
            var retrofit: Retrofit = builder.build()
            var client: UserInterface = retrofit.create(UserInterface::class.java)
            var loggedInUser: User? = null;
            var loginInfo = LoginInfo(username, password)
            client.loginUser(loginInfo)?.enqueue(object :
                Callback<User?> {
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d("Login Failure", "Login Failure in onFailure")
                    Log.d("Login Failure", t.message.toString())

                }

                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (!response.isSuccessful) {
                        Log.d("Login Failure", "Login failure. Yay.")
                        Toast.makeText(activity, "Login Failed", Toast.LENGTH_LONG).show()
                    } else {
                        loggedInUser = response.body()
                        Log.d("Login Success", "Login success. Boo.")
                        Log.d("Login Success", "Token:" + loggedInUser?.token.toString())
                        var intent = Intent(activity, ProductScrollingActivity::class.java)
                        intent.putExtra("User", loggedInUser);
                        startActivity(intent)
                    }
                }
            })
        }
    }
}