package com.softgames.iotec.domain.model

import com.softgames.iotec.data.local.provider.list_country

data class UserData(
    var name: String = "",
    var last_name: String = "",
    var organization_name: String = "",
    var organization_code: String? = null,
    var phone: String = "",
    var sms_code: String = "",
    var mail: String = "",
    var country: Country = list_country[0],
)
