package com.blesspearl.jobz4me.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blesspearl.jobz4me.Models.Jobz
import com.google.firebase.database.*

class UViewModel: ViewModel() {
    var list = MutableLiveData<List<Jobz>>()
    fun getJobz():LiveData<List<Jobz>>{
        val  mRef=FirebaseDatabase.getInstance().reference
        val query:Query = mRef.child("Jobs").child("Unofficial")
        val dtList:MutableList<Jobz> = ArrayList()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               for (ds in snapshot.children){
                   dtList.add(ds.getValue(Jobz::class.java)!!)
               }
                list.value=dtList
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return  list
    }
}