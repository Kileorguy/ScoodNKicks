package edu.bluejack23_1.scoodnkicks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.Timestamp
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.ViewModel.HomeViewModel
import edu.bluejack23_1.scoodnkicks.adapter.PopularLocationAdapter
import edu.bluejack23_1.scoodnkicks.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var carousel: ImageSlider
    private lateinit var grid: GridView
    private lateinit var locationHRef: TextView
    private lateinit var drawer: DrawerLayout
    private lateinit var burgar: ImageView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val role = intent.getStringExtra("role")
        val email = intent.getStringExtra("email")
        var U_isBorrowing: Boolean = false
        var U_ScooterCode: String = ""
        var U_deadline: Timestamp
        HomeViewModel.getUser(email.toString()) { user ->
            if (user != null) {
                if (user.getBoolean("isBorrowing") != null) {
                    U_isBorrowing = user.getBoolean("isBorrowing") as Boolean
                } else if (user.getString("scooterCode") != null) {
                    U_ScooterCode = user.getString("scooterCode").toString()
                }
//                U_deadline = user.get("deadline") as Timestamp
            }
        }

        burgar = binding.Burgar
        drawer = binding.drawerLayout
        burgar.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
        binding.sidebar.bringToFront()

        binding.sidebar.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    drawer.closeDrawer(GravityCompat.START)
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra("role", role)
                    intent.putExtra("email", email)
                    intent.putExtra("isBorrowing", U_isBorrowing)
                    intent.putExtra("scooterCode", U_ScooterCode)

                    startActivity(intent)
                    true
                }

                R.id.nav_history -> {
                    val intent = Intent(this, UserHistoryActivity::class.java)
                    intent.putExtra("role", role)
                    intent.putExtra("email", email)
                    intent.putExtra("isBorrowing", U_isBorrowing)
                    intent.putExtra("scooterCode", U_ScooterCode)
                    startActivity(intent)
                    true
                }

                R.id.nav_location -> {
                    val intent = Intent(this, LocationActivity::class.java)
                    intent.putExtra("role", role)
                    intent.putExtra("email", email)
                    intent.putExtra("isBorrowing", U_isBorrowing)
                    intent.putExtra("scooterCode", U_ScooterCode)
                    startActivity(intent)
                    true
                }

                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("role", role)
                    intent.putExtra("email", email)
                    intent.putExtra("isBorrowing", U_isBorrowing)
                    intent.putExtra("scooterCode", U_ScooterCode)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                else -> {
                    true
                }
            }
        }


        carousel = binding.carousel
        locationHRef = binding.locationPageHref
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.scooter_banner_1, ""))
        imageList.add(SlideModel(R.drawable.scooter_banner_2, ""))
        imageList.add(SlideModel(R.drawable.scooter_banner_3, ""))
        carousel.setImageList(imageList, ScaleTypes.CENTER_INSIDE)


        locationHRef.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra("role", role)
            intent.putExtra("email", email)
            intent.putExtra("isBorrowing", U_isBorrowing)
            intent.putExtra("scooterCode", U_ScooterCode)
            startActivity(intent)
        }

        grid = binding.popularLocationGrid
        val homeViewModel = HomeViewModel()
        val locations: ArrayList<LocationModel> = ArrayList()
        homeViewModel.getMostPopularLocation(this) { locationList ->
            for (location in locationList) {
                locations.add(location)
            }
            val adapter = PopularLocationAdapter(this, locations)
            grid.adapter = adapter
        }

        val borrowingContainer = binding.borrowing
        HomeViewModel.getUser(email.toString()) {
            if (it != null) {
                var borrow = false
                if (it.getBoolean("isBorrowing") != null) {
                    borrow = it.getBoolean("isBorrowing") as Boolean
                }
                if (borrow) {
                    val borrowingScooterID = binding.borrowingScooterId
                    borrowingScooterID.text = it.getString("scooterCode")
                    val deadline = binding.deadlineBorrowing

                    val timestamp1: Timestamp = it.get("deadline") as Timestamp
                    val timestamp2: Timestamp = Timestamp.now()

                    val diff = (timestamp1.toDate().time - timestamp2.toDate().time) / 1000 / 60

                    deadline.text = " $diff Minutes"


                } else {
                    borrowingContainer.visibility = View.GONE
                }
            } else {
                borrowingContainer.visibility = View.GONE
            }
        }

    }
}