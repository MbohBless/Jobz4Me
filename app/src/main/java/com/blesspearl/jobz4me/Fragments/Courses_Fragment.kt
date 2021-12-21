package com.blesspearl.jobs4me.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blesspearl.jobz4me.Adapters.CourseAdapter
import com.blesspearl.jobz4me.Interfaces.UdemyApiInterface
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.Models.Courses
import com.blesspearl.jobz4me.R
import com.blesspearl.jobz4me.databinding.FragmentCoursesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Courses_Fragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        binding.cat1.text = "Business"
        binding.cat2.text = "Design"
        binding.cat3.text = "Development"
        binding.cat4.text = "Lifestyle"
        binding.cat5.text = "Marketing"
        binding.cat6.text = "Music"

        callForDisplay(
            category = "Business",
            recyclerView = binding.recy1,
            progress = binding.progress1
        )
        callForDisplay("Design", binding.recy2, binding.progress2)
        callForDisplay("Development", binding.recy3, binding.progress3)
        callForDisplay("Lifestyle", binding.recy4, binding.progress4)
        callForDisplay("Marketing", binding.recy5, binding.progress5)
        callForDisplay("Music", binding.recy6, binding.progress6)

        binding.search.setOnClickListener {
            val search = binding.searchEd.text.toString()
            if (search.isNotBlank()) {
                val formString = search.trim().replace(" ", "+")
                showSheet(formString)
            } else {
                Toast.makeText(context, "cannot search an Empty String", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
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


    private fun callForDisplay(
        category: String,
        recyclerView: RecyclerView,
        progress: ProgressBar
    ) {
        val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
        var courseList: MutableList<Course> = ArrayList()
        udemyApiInterface.enqueue(object : Callback<Courses> {
            override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                if (response.isSuccessful) {
                    Log.d("TESTOPPER", "onResponse:${response.body()} ")
                    val courses: Courses = response.body()!!
                    courseList.addAll(courses.results)
                    progress.visibility = GONE
                    displayCourses(courseList = courseList, recyclerView = recyclerView)
                } else {
                    Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Courses>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun displayCourses(courseList: MutableList<Course>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CourseAdapter(courseList, requireActivity(), "cat")
    }

    private fun displaySearchCourses(courseList: MutableList<Course>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CourseAdapter(courseList, requireActivity(), "search")
    }


}