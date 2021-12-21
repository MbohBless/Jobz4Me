package com.blesspearl.jobz4me.Fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blesspearl.jobz4me.Adapters.CourseAdapter
import com.blesspearl.jobz4me.Interfaces.UdemyApiInterface
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.Models.Courses
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.Models.Users
import com.blesspearl.jobz4me.R
import com.blesspearl.jobz4me.databinding.FragmentDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Details_Fragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var job: Jobz
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        job = activity?.intent!!.getSerializableExtra("Job") as Jobz
        binding.name.text = job.name

        binding.qualification.text = "Required Qualification: \n${job.qualification}"
        if (activity?.intent!!.hasExtra("Fragment")) {
            binding.category.visibility = GONE
            binding.qualification.visibility = GONE
        }
        binding.description.text = "Description:\n ${job.description}"
        binding.localName.text = "Place Name:\n ${job.local_name}"
        binding.date.text = "Apply by:\n ${job.required_date}"
        val database = Firebase.database
        val mRef =
            database.getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.getValue(Users::class.java)
                binding.postedBy.text = "Posted By:${name!!.name}"
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        binding.category.text = job.category
        binding.location.setOnClickListener {
            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = Details_FragmentDirections.actionDetailsFragmentToLocationFragment(
                job.location!!,
                job.local_name!!
            )
            navController.navigate(action)
        }
        binding.checkOutCourses.setOnClickListener {
            showSheet(job.category.toString())
        }
        val emls = arrayOf<String>()
        binding.contact.setOnClickListener {
            showContactSheet()
        }
        return binding.root
    }


    private fun displaySearchCourses(courseList: MutableList<Course>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CourseAdapter(courseList, requireActivity(), "search")
    }

    private fun getSearch(recyclerView: RecyclerView, progressBar: ProgressBar, search: String) {
        val udemyApiInterface = UdemyApiInterface.create().getSearch(search)
        var courseList: MutableList<Course> = ArrayList()
        udemyApiInterface.enqueue(object : Callback<Courses> {
            override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                if (response.isSuccessful) {
                    Log.d("TESTOPPER", "onResponse:${response.body()} ")
                    val courses: Courses = response.body()!!
                    courseList.addAll(courses.results)
                    progressBar.visibility = GONE
                    displaySearchCourses(courseList = courseList, recyclerView = recyclerView)
                } else {
                    Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Courses>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showSheet(search: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recycler_bottom)
        val progressBar: ProgressBar = dialog.findViewById(R.id.progress_bottom)
        getSearch(recyclerView = recyclerView, progressBar = progressBar, search = search)
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun showContactSheet() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.contact_bottom_sheet)
        val contactEmail = dialog.findViewById<TextView>(R.id.contact_email)
        contactEmail.setOnClickListener {
            Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show()
            val mRef = Firebase.database.getReference("Users").child(job.posted_by!!).child("email")
            mRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = snapshot.getValue(String::class.java)

                    val address = arrayOf(email)
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:") // only email apps should handle this
                        putExtra(Intent.EXTRA_EMAIL, address)
                    }
                    startActivity(intent)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
        val contactPhone: TextView = dialog.findViewById(R.id.contact_phone)
        contactPhone.setOnClickListener {
            val mRef =
                Firebase.database.getReference("Users").child(job.posted_by!!).child("number")
            mRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val number = snapshot.getValue(String::class.java)
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$number.")
                    }
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

}