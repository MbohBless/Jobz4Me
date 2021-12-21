package com.blesspearl.jobz4me

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blesspearl.jobz4me.Adapters.JobAdapter
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.Models.Users
import com.blesspearl.jobz4me.databinding.ActivityProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var  binding:ActivityProfileBinding
    private var  jobList: MutableList<Jobz> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="My Profile"
        binding.btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("finish", true);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
            startActivity(intent)
            this.finish()
        }
        if (intent.hasExtra("Number")){
            Toast.makeText(this, "Please Set your Phone number", Toast.LENGTH_SHORT).show()
        }
        val auth=FirebaseAuth.getInstance().currentUser
        Glide.with(this@ProfileActivity).load(auth!!.photoUrl).into(binding.userImage)
        binding.userEmail.text = auth.email
        binding.userName.text = auth.displayName

        val mRef = Firebase.database.getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                binding.edPhoneNumber.setText(user!!.number)
                if(binding.edPhoneNumber.text!=null){
                    binding.btnArrange.visibility= View.VISIBLE
                    binding.btnArrange.setOnClickListener {
                        if(binding.edPhoneNumber.text.length>13||binding.edPhoneNumber.text.length<9){
                            Toast.makeText(this@ProfileActivity, "In appropriate length for phone number", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            mRef.child("number").setValue(binding.edPhoneNumber.text.toString())
                                .addOnCompleteListener {
                                    finish()
                                    Toast.makeText(this@ProfileActivity, "successfully modified", Toast.LENGTH_SHORT)  .show()
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val database= Firebase.database
        val  myRef =database.getReference("IndividualJobs").child(FirebaseAuth.getInstance().currentUser!!.uid)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    jobList.add(ds.getValue(Jobz::class.java)!!)
                }
                displayJobs(jobList)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "hello sorry we are having some technical difficulties at the moment", Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun displayJobs(jobList: List<Jobz>) {
        binding.recyclerView.layoutManager=
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter= JobAdapter(jobList,"Unclassified", this)
    }
}