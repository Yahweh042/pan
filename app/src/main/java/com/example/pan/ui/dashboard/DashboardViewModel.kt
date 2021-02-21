package com.example.pan.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pan.http.IPanService
import com.example.pan.model.FileInfo

class DashboardViewModel @ViewModelInject constructor(
    private val panService: IPanService
) : ViewModel() {

    val dirLiveData = MutableLiveData<String>()
    val fileInfoList: LiveData<PagedList<FileInfo>> = Transformations.switchMap(dirLiveData) {
        LivePagedListBuilder(ListFileDataSourceFactory(panService, it), 20).build()
    }

    fun changeDir(dir: String) {
        dirLiveData.postValue(dir)
    }

}