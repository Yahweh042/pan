package com.example.pan.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pan.databinding.FragmentDownloadListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.properties.Delegates

@AndroidEntryPoint
class DownloadListFragment : Fragment() {

    private lateinit var binding: FragmentDownloadListBinding
    private val mViewModel: NotificationsViewModel by viewModels()
    private val adapter = DownloadAdapter()
    private var type by Delegates.notNull<Int>()

    companion object {
        fun getInstance(type: Int): DownloadListFragment {
            val fragment = DownloadListFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadListBinding.inflate(inflater, container, false)
        type = arguments?.getInt("type")!!
        return binding.root
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.downloadList
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )


        when (type) {
            0 -> {
                mViewModel.activeTask.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                mViewModel.tellActive()
            }
            else -> {
                mViewModel.stoppedTask.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                mViewModel.tellStopped()
            }
        }

    }
}