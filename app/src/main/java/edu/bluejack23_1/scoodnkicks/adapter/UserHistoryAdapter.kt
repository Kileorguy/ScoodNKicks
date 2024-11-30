package edu.bluejack23_1.scoodnkicks.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.R
import org.w3c.dom.Text

class UserHistoryAdapter (private var dataList: ArrayList<HistoryModel>, private val ctx: Context, private val role : String)
    : RecyclerView.Adapter<UserHistoryAdapter.ViewHolderClass>(){

    fun updateData(newData: ArrayList<HistoryModel>){
        this.dataList = newData
    }

    class ViewHolderClass(itemView : View): RecyclerView.ViewHolder(itemView)  {
        val rvScooterId : TextView = itemView.findViewById(R.id.scooter_id_label)
        val rvPlace : TextView = itemView.findViewById(R.id.place_label)
        val rvLate : TextView = itemView.findViewById(R.id.time_label)
        val rvType : TextView = itemView.findViewById(R.id.type_label)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHistoryAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item_layout,parent,false)
        return UserHistoryAdapter.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: UserHistoryAdapter.ViewHolderClass, position: Int) {
        if(dataList[position].scooterID == "0"){
            holder.rvLate.text = "Late"
            holder.rvPlace.text = "Place"
            holder.rvScooterId.text = "ID"
            holder.rvType.text = "Type"
        }else{
            val currItem = dataList[position]
            val time :Timestamp = currItem.time as Timestamp
            holder.rvLate.text = currItem.late.toString() + " Minute(s)"
            holder.rvPlace.text = currItem.place
            holder.rvScooterId.text = currItem.scooterID
            holder.rvType.text = currItem.type
            if(currItem.late != 0 && currItem.type != "Current"){
                holder.rvPlace.setTextColor(Color.RED)
                holder.rvLate.setTextColor(Color.RED)
                holder.rvScooterId.setTextColor(Color.RED)
                holder.rvType.setTextColor(Color.RED)
            }
            else if(currItem.late == 0 && currItem.type != "Current"){
                holder.rvLate.text = "-"
            }
        }
    }


}