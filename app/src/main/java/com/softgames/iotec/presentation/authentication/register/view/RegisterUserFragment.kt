package com.softgames.iotec.presentation.authentication.register.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.softgames.iotec.R
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.databinding.FragmentRegisterUserBinding
import com.softgames.iotec.domain.model.USER_TYPE
import com.softgames.iotec.presentation.authentication.register.viewModel.RegisterUserViewModel
import com.softgames.iotec.utils.C_USER_TYPE
import com.softgames.iotec.utils.text

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterUserBinding.inflate(inflater, container, false)

    private val view_model: RegisterUserViewModel by activityViewModels()
    private lateinit var user_type: USER_TYPE

    override fun recoverData() {

        val user_data = view_model.user_data.value!!
        user_type = requireActivity().intent.extras!!.getSerializable(C_USER_TYPE) as USER_TYPE
        message(user_type.NAME)

        binding.tbxName.text = user_data.name
        binding.tbxLastName.text = user_data.last_name
    }

    override fun launchEvents() {
        binding.btnContinuar.setOnClickListener {
            if (valideTextBoxes()) {
                saveTextBoxesData()
                when (user_type) {
                    is USER_TYPE.ADMIN -> findNavController().navigate(R.id.ACTION_REGISTER_USER_TO_REGISTER_ADMIN)
                    is USER_TYPE.TEACHER -> findNavController().navigate(R.id.ACTION_REGISTER_USER_TO_REGISTER_TEACHER)
                }
            }
        }
    }

    override fun valideTextBoxes(): Boolean {
        binding.apply {
            if (tbxName.text.isEmpty()) {
                tbxName.error = "Ingrese su nombre."
            }
            if (tbxLastName.text.isEmpty()) {
                tbxLastName.error = "Ingrese su apellido."; return false
            }
            tbxName.error = null; tbxLastName.error = null; return true
        }
    }

    override fun saveTextBoxesData() {
        view_model.saveUserName(binding.tbxName.text, binding.tbxLastName.text)
    }

}