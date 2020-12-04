package com.example.giftcardsite.api.service

import com.example.giftcardsite.api.model.LoginInfo
import com.example.giftcardsite.api.model.RegisterInfo
import com.example.giftcardsite.api.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface UserInterface {

    @POST("/api/login")
    fun loginUser(@Body loginInfo: LoginInfo): Call<User?>?;

    @POST("/api/register")
    fun registerUser(@Body registerInfo: RegisterInfo): Call<User?>?
}