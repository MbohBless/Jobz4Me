package com.blesspearl.jobz4me

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.databinding.ActivityCourseDetailsBinding
import com.bumptech.glide.Glide

class CourseDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Brief Course Details"
        val course = intent.getSerializableExtra("Course") as Course
        Glide.with(this).load(course.image_480x270).into(
            binding
                .imageCourse
        )
        binding.title.text = course.title
        binding.headline.text = course.headline
        binding.price.text = course.price
        val admin: StringBuilder = StringBuilder()
        for (a in course.visible_instructors) {
            admin.append("${a.title} -  ${a.job_title} \n")
        }
        binding.admins.text = admin
        binding.btnCheckOut.setOnClickListener {
            openInBrowser(link = course.url)

        }
        binding.btnShare.setOnClickListener {
            val url = "https://www.udemy.com${course.url}"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

    private fun openInBrowser(link: String) {
        val url = "https://www.udemy.com$link"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}