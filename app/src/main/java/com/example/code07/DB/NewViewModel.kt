package com.example.code07.DB

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// 定义一个NewViewModel类，继承自ViewModel
class NewViewModel(private val repository: NewRepository) : ViewModel(){
    // 定义一个MutableLiveData，用于存储新闻列表
    private val newsList=  MutableLiveData<List<New>>()
    // 获取新闻列表的函数
    fun getNewsList(): MutableLiveData<List<New>> {
        return newsList
    }

    // 删除数据的函数，使用协程在IO线程中执行
    fun delData()=runBlocking {
        launch(Dispatchers.IO) {   MyDatabase.clearData()}
    }

    // 插入新的新闻数据的函数，使用协程在IO线程中执行
    fun insert(new: New) = runBlocking{
        launch(Dispatchers.IO) {
            repository.insertNew(new)
        }
    }

    // 获取所有新闻数据的函数，使用viewModelScope.launch在IO线程中执行
    // 并将获取到的数据post到newsList中
    fun getAll()  =  viewModelScope.launch(Dispatchers.IO){
        newsList.postValue(repository.getAllNew()!!)
    }

}