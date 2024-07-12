package com.example.ggnews.Utils

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
//解析返回的数据,然后根据泛型T返回数据
@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    @Json(name = "code") val code: Int,
    @Json(name = "newslist") val `data`: T,
    @Json(name = "msg") val msg: String
)