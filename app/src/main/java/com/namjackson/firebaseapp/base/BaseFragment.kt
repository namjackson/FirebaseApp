package com.namjackson.firebaseapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment<B : ViewDataBinding>(
    private val layoutId: Int
) : Fragment() {
    protected lateinit var binding: B

    abstract fun getViewModel(): BaseViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this@BaseFragment.viewLifecycleOwner
        subscribe(getViewModel())
    }


    fun showToast(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }

    protected fun bind(action: B.() -> Unit) {
        binding.run(action)
    }

    private fun subscribe(viewModel: BaseViewModel) {
        subscribeToShowToast(viewModel)
    }

    private fun subscribeToShowToast(viewModel: BaseViewModel) {
        viewModel.showToastEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                showToast(it)
            }
        })
    }
}