package com.softgames.iotec.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = setViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main()
        recoverData()
        launchEvents()
    }

    override fun onPause() {
        saveTextBoxesData()
        super.onPause()
    }

    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun main() {}

    protected open fun recoverData() {}

    protected abstract fun launchEvents()

    protected open fun valideTextBoxes() = false

    protected open fun saveTextBoxesData() {}

    fun message(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}