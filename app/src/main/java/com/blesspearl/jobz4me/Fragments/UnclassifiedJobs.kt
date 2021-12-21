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
import com.blesspearl.jobz4me.Models.Users
import com.blesspearl.jobz4me.PostJobActivity
import com.blesspearl.jobz4me.ProfileActivity
import com.blesspearl.jobz4me.databinding.FragmentUnclassifiedJobsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UnclassifiedJobs : Fragment() {
    private lateinit var binding: FragmentUnclassifiedJobsBinding

    companion object {
        const val U_JOB_POST = "FRAGMENT_CLASSIFIED_JOBS"
    }

    private var jobList: MutableList<Jobz> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnclassifiedJobsBinding.inflate(inflater, container, false)
        binding.unclassifiedClass.setOnClickListener {
            openPostActivity()
        }
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


        val database = Firebase.database
        val myRef = database.getReference("Jobs").child("Unofficial")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
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

