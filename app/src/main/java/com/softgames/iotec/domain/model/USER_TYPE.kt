package com.softgames.iotec.domain.model

import java.io.Serializable

sealed class USER_TYPE : Serializable {

    abstract val NAME: String
    abstract val ROUTE: String

    object ADMIN : USER_TYPE() {
        override val NAME = "ADMIN"
        override val ROUTE = "Administradores"
    }

    object TEACHER : USER_TYPE() {
        override val NAME = "TEACHER"
        override val ROUTE = "Maestros"
    }
}
