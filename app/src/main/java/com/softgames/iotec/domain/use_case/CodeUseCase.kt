package com.softgames.iotec.domain.use_case

import com.softgames.iotec.utils.extensions.*
import java.lang.Exception
import java.util.*

class CodeUseCase {
    companion object{
        fun generate(): String{
            val date = Calendar.getInstance()
            val letters = (0..9).toList() + ('A'..'Z').toList() + ('a'..'z').toList()
            val temp_code = arrayOf(
                letters[(letters.indices).random()],
                letters[date.YEAR],
                letters[date.MONTH],
                letters[date.DAY],
                letters[date.HOUR],
                letters[date.MINUTE],
                letters[date.SECOND],
                date.MILLISECOND.toString()[0],
                try {date.MILLISECOND.toString()[1]}
                catch (e: Exception) {letters[(letters.indices).random()]},
                try {date.MILLISECOND.toString()[2]}
                catch (e: Exception) {letters[(letters.indices).random()]}
            )
            return temp_code.joinToString("")
        }
    }
}