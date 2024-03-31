package kh.ad.appgaintask.core.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Credentials.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = retrofitBuilder.build()
    val movieApi: MovieApi = retrofit.create(MovieApi::class.java)
}