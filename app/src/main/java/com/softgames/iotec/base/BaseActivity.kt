package com.softgames.iotec.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.softgames.iotec.R

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.IOTEC_THEME)
        binding = setViewBinding()
        setContentView(binding.root)

        main()
        launchEvents()
    }

    protected abstract fun setViewBinding(): VB

    protected open fun main(){}

    protected abstract fun launchEvents()

    protected open fun validTextBoxes() = false

    protected fun message(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
}
