package com.example.ggnews.NewsFiles

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class News(
    @Json(name = "ctime") val ctime: String,
    @Json(name = "description") val description: String,
    @Json(name = "id") val id: String,
    @Json(name = "picUrl") val picUrl: String,
    @Json(name = "source") val source: String,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String
)