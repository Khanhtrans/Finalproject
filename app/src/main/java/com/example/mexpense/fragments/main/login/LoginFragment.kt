package com.example.mexpense.fragments.main.login

import android.content.Intent
import androidx.navigation.findNavController
import com.example.mexpense.MainActivity
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentLoginBinding
import com.example.mexpense.services.SqlService
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


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
        viewBinding.loginButton.setOnClickListener {
            verifyFromSQLite({
                //login success
                val intent = Intent(activity,MainActivity::class.java)
                this.startActivity(intent)
                this.activity?.finish()
            },{
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
            return
        }
        if (!inputValidation.isInputEditTextEmail(
                viewBinding.username,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.password,
                viewBinding.textInputLayoutPassword,
                getString(R.string.error_message_pass)
            )
        ) {
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

}