package com.example.code07.DB

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewViewModel(private val repository: NewRepository) : ViewModel(){
    private val newsList=  MutableLiveData<List<New>>()
    fun getNewsList(): MutableLiveData<List<New>> {
        return newsList
    }

    fun delData()=runBlocking {
        launch(Dispatchers.IO) {   MyDatabase.clearData()}
    }

    fun insert(new: New) = runBlocking{
        launch(Dispatchers.IO) {
            repository.insertNew(new)
        }
    }

    fun getAll()  =  viewModelScope.launch(Dispatchers.IO){
        newsList.postValue(repository.getAllNew()!!)
    }

}
