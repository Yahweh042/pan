package com.example.pan.ui.file

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.pan.R
import com.example.pan.databinding.ItemFileInfoBinding
import com.example.pan.model.FileInfo
import com.example.pan.ui.base.BaseViewHolder

class ListFileInfoAdapter :
    PagedListAdapter<FileInfo, BaseViewHolder<ItemFileInfoBinding>>(Callback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemFileInfoBinding> {
        val binding = ItemFileInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemFileInfoBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.fileName.text = it.server_filename
            if (it.isdir == 1) {
                holder.binding.icon.setImageResource(R.drawable.baseline_folder_black_24dp)
            } else {
                holder.binding.icon.setImageResource(R.drawable.baseline_file_black_24dp)
            }
        }
    }

    internal class Callback : DiffUtil.ItemCallback<FileInfo>() {
        override fun areItemsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem.fs_id == newItem.fs_id
        }
    }

}