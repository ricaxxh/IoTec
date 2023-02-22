package com.softgames.iotec.presentation.authentication.register.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.softgames.iotec.base.BaseFragment
import com.softgames.iotec.data.remote.repository.register.FirestoreRegisterRepo
import com.softgames.iotec.databinding.FragmentRegisterTeacherBinding
import com.softgames.iotec.domain.model.USER_TYPE
import com.softgames.iotec.domain.model.UserData
import com.softgames.iotec.presentation.authentication.register.viewModel.RegisterUserViewModel
import com.softgames.iotec.presentation.menu.MenuActivity
import com.softgames.iotec.utils.text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterTeacherFragment : BaseFragment<FragmentRegisterTeacherBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterTeacherBinding.inflate(inflater, container, false)

    private val viewModel: RegisterUserViewModel by activityViewModels()

    override fun recoverData() {
        //val data = viewModel.user_data.value!!
        val data = UserData(organization_code = "")
        binding.tbxCode.text = data.organization_code!!
    }

    override fun launchEvents() {
        binding.btnRegistro.setOnClickListener {
            if (valideTextBoxes()) {
                registerTeacher()
            }
        }

        binding.tbxCode.editText!!.doAfterTextChanged {
            if (it!!.length == 10) {
                message("Entro")
                viewLifecycleOwner.lifecycleScope.launch {
                    FirestoreRegisterRepo.searchOrganization(it.toString())
                        .collect { name ->
                            if(name != null) {
                                binding.txtOrganization.text = name
                            } else{
                                binding.txtOrganization.text = "Sin resultados."
                            }
                        }
                }
            }
        }
    }

    private fun registerTeacher() {

        val user_data = viewModel.user_data.value!!
        val user = Firebase.auth.currentUser!!
        val register_data = hashMapOf(
            "Nombre" to user_data.name,
            "Apellidos" to user_data.last_name,
            "Código" to user_data.organization_code,
            "Tipo de usuario" to USER_TYPE.ADMIN.NAME,
        )
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            FirestoreRegisterRepo.registerTeacher(user.uid, register_data).collect() {
                startActivity(Intent(requireContext(), MenuActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    override fun valideTextBoxes(): Boolean {
        binding.apply {
            if (tbxCode.text.isEmpty()) {
                tbxCode.error = "Ingrese el código."
            }
            if (tbxCode.text.length != 10) {
                tbxCode.error = "El código debe tener 10 dígitos."
            }
            tbxCode.error = null
            return true
        }
    }

    override fun saveTextBoxesData() {
        viewModel.saveOrganizationCode(binding.tbxCode.text)
    }
}