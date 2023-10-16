package com.example.mexpense.fragments.main.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.mexpense.MainActivity
import com.example.mexpense.R
import com.example.mexpense.activity.main.login.LoginActivity
import com.example.mexpense.base.BaseActivity
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.base.SharePreUtil
import com.example.mexpense.databinding.FragmentLoginBinding
import com.example.mexpense.services.SqlService
import com.example.mexpense.ultilities.Constants
import com.example.mexpense.ultilities.Constants.SHARE_NAME
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler


class LoginFragment : BaseMVVMFragment<FragmentLoginBinding, LoginViewModel>() {

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: SqlService
    private lateinit var viewBinding: FragmentLoginBinding

    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        databaseHelper =  SqlService.getInstance(requireContext())
        inputValidation = InputValidation(requireContext());
    }

    override fun registerViewEvent() {
        viewBinding = getViewBinding()

        val isLogin = SharePreUtil.GetShareBoolean(requireContext(), Constants.KEY_IS_LOGIN);
        if (isLogin) {
            val email = SharePreUtil.GetShareString(requireContext(), Constants.KEY_EMAIL);
            viewBinding.username.setText(email?:"")
        }

        viewBinding.loginButton.setOnClickListener {
            (activity as LoginActivity).showLoading()
            verifyFromSQLite({
                //login success
                val email = viewBinding.username.text.toString().trim()
                lifecycleScope.launch {
                    delay(2000L)
                    (activity as LoginActivity).hideLoading()
                }
                loginSuccess(email)
            },{
                lifecycleScope.launch {
                    delay(2000L)
                    (activity as LoginActivity).hideLoading()
                }
                Snackbar.make(
                    viewBinding.container,
                    getString(R.string.error_valid_email_password),
                    Snackbar.LENGTH_LONG
                ).show()
            })
        }

        viewBinding.txtSignUpLink.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    override fun registerViewModelObs() {
    }

    private fun loginSuccess(email:String) {
        val user = databaseHelper.allUser.first { it.email == email }
        val name = user.name
        val id = user.id
        SharePreUtil.SetShareString(requireContext(),Constants.KEY_EMAIL,email)
        SharePreUtil.SetShareString(requireContext(),Constants.KEY_NAME,name)
        SharePreUtil.SetShareInt(requireContext(),Constants.KEY_USER_ID,id)
        SharePreUtil.SetShareBoolean(requireContext(),Constants.KEY_IS_LOGIN,true)

        val intent = Intent(activity,MainActivity::class.java)
        this.startActivity(intent)
        this.activity?.finish()
    }


    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private fun verifyFromSQLite(success:  () -> Unit, loginFail: ()-> Unit) {
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.username,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            (activity as LoginActivity).hideLoading()
            return
        }
        if (!inputValidation.isInputEditTextEmail(
                viewBinding.username,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            (activity as LoginActivity).hideLoading()

            return
        }
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.password,
                viewBinding.textInputLayoutPassword,
                getString(R.string.error_message_pass)
            )
        ) {
            (activity as LoginActivity).hideLoading()

            return
        }
        try {
            if (databaseHelper.checkUser(
                    viewBinding.username.text.toString().trim(),
                    viewBinding.password.text.toString().trim()
                )
            ) {
                // login success
                success()
            } else {
                loginFail()
            }
        } catch (e: Exception) {
            loginFail()
        }

    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        viewBinding.username.setText("")
        viewBinding.password.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as LoginActivity).hideLoading()
    }

}