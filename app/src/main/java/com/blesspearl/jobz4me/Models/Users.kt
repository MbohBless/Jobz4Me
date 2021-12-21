package com.blesspearl.jobz4me.Models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Users(
    var name: String? = null,
    var id: String? = null,
    var number: String? = null,
    var email: String? = null
) {
    override fun toString(): String {
        return "Users(name=$name, id=$id, number=$number, email=$email)"
    }
}