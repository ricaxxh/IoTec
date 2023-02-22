package com.softgames.iotec.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.softgames.iotec.base.BaseActivity
import com.softgames.iotec.databinding.ActivityWelcomeBinding
import com.softgames.iotec.domain.model.USER_TYPE
import com.softgames.iotec.presentation.authentication.login.LoginActivity
import com.softgames.iotec.presentation.authentication.register.view.RegisterActivity
import com.softgames.iotec.utils.C_USER_TYPE

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {
    override fun setViewBinding() = ActivityWelcomeBinding
        .inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun launchEvents() {

        binding.btnAdmin.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .putExtra(C_USER_TYPE, USER_TYPE.ADMIN)
            )
        }

        binding.btnTeacher.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .putExtra(C_USER_TYPE, USER_TYPE.TEACHER)
            )

        }

    }
}