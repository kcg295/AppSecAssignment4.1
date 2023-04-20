package com.example.giftcardsite

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftcardsite.api.model.*
import com.example.giftcardsite.api.service.ProductInterface
import com.example.giftcardsite.api.service.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductScrollingActivity : AppCompatActivity(), SensorEventListener, LocationListener {
    var loggedInUser: User? = null
    private lateinit var sensorManager: SensorManager
    private var mAccel : Sensor? = null
    private var lastEvent : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationPermissionCode = 2
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        loggedInUser = intent.getParcelableExtra<User>("User")
        findViewById<Button>(R.id.view_cards_button).setOnClickListener{
            val intent = Intent(this, CardScrollingActivity::class.java).apply {
                putExtra("User", loggedInUser)
            }
            startActivity(intent)
        }
        //var productList: List<Product?>? = null
        var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(
                GsonConverterFactory.create())
        var retrofit: Retrofit = builder.build()
        var client: ProductInterface = retrofit.create(ProductInterface::class.java)
        val outerContext = this
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = manager
        client.getAllProducts()?.enqueue(object :
                Callback<List<Product?>?> {
            override fun onFailure(call: Call<List<Product?>?>, t: Throwable) {
                Log.d("Product Failure", "Product Failure in onFailure")
                Log.d("Product Failure", t.message.toString())

            }

            override fun onResponse(call: Call<List<Product?>?>, response: Response<List<Product?>?>) {
                if (!response.isSuccessful) {
                    Log.d("Product Failure", "Product failure. Yay.")
                }
                var productListInternal = response.body()
                Log.d("Product Success", "Product Success. Boo.")
                if (productListInternal == null) {
                    Log.d("Product Failure", "Parsed null product list")
                    Log.d("Product Failure", response.toString())
                } else {
                    recyclerView.adapter = RecyclerViewAdapter(outerContext, productListInternal, loggedInUser)
                }
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onLocationChanged(location: Location) {
        var userInfoContainer = UserInfoContainer(location, null, loggedInUser?.token)
        var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(
            GsonConverterFactory.create())
        var retrofit: Retrofit = builder.build()
        var client: UserInfo = retrofit.create(UserInfo::class.java)

        client.postInfo(userInfoContainer, loggedInUser?.token)?.enqueue(object: Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("Metric Failure", "Metric Failure in onFailure")
                Log.d("Metric Failure", t.message.toString())

            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (!response.isSuccessful) {
                    Log.d("Metric Failure", "Metric failure. Yay.")
                } else {
                    Log.d("Metric Success", "Metric success. Boo.")
                    Log.d("Metric Success", "Token:${userInfoContainer.token}")
                }
            }
        })
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            var userInfoContainer = UserInfoContainer(null, event.values[0].toString(), loggedInUser?.token)
            var builder: Retrofit.Builder = Retrofit.Builder().baseUrl("http://appsec.moyix.net").addConverterFactory(
                GsonConverterFactory.create())
            var retrofit: Retrofit = builder.build()
            var client: UserInfo = retrofit.create(UserInfo::class.java)
            if (lastEvent == null) {
                lastEvent = event.values[0].toString()
            } else if (lastEvent == event.values[0].toString()) {
                return
            }
            client.postInfo(userInfoContainer, loggedInUser?.token)?.enqueue(object: Callback<User?> {
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.d("Metric Failure", "Metric Failure in onFailure")
                    Log.d("Metric Failure", t.message.toString())

                }

                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (!response.isSuccessful) {
                        Log.d("Metric Failure", "Metric failure. Yay.")
                    } else {
                        Log.d("Metric Success", "Metric success. Boo.")
                        Log.d("Metric Success", "Token:${userInfoContainer.token}")
                    }
                }
            })
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        mAccel?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}
