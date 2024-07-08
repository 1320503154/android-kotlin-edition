package com.example.code07.DB

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewViewModel(private val repository: NewRepository) : ViewModel() {
    private val newsList = MutableLiveData<List<New>>()

    fun getNewsList(): LiveData<List<New>> {
        return newsList
    }

    fun delData() = viewModelScope.launch(Dispatchers.IO) {
        MyDatabase.clearData()
    }

    fun insert(new: New) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNew(new)
    }

    fun getAll() = viewModelScope.launch(Dispatchers.IO) {
        val allNews = repository.getAllNew()
        newsList.postValue(allNews ?: emptyList())
    }
}

class NewViewModelFactory(private val repository: NewRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
