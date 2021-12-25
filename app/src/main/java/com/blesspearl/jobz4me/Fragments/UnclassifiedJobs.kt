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
import com.blesspearl.jobz4me.Models.Users
import com.blesspearl.jobz4me.PostJobActivity
import com.blesspearl.jobz4me.ProfileActivity
import com.blesspearl.jobz4me.VMFactories.UViewModelFactory
import com.blesspearl.jobz4me.ViewModels.UViewModel
import com.blesspearl.jobz4me.databinding.FragmentUnclassifiedJobsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UnclassifiedJobs : Fragment(){
    private lateinit var binding: FragmentUnclassifiedJobsBinding
    private lateinit var viewModel: UViewModel

    companion object {
        const val U_JOB_POST = "FRAGMENT_CLASSIFIED_JOBS"
    }

    private var jobList: MutableList<Jobz> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnclassifiedJobsBinding.inflate(inflater, container, false)

        val factory = UViewModelFactory()
        viewModel = ViewModelProvider(this,factory)[UViewModel::class.java]
        binding.unclassifiedClass.setOnClickListener {
            openPostActivity()
        }
        viewModel.getJobz().observe(requireActivity(), {
            displayJobs(it)
        })

        val mRef = Firebase.database.getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                if (user?.number == null) {
                    val intent = Intent(requireActivity(), ProfileActivity::class.java)
                    intent.putExtra("Number", "Number")
                    requireActivity().startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        return binding.root
    }

    private fun openPostActivity() {
        val intent = Intent(context, PostJobActivity::class.java)
        intent.putExtra(U_JOB_POST, U_JOB_POST)
        startActivity(intent)
    }

    private fun displayJobs(jobList: List<Jobz>) {
        binding.unClassifiedRecycle.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.unClassifiedRecycle.adapter = JobAdapter(jobList, "Unclassified", requireActivity())
    }




}

