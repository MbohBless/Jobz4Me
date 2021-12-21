package com.blesspearl.jobz4me.Models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@Keep
@IgnoreExtraProperties
data class Jobz(
    var name: String? = null,
    var description: String? = null,
    var qualification: String? = null,
    var category: String? = null,
    var location: String? = null,
    var local_name: String? = null,
    var pay_range: String? = null,
    var required_date: String? = null,
    var posted_by: String? = null
) : Serializable {

    override fun toString(): String {
        return "Job(name='$name', description=$description, qualification=$qualification, category='$category', location='$location', local_name=$local_name, pay_range='$pay_range', required_date='$required_date', posted_by='$posted_by')"
    }
}