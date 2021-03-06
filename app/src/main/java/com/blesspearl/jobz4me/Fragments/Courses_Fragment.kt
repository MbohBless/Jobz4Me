package com.blesspearl.jobs4me.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blesspearl.jobz4me.Adapters.CourseAdapter
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.R
import com.blesspearl.jobz4me.VMFactories.HAViewModelFactory
import com.blesspearl.jobz4me.ViewModels.HomeActivityViewModel
import com.blesspearl.jobz4me.databinding.FragmentCoursesBinding


class Courses_Fragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding
    private lateinit var viewModel: HomeActivityViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val factory = HAViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeActivityViewModel::class.java]
        binding.cat1.text = "Business"
        binding.cat2.text = "Design"
        binding.cat3.text = "Development"
        binding.cat4.text = "Lifestyle"
        binding.cat5.text = "Marketing"
        binding.cat6.text = "Music"

        viewModel.callForDisplay1("Business", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy1)
            binding.progress1.visibility = GONE
        })
        viewModel.callForDisplay2("Design", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy2)
            binding.progress2.visibility = GONE
        })
        viewModel.callForDisplay3("Development", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy3)
            binding.progress3.visibility = GONE
        })
        viewModel.callForDisplay4("Lifestyle", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy4)
            binding.progress4.visibility = GONE
        })
        viewModel.callForDisplay5("Marketing", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy5)
            binding.progress5.visibility = GONE
        })
        viewModel.callForDisplay6("Music", requireContext()).observe(requireActivity(), {
            displayCourses(it, binding.recy6)
            binding.progress6.visibility = GONE
        })
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
        viewModel.getSearch(search = search).observe(requireActivity(), {
            displaySearchCourses(it, recyclerView)
            progressBar.visibility = GONE
        })
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }

    private fun displayCourses(courseList: List<Course>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CourseAdapter(courseList, requireActivity(), "cat")
    }

    private fun displaySearchCourses(courseList: List<Course>, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CourseAdapter(courseList, requireActivity(), "search")
    }


}