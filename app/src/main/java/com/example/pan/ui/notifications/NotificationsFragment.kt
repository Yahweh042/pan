package com.example.pan.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pan.databinding.FragmentNotificationsBinding
import com.example.pan.ui.login.LoginActivity


class NotificationsFragment : Fragment() {

    private val mViewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textNotifications.text = "Login"

        binding.textNotifications.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }

}