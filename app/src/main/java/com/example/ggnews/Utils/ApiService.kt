package com.example.ggnews.Utils

import com.example.ggnews.Constant.Constants
import com.example.ggnews.NewsFiles.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET(Constants.GENERAL_NEWS_PATH)
    suspend fun getNews(@QueryMap map: Map<String, String>): Response<BaseResponse<List<News>>>
}