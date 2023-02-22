package com.softgames.iotec.utils.extensions

import java.util.*

val Calendar.YEAR: Int
    get() = get(Calendar.YEAR) - 2000

val Calendar.MONTH: Int
    get() = get(Calendar.MONTH) + 1

val Calendar.DAY: Int
    get() = get(Calendar.DAY_OF_MONTH)

val Calendar.HOUR: Int
    get() = get(Calendar.HOUR_OF_DAY)

val Calendar.MINUTE: Int
    get() = get(Calendar.MINUTE)

val Calendar.SECOND: Int
    get() = get(Calendar.SECOND)

val Calendar.MILLISECOND: Int
    get() = get(Calendar.MILLISECOND)