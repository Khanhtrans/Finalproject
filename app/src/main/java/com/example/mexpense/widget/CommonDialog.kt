package com.example.upbrain.shared.widget

import android.content.Context

import com.example.mexpense.base.BaseDialog
import com.example.mexpense.databinding.DialogCommonBinding
import com.example.mexpense.exts.gone
import com.example.mexpense.exts.setOnDelayClickListener

class CommonDialog(
    context: Context,
    private val title: String,
    private val negativeText: String? = null,
    private val negativeEvent: (() -> Unit)? = null,
    private val positiveText: String? = null,
    private val positiveEvent: (() -> Unit)? = null,
) : BaseDialog<DialogCommonBinding>(context) {
    override fun getLayoutBinding(): DialogCommonBinding {
        return DialogCommonBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        getViewBinding().tvMessage.text = title

        negativeText?.let {
            getViewBinding().btNegative.apply {
                text = it
                setOnDelayClickListener {
                    dismiss()
                    negativeEvent?.invoke()
                }
            }
        } ?: kotlin.run {
            getViewBinding().btNegative.gone()
        }

        positiveText?.let {
            getViewBinding().btPositive.apply {
                text = it
                setOnDelayClickListener {
                    dismiss()
                    positiveEvent?.invoke()
                }
            }
        } ?: kotlin.run {
            getViewBinding().btPositive.gone()
        }
    }
}