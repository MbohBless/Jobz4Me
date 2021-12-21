package com.blesspearl.jobz4me.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blesspearl.jobs4me.Fragments.Courses_Fragment
import com.blesspearl.jobz4me.Fragments.ProfessionalJobs
import com.blesspearl.jobz4me.Fragments.UnclassifiedJobs

class PagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    var tabCount = behavior
    override fun getCount() = tabCount
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return UnclassifiedJobs()
            }
            1 -> {
                return ProfessionalJobs()
            }
            2 -> {
                return Courses_Fragment()
            }
            else -> {
                throw IllegalStateException("$position is illegal")
            }
        }


    }


}