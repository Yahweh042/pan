package com.example.pan.ui.file

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.pan.R
import com.example.pan.databinding.ItemFileInfoBinding
import com.example.pan.model.FileInfo
import com.example.pan.ui.Utils
import com.example.pan.ui.base.BaseViewHolder

class ListFileInfoAdapter :
    PagedListAdapter<FileInfo, BaseViewHolder<ItemFileInfoBinding>>(Callback()) {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemFileInfoBinding> {
        val binding =
            ItemFileInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder<ItemFileInfoBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.fileName.text = it.server_filename
            var simpleInfo = Utils.formatDate(it.local_mtime)
            if (it.isdir == 1) {
                holder.binding.icon.setImageResource(R.drawable.ic_folder_black_24dp)
            } else {
                holder.binding.icon.setImageResource(R.drawable.ic_file_black_24dp)
                simpleInfo += " ${Utils.formatBit(it.size)}"
            }
            holder.binding.simpleInfo.text = simpleInfo
            holder.itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: FileInfo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
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