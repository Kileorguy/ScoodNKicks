package edu.bluejack23_1.scoodnkicks.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_1.scoodnkicks.AdminScooterPage
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.R

class LocationAdapter(private var dataList: ArrayList<LocationModel>,
                      private val ctx: Context,
                      private val role : String,
                    private val isBorrowing : Boolean,
                    private val scooterCode : String

) :
    RecyclerView.Adapter<LocationAdapter.ViewHolderClass>() {

    fun updateData(newData: ArrayList<LocationModel>){
        this.dataList = newData
    }
    class ViewHolderClass(itemView : View): RecyclerView.ViewHolder(itemView)  {
        val rvLocationName : TextView  = itemView.findViewById(R.id.location_name_lbl)
        val rvLocationAddress : TextView  = itemView.findViewById(R.id.location_address_lbl)
        val rvAvailable : TextView  = itemView.findViewById(R.id.avail_lbl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item_layout,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currItem = dataList[position]

        holder.rvLocationName.text = currItem.name
        holder.rvLocationAddress.text = currItem.address
        holder.rvAvailable.text = currItem.availableScooter.toString()+"/"+ currItem.maxScooter.toString()+"Available"

        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, AdminScooterPage::class.java)
            intent.putExtra("id", currItem.id)
            intent.putExtra("locationName",currItem.name)
            intent.putExtra("availableScooter", currItem.availableScooter)
            intent.putExtra("maxScooter",currItem.maxScooter)
            intent.putExtra("role",role)
            intent.putExtra("isBorrowing", isBorrowing)
            intent.putExtra("scooterCode", scooterCode)
            ctx.startActivity(intent)
        }

    }

}