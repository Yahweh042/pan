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

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, FileInfo>) {
        GlobalScope.launch {
            val response = panService.list("time", "0", "0", 1, params.requestedLoadSize, dir)
            callback.onResult(response.list, null, 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FileInfo>) {
        GlobalScope.launch {
            val response =
                panService.list("time", "0", "0", params.key, params.requestedLoadSize, dir)
            callback.onResult(response.list, params.key + 1)
        }
    }
}