package com.example.pan.ui.file

import androidx.paging.DataSource
import com.example.pan.http.PanRepository
import com.example.pan.model.FileInfo

class ListFileDataSourceFactory(
    private val panRepository: PanRepository,
    private val dir: String
) : DataSource.Factory<Int, FileInfo>() {
    override fun create(): DataSource<Int, FileInfo> {
        return ListFileDataSource(panRepository, dir)
    }
}