package com.example.mexpense.fragments.main.splash

import android.os.Handler
import androidx.navigation.findNavController
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentSpashScreenBinding

class SplashScreenFragment : BaseMVVMFragment<FragmentSpashScreenBinding,SplashScreenViewModel>() {
    lateinit var viewBinding: FragmentSpashScreenBinding
    override fun getViewModelClass(): Class<SplashScreenViewModel> {
        return SplashScreenViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentSpashScreenBinding {
        return FragmentSpashScreenBinding.inflate(layoutInflater)
    }

    override fun initialize() {
    }

    override fun registerViewEvent() {
        viewBinding = getViewBinding()
        Handler().postDelayed({
            viewBinding.container
                .findNavController()
                .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
        }, 2000)
    }

    override fun registerViewModelObs() {
    }


}