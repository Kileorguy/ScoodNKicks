package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.Repository.LocationRepository
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository

class HomeViewModel {

    companion object{
        fun getUser(email : String, callback: (DocumentSnapshot?) -> Unit){
            val userRepo = UserRepository()
            userRepo.getUserById(email) { user ->
                callback(user?.result)
            }
        }
    }

    private val db = Firebase.firestore
    fun getMostPopularLocation(ctx: Context, callback: (ArrayList<LocationModel>) -> Unit){
        val locationList: ArrayList<LocationModel> = ArrayList()
        var repo = LocationRepository()
        repo.getLocations { docs ->
            run {
                if (docs != null) {
                    for (d in docs) {
                        Log.d("locations","docs : ${d.data?.get("name")}")
                        val dataName =  d.data?.get("name").toString()
                        val dataAddress = d.data?.get("address").toString()
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
                        locationList.add(
                            LocationModel(
                                id, dataName, dataAddress, availScooter, maxScooter
                            )
                        )
                        if (locationList.size == 8) break;
                    }
                    callback(locationList)
                }
            }
        }
    }
}