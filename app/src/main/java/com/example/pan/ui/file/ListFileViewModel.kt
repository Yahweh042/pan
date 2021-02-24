package com.example.pan.ui.file

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pan.http.IPanService
import com.example.pan.model.FileInfo

class ListFileViewModel @ViewModelInject constructor(
    private val panService: IPanService
) : ViewModel() {

    val dirLiveData = MutableLiveData<String>()
    val fileInfoList: LiveData<PagedList<FileInfo>> = Transformations.switchMap(dirLiveData) {
        LivePagedListBuilder(ListFileDataSourceFactory(panService, it), 100).build()
    }

    fun changeDir(dir: String) {
        dirLiveData.postValue(dir)
    }

    fun onBackDir() {
        val value = dirLiveData.value
        dirLiveData.postValue(value?.substring(value.lastIndexOf("/")))
    }

    fun initData() {
        dirLiveData.postValue("/")
    }

    fun refresh() {
        fileInfoList.value?.dataSource?.invalidate()
    }
}