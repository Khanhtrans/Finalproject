package com.example.mexpense.fragments.main.register

import androidx.navigation.findNavController
import com.example.mexpense.R
import com.example.mexpense.base.BaseMVVMFragment
import com.example.mexpense.databinding.FragmentRegisterBinding
import com.example.mexpense.entity.User
import com.example.mexpense.fragments.main.login.InputValidation
import com.example.mexpense.services.SqlService
import com.example.mexpense.services.SqlService.TABLE_USER
import com.google.android.material.snackbar.Snackbar


class RegisterFragment : BaseMVVMFragment<FragmentRegisterBinding,RegisterViewModel>() {
    private lateinit var viewBinding: FragmentRegisterBinding
    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: SqlService
    private lateinit var user: User
    override fun getViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }

    override fun getLayoutBinding(): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        databaseHelper =  SqlService.getInstance(requireContext())
        inputValidation = InputValidation(requireContext())
        user = User()

    }

    override fun registerViewEvent() {
        viewBinding = getViewBinding()
        viewBinding.btnRegister.setOnClickListener {
            postDataToSQLite()
        }
        viewBinding.txtBackLogin.setOnClickListener {
            it.findNavController()
                .navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    override fun registerViewModelObs() {
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private fun postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.txtName,
                viewBinding.textInputLayoutName,
                getString(R.string.error_message_name)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.txtUsername,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextEmail(
                viewBinding.txtUsername,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextFilled(
                viewBinding.txtPassword,
                viewBinding.textInputLayoutEmail,
                getString(R.string.error_message_password)
            )
        ) {
            return
        }
        if (!inputValidation.isInputEditTextMatches(
                viewBinding.txtPassword,
                viewBinding.txtRePassword,
                viewBinding.textInputLayoutRePassword,
                getString(R.string.error_password_match)
            )
        ) {
            return
        }
        try {
            val isDbExist = databaseHelper.doesTableExist(TABLE_USER)
            if (!databaseHelper.checkUser(viewBinding.txtUsername.text.toString().trim())) {
                user.name = viewBinding.txtName.text.toString().trim()
                user.email = viewBinding.txtUsername.text.toString().trim()
                user.password = viewBinding.txtPassword.text.toString().trim()
                databaseHelper.addUser(user)
                // Snack Bar to show success message that record saved successfully
                Snackbar.make(
                    viewBinding.container,
                    getString(R.string.success_message),
                    Snackbar.LENGTH_LONG
                ).show()
                emptyInputEditText()
            } else {
                // Snack Bar to show error message that record already exists
                Snackbar.make(
                    viewBinding.container,
                    getString(R.string.error_email_exists),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Snackbar.make(
                viewBinding.container,
                getString(R.string.error_email_exists),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    /**
     * This method is to empty all input edit text
     */
    private fun emptyInputEditText() {
        viewBinding.txtName.setText("")
        viewBinding.txtUsername.setText("")
        viewBinding.txtPassword.setText("")
        viewBinding.txtRePassword.setText("")
    }
}