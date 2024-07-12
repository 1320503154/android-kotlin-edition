package com.example.ggnews.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ggnews.Constant.Constants
import com.example.ggnews.NewsFiles.News
import com.example.ggnews.Utils.ApiClient
import com.example.ggnews.Utils.ApiService
import com.example.ggnews.Utils.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewViewModel : ViewModel() {
    //创建一个可变的LiveData News列表,当数据发生变化时,会通知所有观察者,同步UI更新
    private val newsList = MutableLiveData<List<News>>()

    fun getNewsList(): MutableLiveData<List<News>> {
        return newsList
    }

    fun refreshData(page: Int) = viewModelScope.launch {
        try{
            val response = withContext(Dispatchers.IO) {
                val map: MutableMap<String, String> = HashMap()
                map["key"] = Constants.API_KEY
                map["num"] = Constants.NEWS_NUM.toString()
                map["page"] = page.toString()
                ApiClient.retrofit.create(ApiService::class.java).getNews(map)
            }
            Log.d("NewViewModel", "收到响应: ${response.isSuccessful}")
            if (response.isSuccessful) {
                val baseResponse: BaseResponse<List<News>>? = response.body()
                Log.d("NewViewModel", "基础响应: $baseResponse")
                if (baseResponse != null) {
                    newsList.value = baseResponse.data
                    Log.d("NewViewModel", "更新新闻列表，共 ${baseResponse.data.size} 条新闻")
                } else {
                    Log.e("NewViewModel", "基础响应为空")
                }
            } else {
                Log.e("NewViewModel", "响应失败: ${response.errorBody()?.string()}")
            }
        }catch(e: Exception){
            Log.e("NewViewModel", "获取数据时出错", e)
        }
    }
}