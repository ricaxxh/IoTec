package com.softgames.iotec.presentation.authentication.login.phone_auth.view_model

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.softgames.iotec.data.remote.repository.authentication.FirebasePhoneAuthRepo
import com.softgames.iotec.domain.model.Country
import com.softgames.iotec.domain.model.FirebaseAuthResponse
import com.softgames.iotec.domain.model.UserData
import com.softgames.iotec.presentation.authentication.login.phone_auth.model.FirebaseCodeResponse
import com.softgames.iotec.presentation.authentication.login.phone_auth.model.PhoneAuthInfo

class PhoneAuthViewModel : ViewModel() {

    private val _user_data = MutableLiveData(UserData())
    val user_data: LiveData<UserData> = _user_data

    private val _auth_info = MutableLiveData(PhoneAuthInfo())
    val auth_info: LiveData<PhoneAuthInfo> = _auth_info

    private val _code_response = MutableLiveData<FirebaseCodeResponse>()
    val code_response: LiveData<FirebaseCodeResponse> = _code_response

    private val _auth_response = MutableLiveData<FirebaseAuthResponse>()
    val auth_response: LiveData<FirebaseAuthResponse> = _auth_response


    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun savePhoneNumber(phone_number: String) {
        _user_data.value!!.phone = phone_number
    }

    fun saveCountry(country: Country) {
        _user_data.value = UserData(country = country, phone = user_data.value!!.phone)
    }

    fun saveSmsCode(sms_code: String) {
        _user_data.value!!.sms_code = sms_code
    }

    suspend fun sendVerificationCode(activity: Activity) {
        val phone_number = "+".plus(user_data.value!!.country.lada).plus(user_data.value!!.phone)
        FirebasePhoneAuthRepo.sendVerificationCode(phone_number, activity)
            .collect { code_response ->
                if (code_response is FirebaseCodeResponse.ON_CODE_SEND) {
                    _auth_info.value!!.verificationId = code_response.verificationId
                    _auth_info.value!!.token = code_response.token
                    _auth_info.value!!.codeWasSend = true
                }
                _code_response.value = code_response
            }
    }

    suspend fun loginWithPhoneCredential(activity: Activity, credential: PhoneAuthCredential) {
        FirebasePhoneAuthRepo.signInWithPhoneAuthCredential(activity, credential)
            .collect { auth_response ->
                _auth_response.value = auth_response
            }
    }

}