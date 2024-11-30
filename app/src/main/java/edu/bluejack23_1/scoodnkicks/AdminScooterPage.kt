package edu.bluejack23_1.scoodnkicks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.TextView
import edu.bluejack23_1.scoodnkicks.Model.ScooterModel
import edu.bluejack23_1.scoodnkicks.ViewModel.ScooterViewModel
import edu.bluejack23_1.scoodnkicks.adapter.ScooterAdapter
import edu.bluejack23_1.scoodnkicks.databinding.ActivityAdminScooterPageBinding

class AdminScooterPage : AppCompatActivity() {
    private lateinit var binding: ActivityAdminScooterPageBinding
    private lateinit var grid: GridView
    private lateinit var locName : TextView
    override fun onCreate(savedInstanceState: Bundle?) {

//        intent.putExtra("id", currItem.id)
//        intent.putExtra("locationName",currItem.name)
//        intent.putExtra("availableScooter", currItem.availableScooter)
//        intent.putExtra("maxScooter",currItem.maxScooter)
        super.onCreate(savedInstanceState)

        binding = ActivityAdminScooterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("id").toString()
        val locationName = intent.getStringExtra("locationName").toString()
        val role = intent.getStringExtra("role").toString()
        val isBorrowing = intent.getBooleanExtra("isBorrowing", false)

        locName = binding.locationName
        locName.text = locationName



        grid = binding.scooterLocationGrid
        val scooterList: ArrayList<ScooterModel> = ArrayList()
        ScooterViewModel.getScooter(id,this){
            if (it != null) {
                scooterList.clear()
                if(role.equals("admin")){
                    scooterList.add(ScooterModel(-5, "None", false))
                }
                for (i in it){
                    Log.d("Location Model", i.toString())
                    scooterList.add(i)
                }
                val adapter = ScooterAdapter(this, scooterList,id,locationName,role,isBorrowing)
                grid.adapter = adapter
            }
        }

        binding.bckBtn.setOnClickListener{
            this.finish()
        }
    }
}