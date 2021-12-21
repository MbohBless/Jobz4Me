package com.blesspearl.jobz4me.Interfaces

import com.blesspearl.jobz4me.BuildConfig
import com.blesspearl.jobz4me.Models.Courses
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UdemyApiInterface {

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization:${BuildConfig.UDEMY_API_KEY}",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("api-2.0/courses/")

    fun getCategories(@Query("category") category: String): Call<Courses>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization:${BuildConfig.UDEMY_API_KEY}",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("api-2.0/courses/")
    fun getSearch(@Query("search") search: String): Call<Courses>

    companion object {
        private const val BASE_URL = "https://www.udemy.com"
        fun create(): UdemyApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(UdemyApiInterface::class.java)

        }
    }

}