package com.example.giftcardsite.api.service

import com.example.giftcardsite.api.model.Product
import retrofit2.Call
import retrofit2.http.GET

public interface ProductInterface {
    @GET("/api/index")
    fun getAllProducts(): Call<List<Product?>?>?;

}