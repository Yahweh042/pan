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
import androidx.recyclerview.widget.RecyclerView
import com.example.pan.R
import com.example.pan.model.FileInfo
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFileFragment : Fragment() {

    private val mViewModel: ListFileViewModel by viewModels()
    private val adapter = ListFileInfoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel.fileInfoList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
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
                    AlertDialog.Builder(this@ListFileFragment.requireContext()).setMessage(Gson().toJson(item)).create().show()
                }
            }

        })
        mViewModel.changeDir("/")
    }
}