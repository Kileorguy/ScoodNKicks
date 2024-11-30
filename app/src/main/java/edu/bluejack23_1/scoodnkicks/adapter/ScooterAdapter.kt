package edu.bluejack23_1.scoodnkicks.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import edu.bluejack23_1.scoodnkicks.Model.ScooterModel
import edu.bluejack23_1.scoodnkicks.R
import edu.bluejack23_1.scoodnkicks.ViewModel.ScooterViewModel

class ScooterAdapter (context: Context, scooterModelList: ArrayList<ScooterModel>, private val id : String, private val locationName : String, private val role : String, private val isBorrowing : Boolean):
    ArrayAdapter<ScooterModel?>(context, 0, scooterModelList as List<ScooterModel?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.scooter_card, parent, false)
            val scooterModel: ScooterModel? = getItem(position)
            val scooterID = listItemView!!.findViewById<TextView>(R.id.scooter_number)
            val scooterImg = listItemView!!.findViewById<ImageView>(R.id.image_scooter)
            val linearLayout: LinearLayout = listItemView!!.findViewById(R.id.linear_layout)
            if (scooterModel != null) {
                if(scooterModel.isUsed){
                    linearLayout.setBackgroundColor(Color.RED)
                }else{
                    linearLayout.setBackgroundColor(Color.GREEN)
                }
                if(scooterModel.id == -5){
                    scooterID.visibility = View.GONE
                    scooterImg.setImageResource(R.drawable.ic_baseline_plus)

                } else {
                    scooterID.text = "Scooter ${scooterModel.id}"
                }
                listItemView.setOnClickListener {
                    if(scooterModel.id == -5){
                        ScooterViewModel.addScooter(id,scooterModel,context)
                    }else{
                        if(!scooterModel.isUsed){
                            if(role == "admin"){
                                displayModal(scooterModel.id,locationName,id)
                            }else{
                                displayCode(scooterModel.id,scooterModel.code)
                            }
                        }
                    }
                }
            }
        }
        return listItemView
    }

    private fun displayCode(id : Int, code:String){
        if(isBorrowing) {
            return
        }
        val builder = AlertDialog.Builder(context)
        val modal = LayoutInflater.from(context).inflate(R.layout.scooter_code_dialog,null)
        builder.setView(modal)

        val dialog = builder.create()
        val closeBtn = modal.findViewById<Button>(R.id.close_button)
        val scooterID = modal.findViewById<TextView>(R.id.scooter_id)
        val scooterCode = modal.findViewById<TextView>(R.id.scooter_code)

        scooterID.text = id.toString()
        scooterCode.text = code

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun displayModal(id : Int, location : String, locationId : String){
        val builder = AlertDialog.Builder(context)
        val modal = LayoutInflater.from(context).inflate(R.layout.delete_scooter_dialog,null)
        builder.setView(modal)

        val dialog = builder.create()
        val deleteBtn = modal.findViewById<Button>(R.id.add_location_btn)
        val scooterIdText = modal.findViewById<TextView>(R.id.scooter_id)
        scooterIdText.text = id.toString()
        val locationText = modal.findViewById<TextView>(R.id.scooter_location)
        locationText.text = location

        deleteBtn.setOnClickListener {
            ScooterViewModel.deleteScooter(locationId,id,context)

        }
        dialog.show()
    }
}
