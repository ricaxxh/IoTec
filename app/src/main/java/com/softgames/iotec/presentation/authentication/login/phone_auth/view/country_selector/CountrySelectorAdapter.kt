package com.softgames.iotec.presentation.authentication.login.phone_auth.view.country_selector

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.softgames.iotec.data.local.provider.list_country
import com.softgames.iotec.databinding.ItemCountryBinding
import com.softgames.iotec.domain.model.Country

class CountrySelectorAdapter(
    val context: Context,
    var country_list: List<Country> = list_country,
    val listener: (Country) -> Unit
) :
    RecyclerView.Adapter<CountrySelectorAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(country_list[position])
    }

    override fun getItemCount() = country_list.size

    inner class CountryViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country) {
            binding.apply {
                imgFlag.setImageDrawable(ContextCompat.getDrawable(context, country.flag))
                txtName.text = country.name
                txtLada.text = "+".plus(country.lada)

                root.setOnClickListener {
                    listener(country)
                }
            }
        }
    }
}