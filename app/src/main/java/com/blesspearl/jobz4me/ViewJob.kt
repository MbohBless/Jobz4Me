package com.blesspearl.jobz4me

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blesspearl.jobz4me.databinding.ActivityViewJobBinding

class ViewJob : AppCompatActivity() {
    private lateinit var binding:ActivityViewJobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Job Details"
    }
}