package edu.bluejack23_1.scoodnkicks


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.ViewModel.LocationViewModel
import edu.bluejack23_1.scoodnkicks.adapter.LocationAdapter
import edu.bluejack23_1.scoodnkicks.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLocationBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var addLocationBtn : Button
    private lateinit var searchBar : EditText
    private lateinit var sortButton : ImageView

    private var firstFetch = false
    private var sortAsc = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)

        searchBar = binding.searchBar
        addLocationBtn = binding.addLocationBtn

        val role = intent.getStringExtra("role").toString()
        val email = intent.getStringExtra("email")
        val isBorrowing = intent.getBooleanExtra("isBorrowing",false)
        val scooterCode = intent.getStringExtra("scooterCode").toString()

        if(!role.equals("admin")){
            addLocationBtn.visibility = View.GONE
        }else{
            addLocationBtn.visibility = View.VISIBLE
        }
        sortButton = binding.sortBtn
        setContentView(binding.root)

        recyclerView = findViewById(R.id.RV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        getData(role,isBorrowing,scooterCode)

        searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filterData(p0.toString())
            }

        })

        sortButton.setOnClickListener {
            sortData()
        }
        addLocationBtn.setOnClickListener{
            displayModal()
        }

        binding.bckBtn.setOnClickListener{
            this.finish()
        }

    }

    private fun sortData(){
        LocationViewModel.setRVData {
                dataList ->
            if(dataList != null){
                if(!sortAsc){
                    sortAsc = true
                    val locationAdapter : LocationAdapter = recyclerView.adapter as LocationAdapter
                    var newData = dataList.sortBy { it.availableScooter }
                    val newArrayList: ArrayList<LocationModel> = ArrayList(dataList)
                    locationAdapter.updateData(newArrayList)
                    locationAdapter.notifyDataSetChanged()

                }else{
                    sortAsc = false
                    val locationAdapter : LocationAdapter = recyclerView.adapter as LocationAdapter
                    var newData = dataList.sortByDescending { it.availableScooter }
                    val newArrayList: ArrayList<LocationModel> = ArrayList(dataList)
                    locationAdapter.updateData(newArrayList)
                    locationAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun filterData(filter : String){
        LocationViewModel.setRVData {
                dataList ->
            if(dataList != null){
                var newData = dataList.filter { it.name.contains(filter) || it.address.contains(filter) }
                var newArrayList : ArrayList<LocationModel> = ArrayList(newData)
                val locationAdapter : LocationAdapter = recyclerView.adapter as LocationAdapter
                locationAdapter.updateData(newArrayList)
                locationAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun displayModal(){
        Log.d("Display Modal","Hehe")
        val builder = AlertDialog.Builder(this)
        val modal = LayoutInflater.from(this).inflate(R.layout.add_location_dialog,null)
        builder.setView(modal)
        val dialog = builder.create()

        val closeBtn = modal.findViewById<Button>(R.id.close_modal)
        val addLocationBtn = modal.findViewById<Button>(R.id.add_location_btn)

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        addLocationBtn.setOnClickListener {
            val nameField = modal.findViewById<EditText>(R.id.location_name_field)
            val addressField = modal.findViewById<EditText>(R.id.location_address_field)
            val name = nameField.text.toString()
            val address = addressField.text.toString()
            val check = LocationViewModel.addData(name,address,this)
            if(check){
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun getData(role : String, isBorrowing: Boolean, scooterCode: String){

        LocationViewModel.setRVData {
            dataList ->

            if(dataList != null){
                if(!firstFetch){
                    firstFetch = true
                    var adapter = LocationAdapter(dataList, this,role,isBorrowing,scooterCode)
//                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
//                recyclerView.adapter.notifyDataSetChanged()
                }else{
                    Log.d("Realtime Callback NotFirst", dataList.toString())
                    val locationAdapter : LocationAdapter = recyclerView.adapter as LocationAdapter
                    locationAdapter.updateData(dataList)
                    locationAdapter.notifyDataSetChanged()
                }

            }else{
                recyclerView.adapter = LocationAdapter(arrayListOf<LocationModel>(),this,role, isBorrowing, scooterCode)
            }
        }

    }
}