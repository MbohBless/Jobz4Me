package com.blesspearl.jobz4me.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blesspearl.jobz4me.Adapters.JobAdapter
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.PostJobActivity
import com.blesspearl.jobz4me.databinding.FragmentProfessionalJobsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfessionalJobs : Fragment() {
    companion object {
        const val P_JOB_POST = "FRAGMENT_PROFESSIONAL_JOBS"
    }

    private lateinit var binding: FragmentProfessionalJobsBinding
    private var jobList: MutableList<Jobz> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = Firebase.database
        val myRef = database.getReference("Jobs").child("Professional")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    jobList.add(ds.getValue(Jobz::class.java)!!)
                }
                displayJobs(jobList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "hello sorry we are having some technical difficulties at the moment",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding = FragmentProfessionalJobsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.professionalClass.setOnClickListener {
            openPostActivity()
        }

        return binding.root
    }

    init {

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