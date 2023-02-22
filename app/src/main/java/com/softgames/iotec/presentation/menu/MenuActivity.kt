package com.softgames.iotec.presentation.menu

import com.softgames.iotec.base.BaseActivity
import com.softgames.iotec.databinding.ActivityMenuBinding

class MenuActivity : BaseActivity<ActivityMenuBinding>() {
    override fun setViewBinding(): ActivityMenuBinding =
        ActivityMenuBinding.inflate(layoutInflater)

    override fun launchEvents() {

    }
}