package com.example.pan.ui.notifications

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.pan.databinding.ItemTaskInfoBinding
import com.example.pan.model.DownloadTaskInfo
import com.example.pan.ui.base.BaseViewHolder

class DownloadAdapter :
    ListAdapter<DownloadTaskInfo, BaseViewHolder<ItemTaskInfoBinding>>(Callback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemTaskInfoBinding> {
        return BaseViewHolder(
            ItemTaskInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder<ItemTaskInfoBinding>, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item?.let {
                fileName.text = it.fileName
                progressBar.progress = it.process
                simpleInfo.text = "${it.completeLength}/${it.totalLength}"
                num.text = it.connections
                speed.text = it.downloadSpeed
            }

        }
    }

    internal class Callback : DiffUtil.ItemCallback<DownloadTaskInfo>() {
        override fun areItemsTheSame(
            oldItem: DownloadTaskInfo,
            newItem: DownloadTaskInfo
        ): Boolean {
            return TextUtils.equals(oldItem.gid, newItem.gid)
        }

        override fun areContentsTheSame(
            oldItem: DownloadTaskInfo,
            newItem: DownloadTaskInfo
        ): Boolean {
            val flag = (oldItem.downloadSpeed == newItem.downloadSpeed) &&
                    (oldItem.connections == newItem.connections) &&
                    (oldItem.completeLength == newItem.completeLength) &&
                    (oldItem.process == newItem.process)
            return false
        }
    }

}