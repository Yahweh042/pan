package com.example.pan

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pan.http.PanRepository
import com.example.pan.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val panRepository: PanRepository
) : ViewModel() {

    val title = MutableLiveData<String>()

    val userInfo = MutableLiveData<UserInfo>()

    fun setTitle(title: String) {
        this.title.postValue(title)
    }

    @ExperimentalCoroutinesApi
    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            panRepository.getUserInfo().collect {
                userInfo.postValue(it)
            }
        }
    }


}