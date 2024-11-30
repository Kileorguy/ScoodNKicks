package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.Repository.LocationRepository

class LocationViewModel {
    companion object {
        fun setRVData( callback: (ArrayList<LocationModel>) -> Unit)  {
            val repo = LocationRepository()
            var dataList: ArrayList<LocationModel> = ArrayList<LocationModel>()
            repo.getRealtimeLocation { docs ->

                run {
                    dataList.clear()
                    if (docs != null) {
                        for (d in docs) {
                            repo.setLocationCount(d.id)
                            Log.d("locations","docs : ${d.data?.get("name")}")
                            val dataName =  d.data?.get("name").toString()
                            val dataAddress = d.data?.get("Address").toString()
                            var availScooter : Int = 0
                            var maxScooter : Int = 0
                            if(d.data?.get("availableScooter") != null){
                                var query1 = d.data?.get("availableScooter") as Long
                                availScooter =query1.toInt()
                            }
                            if(d.data?.get("maxScooter") != null){
                                var query1 = d.data?.get("maxScooter") as Long
                                maxScooter = query1.toInt()
                            }
                            val id : String = d.id
                            dataList.add(
                                LocationModel(
                                    id,dataName, dataAddress, availScooter as Int ?: 0, maxScooter as Int ?: 0
                                )
                            )


                        }
                        callback(dataList)

                    }
                }
            }

        }
        fun addData(name : String, address : String, ctx : Context): Boolean{

            if(name.length <= 5){
                Toast.makeText(ctx, "Location name must be more than 5 characters", Toast.LENGTH_SHORT).show()
                return false
            }
            else if(!address.contains(" ")){
                Toast.makeText(ctx, "Address must be at least 2 words!", Toast.LENGTH_SHORT).show()
                return false
            }

            val repo = LocationRepository()
            repo.addLocation(ctx, LocationModel("-",name,address,0,0))
            return true
        }
    }



}