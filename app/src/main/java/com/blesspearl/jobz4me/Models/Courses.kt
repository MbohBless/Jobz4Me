package com.blesspearl.jobz4me.Models

data class Courses(
    var count: Int,
    var next: String,
    var previous: String,
    var results: List<Course>
) {
    override fun toString(): String {
        return "Courses(count=$count, next='$next', previous='$previous', results=$results)"
    }
}
