package com.blesspearl.jobz4me.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blesspearl.jobz4me.CourseDetailsActivity
import com.blesspearl.jobz4me.Models.Course
import com.blesspearl.jobz4me.R
import com.bumptech.glide.Glide

class CourseAdapter(var courses: List<Course>, var activity: Context, var type: String) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_course)
        val title: TextView = itemView.findViewById(R.id.title)
        val headline: TextView = itemView.findViewById(R.id.headline)
        val cost: TextView = itemView.findViewById(R.id.cost)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        val view1 =
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_long, parent, false)
        if (type == "cat") {
            return CourseViewHolder(view)
        } else {
            return CourseViewHolder(view1)
        }

    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.title.text = course.title
        holder.cost.text = course.price
        holder.headline.text = course.headline
        Glide.with(activity).load(course.image_480x270).centerCrop().into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(activity, CourseDetailsActivity::class.java)
            intent.putExtra("Course", course)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size

}