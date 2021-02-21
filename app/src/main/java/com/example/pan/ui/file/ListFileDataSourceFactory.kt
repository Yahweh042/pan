package com.example.pan.ui.file

import androidx.paging.DataSource
import com.example.pan.http.IPanService
import com.example.pan.model.FileInfo

class ListFileDataSourceFactory(
    private val panService: IPanService,
    private val dir: String
) : DataSource.Factory<Int, FileInfo>() {
    override fun create(): DataSource<Int, FileInfo> {
        return ListFileDataSource(panService, dir)
    }
}