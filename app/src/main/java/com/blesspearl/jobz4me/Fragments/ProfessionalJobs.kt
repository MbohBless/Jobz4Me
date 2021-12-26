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
import com.blesspearl.jobz4me.databinding.FragmentProfessionalJobsBinding

class ProfessionalJobs : Fragment() {
    companion object {
        const val P_JOB_POST = "FRAGMENT_PROFESSIONAL_JOBS"
    }
    private lateinit var binding: FragmentProfessionalJobsBinding
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfessionalJobsBinding.inflate(inflater, container, false)
        val factory = HAViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeActivityViewModel::class.java]
        viewModel.getClassifiedJobs().observe(requireActivity(), {
            displayJobs(it)
        })
        binding.professionalClass.setOnClickListener {
            openPostActivity()
        }
        return binding.root
    }

    private fun openPostActivity() {
        val intent = Intent(context, PostJobActivity::class.java)
        intent.putExtra(P_JOB_POST, P_JOB_POST)
        startActivity(intent)
    }

    private fun displayJobs(jobList: List<Jobz>) {
        binding.professionalRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.professionalRecycler.adapter =
            JobAdapter(jobList, "Professional", requireActivity())
    }

}