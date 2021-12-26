package com.blesspearl.jobz4me.ViewModels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blesspearl.jobz4me.Interfaces.UdemyApiInterface
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.Models.Courses
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityViewModel: ViewModel() {
    var list:MutableLiveData<List<Jobz>>?=null
    var list1:MutableLiveData<List<Jobz>>?=null
    var list2:MutableLiveData<List<Course>>?=null
    var list3:MutableLiveData<List<Course>>?=null
    var list4:MutableLiveData<List<Course>>?=null
    var list5:MutableLiveData<List<Course>>?=null
    var list6:MutableLiveData<List<Course>>?=null
    var list7:MutableLiveData<List<Course>>?=null
    fun getUnclassifiedJobs():LiveData<List<Jobz>>{
        if (list==null){
            list= MutableLiveData()
            val  mRef=FirebaseDatabase.getInstance().reference
            val query:Query = mRef.child("Jobs").child("Unofficial")
            val dtList:MutableList<Jobz> = ArrayList()
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        dtList.add(ds.getValue(Jobz::class.java)!!)
                    }
                    list!!.value=dtList
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        return list as MutableLiveData<List<Jobz>>
    }
    fun checkNumber(activity:Activity){
        val mRef = Firebase.database.getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("number")
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val number = snapshot.getValue(String::class.java)
                if (number.isNullOrBlank()) {
                    val intent = Intent(activity, ProfileActivity ::class.java)
                    intent.putExtra("Number", "Number")
                    activity.startActivity(intent)
                }
                else{
                    Toast.makeText(activity, "its fine", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun getClassifiedJobs():LiveData<List<Jobz>>{
        if ( list1==null){
            list1 = MutableLiveData()
            val  mRef=FirebaseDatabase.getInstance().reference
            val query:Query = mRef.child("Jobs").child("Professional")
            val dtList:MutableList<Jobz> = ArrayList()
            with(query) {
                addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children){
                            dtList.add(ds.getValue(Jobz::class.java)!!)
                        }
                        list1!!.value=dtList
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }

        return list1 as MutableLiveData<List<Jobz>>
    }

    fun callForDisplay1(category: String, context: Context):LiveData<List<Course>> {
          if (list2==null){
              list2= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list2!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list2 as MutableLiveData<List<Course>>
    }
    fun callForDisplay2(category: String, context: Context):LiveData<List<Course>> {
          if (list3==null){
              list3= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list3!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list3 as MutableLiveData<List<Course>>
    }
    fun callForDisplay3(category: String, context: Context):LiveData<List<Course>> {
          if (list4==null){
              list4= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list4!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list4 as MutableLiveData<List<Course>>
    }
    fun callForDisplay4(category: String, context: Context):LiveData<List<Course>> {
          if (list5==null){
              list5= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list5!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list5 as MutableLiveData<List<Course>>
    }
    fun callForDisplay5(category: String, context: Context):LiveData<List<Course>> {
          if (list6==null){
              list6= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list6!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list6 as MutableLiveData<List<Course>>
    }
    fun callForDisplay6(category: String, context: Context):LiveData<List<Course>> {
          if (list7==null){
              list7= MutableLiveData()
              val udemyApiInterface = UdemyApiInterface.create().getCategories(category)
              val courseList: MutableList<Course> = ArrayList()
              udemyApiInterface.enqueue(object : Callback<Courses> {
                  override fun onResponse(call: Call<Courses>, response: Response<Courses>) {
                      if (response.isSuccessful) {
                          Log.d("TESTOPPER", "onResponse:${response.body()} ")
                          val courses: Courses = response.body()!!
                          courseList.addAll(courses.results)
                          list7!!.value=courseList
                      } else {
                          Toast.makeText(context, "${response.code()}hello", Toast.LENGTH_SHORT).show()
                      }
                  }
                  override fun onFailure(call: Call<Courses>, t: Throwable) {
                      Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                  }
              })
          }

          return list7 as MutableLiveData<List<Course>>
    }

}