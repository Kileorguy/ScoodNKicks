package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.ViewModel.HomeViewModel
import edu.bluejack23_1.scoodnkicks.adapter.PopularLocationAdapter
import edu.bluejack23_1.scoodnkicks.databinding.ActivityAdminHomeBinding



class AdminHomeActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityAdminHomeBinding
    private lateinit var logOutGroup: LinearLayout
    private lateinit var showAll : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role = intent.getStringExtra("role").toString()

        val grid = binding.popularLocationGrid
        showAll = binding.locationPageHref
        logOutGroup = binding.logOutGroup
        val homeViewModel = HomeViewModel()
        val locations: ArrayList<LocationModel> = ArrayList()
        homeViewModel.getMostPopularLocation(this) { locationList ->
            for (location in locationList) {
                locations.add(location)
            }
            val adapter = PopularLocationAdapter(this, locations)
            grid.adapter = adapter


        }
        showAll.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra("role",role)
            startActivity(intent)
        }

        logOutGroup.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }

//        binding.userListBtn.setOnClickListener{
//            val intent = Intent(this, AdminUser::class.java)
//            intent.putExtra("role",role)
//            startActivity(intent)
//        }
//
//        binding.userListBtn.setOnClickListener {
//            val intent = Intent(this, UserListActivity::class.java)
//            startActivity(intent)
//        }
    }
}