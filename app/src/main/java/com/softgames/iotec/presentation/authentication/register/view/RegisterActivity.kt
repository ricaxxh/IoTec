package com.softgames.iotec.presentation.authentication.register.view

import android.content.Intent
import com.softgames.iotec.base.BaseActivity
import com.softgames.iotec.databinding.ActivityRegisterBinding
import com.softgames.iotec.presentation.authentication.login.LoginActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    override fun setViewBinding() = ActivityRegisterBinding.inflate(layoutInflater)

    override fun launchEvents() {
        binding.toolbar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}