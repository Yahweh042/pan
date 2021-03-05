package com.example.pan.ui.file

import androidx.paging.PageKeyedDataSource
import com.example.pan.http.PanRepository
import com.example.pan.model.FileInfo
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
        GlobalScope.launch {
            panRepository.list("time", "0", "0", 1, params.requestedLoadSize, dir).collect {
                if (it.errno == 0) {
                    callback.onResult(it.list, null, 2)
                } else {
                    callback.onResult(arrayListOf(), null, 2)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {

    }

    @ExperimentalCoroutinesApi
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {
        GlobalScope.launch {
            panRepository.list("time", "0", "0", params.key, params.requestedLoadSize, dir)
                .collect {
                    if (it.errno == 0) {
                        callback.onResult(it.list, params.key + 1)
                    } else {
                        callback.onResult(arrayListOf(), params.key + 1)
                    }
                }

        }
    }
}