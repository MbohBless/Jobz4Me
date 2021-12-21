package com.blesspearl.jobz4me.Models

import java.io.Serializable

data class User(var title: String, var job_title: String, var image_100x100: String) :
    Serializable {

    override fun toString(): String {
        return "User(title='$title', job_title='$job_title', image_100x100='$image_100x100')"
    }
}
