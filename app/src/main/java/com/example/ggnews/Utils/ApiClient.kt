package com.example.ggnews.Utils

import com.example.ggnews.Constant.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    //创建moshi对象,用于解析json数据,并且添加kotlin适配器,用于解析kotlin数据
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    //创建retrofit对象,用于网络请求,并且添加moshi转换器,用于解析json数据
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.SERVER_URL) // Set network request BaseUrl address
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}