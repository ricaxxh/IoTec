package com.softgames.iotec.presentation.authentication.login.phone_auth.view.country_selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.softgames.iotec.base.BaseDialogFragment
import com.softgames.iotec.data.local.provider.list_country
import com.softgames.iotec.databinding.DialogCountrySelectorBinding
import com.softgames.iotec.presentation.authentication.login.phone_auth.view_model.PhoneAuthViewModel
import kotlinx.coroutines.launch

class CountrySelectorDialog : BaseDialogFragment<DialogCountrySelectorBinding>() {
    override fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DialogCountrySelectorBinding.inflate(inflater, container, false)

    lateinit var adapter: CountrySelectorAdapter
    private val view_model: PhoneAuthViewModel by activityViewModels()

    override fun launchEvents() {
        createDialog()
        createSelector()
    }

    private fun createDialog() {
        val width = (resources.displayMetrics.widthPixels * .90).toInt()
        val height = (resources.displayMetrics.heightPixels * .70).toInt()
        dialog!!.window!!.setLayout(width, height)
    }

    private fun createSelector() {
        binding.apply {

            adapter = CountrySelectorAdapter(requireContext()) { country ->
                    view_model.saveCountry(country)
                    dismiss()
            }

            recyclerCountry.layoutManager = LinearLayoutManager(requireContext())
            recyclerCountry.adapter = adapter

            tbxSearch.editText!!.doAfterTextChanged { value ->
                adapter.country_list = list_country.filter { it.name.contains(value.toString()) }
                adapter.notifyDataSetChanged()
            }
        }
    }

}