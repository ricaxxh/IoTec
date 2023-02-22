package com.softgames.iotec.presentation.authentication.register.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.data.remote.repository.register.FirestoreRegisterRepo
import com.softgames.iotec.databinding.FragmentRegisterAdminBinding
import com.softgames.iotec.domain.model.USER_TYPE
import com.softgames.iotec.domain.use_case.CodeUseCase
import com.softgames.iotec.presentation.authentication.register.viewModel.RegisterUserViewModel
import com.softgames.iotec.presentation.menu.MenuActivity
import com.softgames.iotec.utils.text
import kotlinx.coroutines.launch

class RegisterAdminFragmen : BaseFragment<FragmentRegisterAdminBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterAdminBinding.inflate(inflater, container, false)

    private val view_model: RegisterUserViewModel by activityViewModels()

    override fun recoverData() {
        generateOrganizationCode()
    }

    override fun launchEvents() {
        binding.btnRegistrar.setOnClickListener {
            if (valideTextBoxes()) {
                registerAdminAndOrganization()
            }
        }
    }

    private fun registerAdminAndOrganization() {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = Firebase.auth.currentUser!!
            val user_data = view_model.user_data.value!!
            val register_data = hashMapOf(
                "Nombre" to user_data.name,
                "Apellidos" to user_data.last_name,
                "Organización" to user_data.organization_name,
                "Código" to user_data.organization_code,
                "Tipo de usuario" to USER_TYPE.ADMIN.NAME,
            )
            FirestoreRegisterRepo.registerAdminAndOrganization(user.uid, register_data)
                .collect() {
                    startActivity(Intent(requireContext(), MenuActivity::class.java))
                    requireActivity().finish()
                }
        }
    }

    private fun generateOrganizationCode() {
        viewLifecycleOwner.lifecycleScope.launch {
            view_model.user_data.observe(viewLifecycleOwner) {
                if (it.organization_code == null) {
                    val code = CodeUseCase.generate()
                    view_model.saveOrganizationCode(code)
                }
                binding.txtOrganizationCode.text = it.organization_code
            }
        }
    }

    override fun valideTextBoxes(): Boolean {
        binding.apply {
            if (tbxOrganizationName.text.isEmpty()) {
                tbxOrganizationName.error = "Ingrese el nombnre de la organización."
                return false
            }
            tbxOrganizationName.error = null
            saveTextBoxesData()
            return true
        }
    }

    override fun saveTextBoxesData() {
        view_model.saveOrganizationName(binding.tbxOrganizationName.text)
    }

}