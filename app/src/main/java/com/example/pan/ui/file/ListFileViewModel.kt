package com.example.pan.ui.file

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pan.http.IPanService
import com.example.pan.http.PanRepository
import com.example.pan.model.FileInfo

class ListFileViewModel @ViewModelInject constructor(
    private val panRepository: PanRepository
) : ViewModel() {

    val refreshStatus = MutableLiveData(false)
    val dirLiveData = MutableLiveData<String>()
    val fileInfoList: LiveData<PagedList<FileInfo>> = Transformations.switchMap(dirLiveData) {
        LivePagedListBuilder(ListFileDataSourceFactory(panRepository, it), 100).build()
    }

    fun changeDir(dir: String) {
        refreshStatus.postValue(true)
        dirLiveData.postValue(dir)
    }

    fun onBackDir() {
        refreshStatus.postValue(true)
        val value = dirLiveData.value
        if (value?.lastIndexOf("/") == 0) {
            dirLiveData.postValue("/")
        } else {
            dirLiveData.postValue(value?.substring(0, value.lastIndexOf("/")))
        }
    }

    fun initData() {
        refreshStatus.postValue(true)
        dirLiveData.postValue("/")
    }

    fun refresh() {
        fileInfoList.value?.dataSource?.invalidate()
    }

    fun stopRefresh() {
        refreshStatus.postValue(false)
    }
}