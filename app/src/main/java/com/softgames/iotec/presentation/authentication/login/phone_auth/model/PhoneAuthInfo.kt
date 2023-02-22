package com.softgames.iotec.presentation.authentication.login.phone_auth.model

import com.google.firebase.auth.PhoneAuthProvider

data class PhoneAuthInfo(
    var codeWasSend: Boolean = false,
    var verificationId: String? = null,
    var token: PhoneAuthProvider.ForceResendingToken? = null
)
