package com.blesspearl.jobz4me.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blesspearl.jobz4me.Adapters.JobAdapter
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.PostJobActivity
import com.blesspearl.jobz4me.VMFactories.HAViewModelFactory
import com.blesspearl.jobz4me.ViewModels.HomeActivityViewModel
import com.blesspearl.jobz4me.databinding.FragmentUnclassifiedJobsBinding


class UnclassifiedJobs : Fragment() {
    private lateinit var binding: FragmentUnclassifiedJobsBinding
    private lateinit var viewModel: HomeActivityViewModel

    companion object {
        const val U_JOB_POST = "FRAGMENT_CLASSIFIED_JOBS"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnclassifiedJobsBinding.inflate(inflater, container, false)
        val factory = HAViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeActivityViewModel::class.java]
        binding.unclassifiedClass.setOnClickListener {
            openPostActivity()
        }
        viewModel.getUnclassifiedJobs().observe(requireActivity(), {
            displayJobs(it)
        })
        viewModel.checkNumber(requireActivity())

        return binding.root
    }

    private fun openPostActivity() {
        val intent = Intent(context, PostJobActivity::class.java)
        intent.putExtra(U_JOB_POST, U_JOB_POST)
        startActivity(intent)
    }

    private fun displayJobs(jobList: List<Jobz>) {
        binding.unClassifiedRecycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.unClassifiedRecycle.adapter = JobAdapter(jobList, "Unclassified", requireActivity())
    }


}

