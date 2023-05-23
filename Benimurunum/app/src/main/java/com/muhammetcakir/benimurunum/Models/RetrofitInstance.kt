package com.muhammetcakir.benimurunum.Models



import com.muhammetcakir.benimurunum.Constans.Constans.Companion.BASE_URL
import com.muhammetcakir.benimurunum.`interface`.NotificationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }

        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}