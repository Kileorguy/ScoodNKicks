package edu.bluejack23_1.scoodnkicks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.R

class PopularLocationAdapter(context: Context, locationModelList: ArrayList<LocationModel>):
    ArrayAdapter<LocationModel?>(context, 0, locationModelList as List<LocationModel?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.location_card, parent, false)
            val locationModel: LocationModel? = getItem(position)
            val locationName = listItemView!!.findViewById<TextView>(R.id.location_name_lbl)
            if (locationModel != null) {
                locationName.text = locationModel.name
            }
        }
        return listItemView
    }
}