package com.example.pan.ui.file

import androidx.paging.PageKeyedDataSource
import com.example.pan.http.IPanService
import com.example.pan.model.FileInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListFileDataSource(
    private val panService: IPanService,
    private val dir: String
) : PageKeyedDataSource<Int, FileInfo>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, FileInfo>
    ) {
        GlobalScope.launch {
            val response = panService.list("time", "0", "0", 1, params.requestedLoadSize, dir)
            if (response.errno == 0) {
                callback.onResult(response.list, null, 2)
            } else {
                callback.onResult(arrayListOf(), null, 2)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {
        GlobalScope.launch {
            val response =
                panService.list("time", "0", "0", params.key, params.requestedLoadSize, dir)
            if (response.errno == 0) {
                callback.onResult(response.list, params.key + 1)
            } else {
                callback.onResult(arrayListOf(), params.key + 1)
            }
        }
    }
}