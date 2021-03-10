package com.example.pan.ui.file

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pan.MainViewModel
import com.example.pan.aria2.Aria2Manager
import com.example.pan.databinding.FragmentDashboardBinding
import com.example.pan.model.FileInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class ListFileFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val mViewModel: ListFileViewModel by viewModels()
    private val adapter = ListFileInfoAdapter()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel.fileInfoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            mViewModel.stopRefresh()
        }
        mViewModel.dirLiveData.observe(viewLifecycleOwner) {
            mainViewModel.setTitle(it)
//            if ("/" != it) {
//                binding.toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp)
//                binding.toolbar.setNavigationOnClickListener {
//                    mViewModel.onBackDir()
//                }
//            } else {
//                binding.toolbar.navigationIcon = null
//            }
        }
        mViewModel.refreshStatus.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        mViewModel.filemeta.observe(viewLifecycleOwner) {
            AlertDialog.Builder(this@ListFileFragment.requireContext())
                .setTitle(it.filename)
                .setMessage(it.dlink)
                .setPositiveButton("下载") { _, _ ->
                    mViewModel.download(it.dlink, it.filename)
                }
                .create().show()
        }
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                mViewModel.onBackDir()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        adapter.setOnItemClickListener(object : ListFileInfoAdapter.OnItemClickListener {
            override fun onItemClick(item: FileInfo) {
                if (item.isdir == 1) {
                    mViewModel.changeDir(item.path)
                } else {
                    mViewModel.filemetas(item.fs_id)
                }
            }

        })
        mViewModel.initData()
        binding.refreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }
    }
}