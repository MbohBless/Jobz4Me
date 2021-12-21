package com.blesspearl.jobz4me

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.blesspearl.jobz4me.Adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var pagerAdapter: PagerAdapter

    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
          supportActionBar?.title="Home"
        viewPager=findViewById(R.id.viewPager)
        tabLayout=findViewById(R.id.tabLayout)
        pagerAdapter= PagerAdapter(supportFragmentManager,3)

        viewPager.adapter = pagerAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem=tab.position
                if(tab.position==0||tab.position==1||tab.position==2)
                    pagerAdapter.notifyDataSetChanged()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(applicationContext).inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.my_profile->{
                val intent = Intent(this,ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.about_app->{
                Toast.makeText(this, "Not Yet clearly defined", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.contact->{
                val addresses = arrayOf<String>("mbohblesspearl@gmail.com")
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:") // only email apps should handle this
                    putExtra(Intent.EXTRA_EMAIL, addresses)
                }

                startActivity(intent)

                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

}