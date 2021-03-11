package com.example.pan.ui.file

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pan.aria2.Aria2Repository
import com.example.pan.http.PanRepository
import com.example.pan.model.FileInfo
import com.example.pan.model.FileMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ListFileViewModel @ViewModelInject constructor(
    private val panRepository: PanRepository,
    private val aria2Repository: Aria2Repository
) : ViewModel() {

    val refreshStatus = MutableLiveData(false)
    val dirLiveData = MutableLiveData<String>()
    val fileInfoList: LiveData<PagedList<FileInfo>> = Transformations.switchMap(dirLiveData) {
        LivePagedListBuilder(ListFileDataSourceFactory(panRepository, it), 100).build()
    }
    val filemeta = MutableLiveData<FileMeta>()

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

    @ExperimentalCoroutinesApi
    fun filemetas(fs_id: Long) {
        viewModelScope.launch {
            panRepository.download(fs_id).collect {
                filemeta.postValue(it)
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun download(url: String, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            aria2Repository.addUri(url, fileName).onStart {
                Log.e("addUri", "下载开始")
            }.catch {
                it.message?.let { it1 -> Log.e("addUri", it1) }
            }.collect {

            }
        }
    }

}