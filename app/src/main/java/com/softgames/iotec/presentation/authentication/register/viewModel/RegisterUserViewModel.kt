package com.softgames.iotec.presentation.authentication.register.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softgames.iotec.domain.model.UserData

class RegisterUserViewModel : ViewModel() {

    private val _user_data = MutableLiveData(UserData())
    val user_data: LiveData<UserData> = _user_data

    fun saveUserName(name: String, last_name: String) {
        _user_data.value!!.name = name
        _user_data.value!!.last_name = last_name
    }

    fun saveOrganizationName(organization_name: String) {
        _user_data.value!!.organization_name = organization_name
    }

    fun saveOrganizationCode(organization_code: String) {
        _user_data.value!!.organization_code = organization_code
    }

}