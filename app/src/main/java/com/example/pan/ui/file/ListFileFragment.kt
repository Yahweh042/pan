package com.example.pan.ui.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pan.R
import com.example.pan.databinding.FragmentDashboardBinding
import com.example.pan.model.FileInfo
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFileFragment : Fragment() {

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
            binding.refreshLayout.isRefreshing = false
        }
        mViewModel.dirLiveData.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
            if ("/" != it) {
                binding.toolbar.setNavigationIcon(R.drawable.baseline_download_black_24dp)
            } else {
                binding.toolbar.navigationIcon = null
            }
        }
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

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
        adapter.setOnItemClickListener(object : ListFileInfoAdapter.OnItemClickListener {
            override fun onItemClick(item: FileInfo) {
                if (item.isdir == 1) {
                    mViewModel.changeDir(item.path)
                } else {
                    AlertDialog.Builder(this@ListFileFragment.requireContext())
                        .setMessage(Gson().toJson(item)).create().show()
                }
            }

        })
        mViewModel.changeDir("/")
        binding.refreshLayout.setOnRefreshListener {
            mViewModel.changeDir("/")
        }
    }
}