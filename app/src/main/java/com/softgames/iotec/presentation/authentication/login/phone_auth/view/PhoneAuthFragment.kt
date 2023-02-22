package com.softgames.iotec.presentation.authentication.login.phone_auth.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.softgames.iotec.R
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.databinding.FragmentPhoneAuthBinding
import com.softgames.iotec.presentation.authentication.login.phone_auth.view_model.PhoneAuthViewModel
import com.softgames.iotec.utils.text
import kotlinx.coroutines.launch

class PhoneAuthFragment : BaseFragment<FragmentPhoneAuthBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhoneAuthBinding.inflate(inflater, container, false)

    private val view_model: PhoneAuthViewModel by activityViewModels()

    override fun recoverData() {
        viewLifecycleOwner.lifecycleScope.launch {
            view_model.user_data.observe(viewLifecycleOwner) { user_data ->
                binding.apply {
                    tbxPhone.text = user_data.phone
                    tbxCountry.text = user_data.country.name
                    tbxLada.text = user_data.country.lada
                    tbxLada.startIconDrawable = ContextCompat
                        .getDrawable(requireContext(), user_data.country.flag)
                }
            }
        }
    }

    override fun launchEvents() {
        binding.apply {

            tbxCountry.editText!!.setOnClickListener {
                saveTextBoxesData()
                findNavController().navigate(R.id.ACTION_PHONE_AUTH_TO_COUNTRY_SELECTOR)
            }

            btnContinuar.setOnClickListener {
                if (valideTextBoxes()) {
                    saveTextBoxesData()
                    findNavController().navigate(R.id.ACTION_PHONE_AUTH_TO_PHONE_CODE)
                }
            }

        }
    }

    override fun valideTextBoxes(): Boolean {
        binding.apply {

            val phone = tbxPhone.text

            if (phone.isEmpty()) {
                tbxPhone.error = "Ingrese el número telefónico."; return false
            }
            if (phone.length != 10) {
                tbxPhone.error = "El número telefónico debe contener 10 dígitos."; return false
            }
            tbxPhone.error = null; return true
        }
    }

    override fun saveTextBoxesData() {
        val phone_number = binding.tbxPhone.text
        view_model.savePhoneNumber(phone_number)
    }

    override fun onPause() {
        saveTextBoxesData()
        super.onPause()
    }

}