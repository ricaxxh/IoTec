package com.softgames.iotec.presentation.authentication.login.phone_auth.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.data.remote.repository.authentication.FirebaseAuthRepo
import com.softgames.iotec.data.remote.repository.authentication.FirebasePhoneAuthRepo
import com.softgames.iotec.databinding.FragmentPhoneCodeBinding
import com.softgames.iotec.domain.model.FirebaseAuthResponse
import com.softgames.iotec.domain.model.USER_TYPE
import com.softgames.iotec.presentation.authentication.login.phone_auth.model.FirebaseCodeResponse
import com.softgames.iotec.presentation.authentication.login.phone_auth.view_model.PhoneAuthViewModel
import com.softgames.iotec.presentation.authentication.register.view.RegisterActivity
import com.softgames.iotec.presentation.menu.MenuActivity
import com.softgames.iotec.utils.C_USER_TYPE
import com.softgames.iotec.utils.text
import kotlinx.coroutines.launch

class PhoneCodeFragment : BaseFragment<FragmentPhoneCodeBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhoneCodeBinding.inflate(inflater, container, false)

    private val view_model: PhoneAuthViewModel by activityViewModels()
    private lateinit var user_type: USER_TYPE

    override fun recoverData() {
        val data = view_model.user_data.value!!
        binding.txtPhone.text = "+".plus(data.country.lada).plus(" ${data.phone}")
        binding.tbxCode.text = data.sms_code
    }

    override fun launchEvents() {
        initAuth()
        verificationCodeStatus()
        firebaseAuthStatus()

        binding.btnContinuar.setOnClickListener {
            if (valideTextBoxes()) {
                val auth_info = view_model.auth_info.value!!

                if (auth_info.codeWasSend) {
                    val credential = FirebasePhoneAuthRepo.generateCredential(
                        verificationId = auth_info.verificationId!!,
                        sms_code = binding.tbxCode.text
                    )

                    loginWithPhoneCredential(credential)

                } else {
                    message("Espere a que se envíe el código de verificación.")
                }
            }
        }
    }

    private fun initAuth() {
        if (!view_model.auth_info.value!!.codeWasSend) {

            //SEND VERIFICATIÓN CODE
            viewLifecycleOwner.lifecycleScope.launch {
                view_model.sendVerificationCode(requireActivity())
            }
        }
    }

    private fun verificationCodeStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            view_model.code_response.observe(viewLifecycleOwner) { code_response ->
                when (code_response) {
                    is FirebaseCodeResponse.LOADING -> {
                        message("Solicitando código de verificación.")
                    }
                    is FirebaseCodeResponse.ON_CODE_SEND -> {
                        message("Se envió el código de verificación.")
                    }
                    is FirebaseCodeResponse.AUTOMAMTIC_VERIFICATION -> {
                        message("Verificación automática exitosa.")
                        loginWithPhoneCredential(code_response.credential)
                    }
                    is FirebaseCodeResponse.FAILURE -> {
                        when (code_response.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                message(
                                    "Error al enviar el código de verificación: " +
                                            "${code_response.exception.message}", Toast.LENGTH_LONG
                                )
                            }
                            is FirebaseTooManyRequestsException -> {
                                message(
                                    "Se excedio el numero de solicitudes de códigos de" +
                                            " verificacion desde el mismo dispositivo",
                                    Toast.LENGTH_LONG
                                )
                            }
                            is FirebaseNetworkException -> {
                                message("Compruebe su conexión a internet")
                            }
                        }
                    }

                }
            }
        }
    }

    private fun firebaseAuthStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            view_model.auth_response.observe(viewLifecycleOwner) { auth_response ->
                when (auth_response) {
                    is FirebaseAuthResponse.LOADING -> {
                        message("Iniciando Sesión.")
                    }
                    is FirebaseAuthResponse.COMPLETE -> {
                        message("Se inicio sesión con exito.")
                        val user = auth_response.task.result.user!!
                        checkUserIsRegistered(user.uid)
                    }
                    is FirebaseAuthResponse.FAILURE -> {
                        when (auth_response.exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                binding.tbxCode.error = "Código incorrecto."
                            }
                            is FirebaseNetworkException -> {
                                message("Error de conexión a internet")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loginWithPhoneCredential(credential: PhoneAuthCredential) {
        viewLifecycleOwner.lifecycleScope.launch {
            view_model.loginWithPhoneCredential(requireActivity(), credential)
        }
    }

    private fun checkUserIsRegistered(user_id: String) {
        user_type = requireActivity().intent.extras!!.getSerializable(C_USER_TYPE) as USER_TYPE
        viewLifecycleOwner.lifecycleScope.launch {
            FirebaseAuthRepo.checkUserExist(user_id, user_type.ROUTE).collect { exist ->
                if (exist) {
                    navigateToMenuScreen()
                } else {
                    message("ENTRO")
                    navigateToRegisterScreen()
                }
            }
        }
    }

    private fun navigateToMenuScreen() {
        startActivity(Intent(requireActivity(), MenuActivity::class.java))
        requireActivity().finish()
    }

    private fun navigateToRegisterScreen() {
        startActivity(
            Intent(requireActivity(), RegisterActivity::class.java)
                .putExtra(C_USER_TYPE, user_type)
        )
        requireActivity().finish()
    }

    override fun valideTextBoxes(): Boolean {
        binding.apply {
            if (tbxCode.text.isEmpty()) {
                tbxCode.error = "Ingrese el código"
                return false
            }
            tbxCode.error = null
            return true
        }
    }

    override fun saveTextBoxesData() {
        view_model.saveSmsCode(binding.tbxCode.text)
    }

    override fun onStop() {
        saveTextBoxesData()
        super.onStop()
    }
}
