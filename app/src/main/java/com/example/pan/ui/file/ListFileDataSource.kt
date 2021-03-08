package com.example.pan.ui.file

import androidx.paging.PageKeyedDataSource
import com.example.pan.http.PanRepository
import com.example.pan.model.FileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListFileDataSource(
    private val panRepository: PanRepository,
    private val dir: String
) : PageKeyedDataSource<Int, FileInfo>() {

    @ExperimentalCoroutinesApi
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FileInfo>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            panRepository.list(dir, "time", "0", 0, params.requestedLoadSize).collect {
                if (it.errno == 0) {
                    callback.onResult(it.list, null, params.requestedLoadSize)
                } else {
                    callback.onResult(arrayListOf(), null, params.requestedLoadSize)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {

    }

    @ExperimentalCoroutinesApi
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {
        GlobalScope.launch(Dispatchers.IO) {
            panRepository.list(dir, "time", "0", params.key, params.requestedLoadSize)
                .collect {
                    if (it.errno == 0) {
                        callback.onResult(it.list, params.key + params.requestedLoadSize)
                    } else {
                        callback.onResult(arrayListOf(), params.key + params.requestedLoadSize)
                    }
                }

        }
    }
}