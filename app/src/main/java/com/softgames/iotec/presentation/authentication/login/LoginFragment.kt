package com.softgames.iotec.presentation.authentication.login

import android.app.Activity.RESULT_OK
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.softgames.iotec.R
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun launchEvents() {
        binding.apply {

            btnPhone.setOnClickListener {
                findNavController().navigate(R.id.ACTION_LOGIN_TO_PHONE_AUTH)
            }

            btnMail.setOnClickListener {

            }

            btnGoogle.setOnClickListener {
                signInWithGoogle()
            }

            btnFacebook.setOnClickListener {

            }
        }
    }

    fun signInWithGoogle() {

        //Config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.WEB_CLIENT_ID))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(requireActivity(), gso)
        login_result.launch(client.signInIntent)
    }

    private val login_result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                message("EXITO")
            } else {
                message("ERROR")
            }
        }

}