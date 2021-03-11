package com.example.pan.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.pan.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tabLayout.addTab(tabLayout.newTab().setText("下载中"))
            tabLayout.addTab(tabLayout.newTab().setText("已完成"))

            viewPager.adapter = object :
                FragmentStatePagerAdapter(this@NotificationsFragment.childFragmentManager) {
                override fun getCount(): Int = 2

                override fun getItem(position: Int): Fragment =
                    DownloadListFragment.getInstance(position)

                override fun getPageTitle(position: Int): CharSequence? =
                    if (position == 0) "下载中" else "已完成"


            }
            tabLayout.setupWithViewPager(viewPager)
        }
    }

}