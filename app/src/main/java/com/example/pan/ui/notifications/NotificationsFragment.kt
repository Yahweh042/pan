package com.example.pan.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.baidu.oauth.sdk.auth.AuthInfo
import com.baidu.oauth.sdk.auth.BdOauthSdk
import com.baidu.oauth.sdk.auth.BdSsoHandler
import com.baidu.oauth.sdk.callback.BdOauthCallback
import com.baidu.oauth.sdk.dto.BdOauthDTO
import com.baidu.oauth.sdk.result.BdOauthResult
import com.example.pan.databinding.FragmentNotificationsBinding
import com.example.pan.ui.login.LoginActivity
import java.util.*


class NotificationsFragment : Fragment() {

    private val mViewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var bdSsoHandler: BdSsoHandler

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

    private fun initSdk() {
        val redirectUrl = ""
        val scope = "basic"
        val appKey = "yGey4TTRWdoglLxRz0X0fBMxYozXweN9"
        val authInfo = AuthInfo(context, appKey, redirectUrl, scope)
        BdOauthSdk.init(authInfo)
        authInfo.isDebug(true)
    }

    private fun oauthLogin() {
        bdSsoHandler = BdSsoHandler(requireActivity())
        val bdOauthDTO = BdOauthDTO()
        bdOauthDTO.oauthType = BdOauthDTO.OAUTH_TYPE_BOTH
        bdOauthDTO.state = UUID.randomUUID().toString()
        bdSsoHandler.authorize(bdOauthDTO, object : BdOauthCallback() {
            override fun onSuccess(result: BdOauthResult) {
                print(result)
            }

            override fun onFailure(result: BdOauthResult) {
                print(result)
            }
        })
    }
}