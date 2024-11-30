package edu.bluejack23_1.scoodnkicks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel
import edu.bluejack23_1.scoodnkicks.ViewModel.UserHistoryViewModel
import edu.bluejack23_1.scoodnkicks.adapter.UserHistoryAdapter
import edu.bluejack23_1.scoodnkicks.databinding.ActivityUserHistoryBinding

class UserHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserHistoryBinding
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var email = intent.getStringExtra("email").toString()
        val isBorrowing = intent.getBooleanExtra("isBorrowing",false)
        val scooterCode = intent.getStringExtra("scooterCode").toString()

        recyclerView = binding.RV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        setRVData(email,scooterCode)

    }

    private fun setRVData(email : String,scooterCode : String){
        val isBorrowing = intent.getBooleanExtra("isBorrowing",false)
//        val scooterCode = intent.getStringExtra("scooterCode").toString()
        UserHistoryViewModel.getRVData(email,isBorrowing,"gajadi",this){list ->
            if(list!=null){

                Log.d("RVTest","berhasil")
                var header = HistoryModel("0", "SLC", "69", "header", "sn@gmail.com", 420)
                if(list[0] != header){
                    list.add(0, header)
                }
                recyclerView.adapter = UserHistoryAdapter(list ,this,"User")
            }else{
                recyclerView.adapter = UserHistoryAdapter(ArrayList() ,this,"User")

            }
        }

    }
}