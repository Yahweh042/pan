package com.example.pan.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pan.R
import com.example.pan.ui.login.LoginActivity
import com.google.gson.Gson
import kotlin.concurrent.thread

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        textView.text = "Login"
        notificationsViewModel.globalStat.observe(viewLifecycleOwner, {
            textView.text = Gson().toJson(it)
        })

        textView.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        thread {
//            notificationsViewModel.getGlobalStat()

        }

        return root
    }
}