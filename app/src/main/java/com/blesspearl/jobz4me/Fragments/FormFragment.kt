package com.blesspearl.jobz4me.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.blesspearl.jobz4me.Fragments.ProfessionalJobs.Companion.P_JOB_POST
import com.blesspearl.jobz4me.Fragments.UnclassifiedJobs.Companion.U_JOB_POST
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.R
import com.blesspearl.jobz4me.databinding.FragmentFormFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class FormFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentFormFragmentBinding
    private lateinit var mAuth: FirebaseAuth
    private var category: String? = null
    private val args: FormFragmentArgs by navArgs()
    private lateinit var dat: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentFormFragmentBinding.inflate(inflater, container, false)

        binding.calender.setOnClickListener {
            selectDate()
        }
        if (args.location == "none") {
            Log.d("FormFragment", "onCreateView: the location is null ")
        } else {
            val location = args.location
            val split = location.split(",")
            binding.locationLatEd.setText(split[0])
            binding.locaionLonEd.setText(split[1])
            Toast.makeText(requireContext(), location, Toast.LENGTH_SHORT).show()
        }
        if (activity?.intent!!.hasExtra(U_JOB_POST)) {
            binding.categorySp.isEnabled = false
            binding.qualificationEd.visibility = GONE
            binding.qualificationTx.visibility = GONE
        }

        binding.btnSubmit.setOnClickListener {
            submitJob()
        }
        binding.postNameEd.setText(mAuth.currentUser?.displayName)
        binding.mapInent.setOnClickListener {
            val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_frag) as NavHostFragment
            val navController = navHostFragment.navController
            val action = FormFragmentDirections.actionFormFragmentToMapFragment()
            navController.navigate(action)
        }
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.planets_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                binding.categorySp.adapter = adapter
                binding.categorySp.onItemSelectedListener = this
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().finish()
        }
        return binding.root
    }

    private fun submitJob() {
        if (binding.nameEd.text.isNullOrBlank() || binding.comNameEd.text.isNullOrBlank() || binding.reqDateTv.text.isNullOrBlank() || binding.locaionLonEd.text.isNullOrEmpty() || binding.locationLatEd.text.isNullOrEmpty()) {
            Toast.makeText(context, "some of the fields cannot be left empty", Toast.LENGTH_SHORT)
                .show()
        } else {
            val name = binding.nameEd.text.toString()
            val description = binding.descriptionEd.text.toString()
            val qualification = binding.qualificationEd.text.toString()
            val cat = category.toString()
            val location = "${binding.locationLatEd.text},${binding.locaionLonEd.text}"
            val localName = binding.comNameEd.text.toString()
            val payRange = binding.payEd.text.toString()
            Toast.makeText(context, "uploading", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.VISIBLE
            val database = Firebase.database
            val myRef = database.getReference("Jobs")
            if (activity?.intent!!.hasExtra(U_JOB_POST)) {
                myRef.child("Unofficial")
                    .push()
                    .setValue(mAuth.currentUser?.let {
                        Jobz(
                            name, description, null, cat, location, localName, payRange, dat,
                            it.uid
                        )
                    })
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val mRef = Firebase.database.getReference("IndividualJobs")
                                .child(mAuth.currentUser!!.uid)
                            mRef.push().setValue(mAuth.currentUser?.let {
                                Jobz(
                                    name,
                                    description,
                                    qualification,
                                    cat,
                                    location,
                                    localName,
                                    payRange,
                                    dat,
                                    it.uid
                                )
                            }).addOnCompleteListener { ask ->
                                if (ask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "successfully Posted Job",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBar.visibility = View.GONE

                                    requireActivity().finish()
                                }
                            }
                        }
                    }

            } else if (activity?.intent!!.hasExtra(P_JOB_POST)) {
                myRef.child("Professional")
                    .push()
                    .setValue(mAuth.currentUser?.let {
                        Jobz(
                            name,
                            description,
                            qualification,
                            cat,
                            location,
                            localName,
                            payRange,
                            dat,
                            it.uid
                        )
                    })
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val mRef = Firebase.database.getReference("IndividualJobs")
                                .child(mAuth.currentUser!!.uid)
                            mRef.push().setValue(mAuth.currentUser?.let {
                                Jobz(
                                    name,
                                    description,
                                    qualification,
                                    cat,
                                    location,
                                    localName,
                                    payRange,
                                    dat,
                                    it.uid
                                )
                            }).addOnCompleteListener { ask ->
                                if (ask.isSuccessful) {
                                    val mRef = Firebase.database.getReference("IndividualJobs")
                                        .child(mAuth.currentUser!!.uid)
                                    mRef.push().setValue(mAuth.currentUser?.let {
                                        Jobz(
                                            name,
                                            description,
                                            qualification,
                                            cat,
                                            location,
                                            localName,
                                            payRange,
                                            dat,
                                            it.uid
                                        )
                                    }).addOnCompleteListener { ask ->
                                        if (ask.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "successfully Posted Job",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            binding.progressBar.visibility = View.GONE

                                            requireActivity().finish()
                                        }
                                    }
//
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun selectDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = context?.let { DatePickerDialog(it, this, year, month, day) }
        datePickerDialog?.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        var mnth = month + 1
        dat = "$day/$mnth/$year"
        binding.reqDateTv.text = dat
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        category = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        category = p0?.getItemAtPosition(0).toString()
    }

}

