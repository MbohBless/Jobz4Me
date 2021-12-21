package com.blesspearl.jobz4me.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blesspearl.jobz4me.Models.Jobz
import com.blesspearl.jobz4me.R
import com.blesspearl.jobz4me.ViewJob


class JobAdapter(var jobs: List<Jobz>, var type: String, var activity: Context) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {
    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val category: TextView = itemView.findViewById(R.id.category)
        val qualifications: TextView = itemView.findViewById(R.id.qualifications)
        val description: TextView = itemView.findViewById(R.id.description)
        val card: CardView = itemView.findViewById(R.id.card)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobAdapter.JobViewHolder, position: Int) {
        val job = jobs[position]
        val intent = Intent(activity, ViewJob::class.java)
        intent.putExtra("Job", job)
        holder.title.text = job.name
        holder.qualifications.text = job.qualification
        holder.category.text = job.category

        if (type == "Unclassified") {
            holder.category.text = "Unclassified"
            holder.qualifications.visibility = GONE
            holder.image.setImageResource(R.drawable.ic_local)
            intent.putExtra("Fragment", "Unclassified")
        }
        holder.description.text = job.description
        holder.card.setOnClickListener {
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = jobs.size

}