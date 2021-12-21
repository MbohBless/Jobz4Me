package com.blesspearl.jobz4me.Models

import java.io.Serializable

class Course(
    var title: String,
    var url: String,
    var price: String,
    var visible_instructors: List<User>,
    var image_480x270: String,
    var headline: String
) : Serializable {
    override fun toString(): String {
        return "Course(title='$title', url='$url', price='$price', visible_instructors=$visible_instructors, image_480x270='$image_480x270', headline='$headline')"
    }
}
