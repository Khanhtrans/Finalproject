package com.example.mexpense.fragments.main.setting

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.LoginActivity
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentSettingBinding
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants

class SettingFragment : BaseMVVMFragment<FragmentSettingBinding,SettingViewModel>() {
    private lateinit var viewBinding: FragmentSettingBinding
    private lateinit var databaseHelper: SqlService
    var name: String = ""
    override fun getViewModelClass(): Class<SettingViewModel> {
        return SettingViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        databaseHelper =  SqlService.getInstance(requireContext())

    }

    override fun registerViewEvent() {
        viewBinding = getViewBinding()
        val email = SharePreUtil.GetShareString(requireContext(),Constants.KEY_EMAIL)
        val name = SharePreUtil.GetShareString(requireContext(),Constants.KEY_NAME)
        viewBinding.tvName.text = name?:""
        viewBinding.tvEmail.text = email?:""

        viewBinding.tvLogout.setOnClickListener {

            SharePreUtil.SetShareBoolean(requireContext(),Constants.KEY_IS_LOGIN,false)

            val intent = Intent(activity,LoginActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    override fun registerViewModelObs() {
    }


}