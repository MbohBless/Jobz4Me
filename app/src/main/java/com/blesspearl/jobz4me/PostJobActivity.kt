package com.blesspearl.jobz4me

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PostJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)
        supportActionBar?.title="New Job"
    }
}