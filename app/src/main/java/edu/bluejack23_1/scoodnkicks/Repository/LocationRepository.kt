package edu.bluejack23_1.scoodnkicks.Repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack23_1.scoodnkicks.Firebase.FirebaseDataSource
import edu.bluejack23_1.scoodnkicks.Model.LocationModel
import edu.bluejack23_1.scoodnkicks.Model.UserModel

class LocationRepository {
    private val ref = "locations"
    private var dataSource : FirebaseDataSource = FirebaseDataSource()
    fun getFirebaseAuth(): FirebaseAuth {
        return dataSource.getFirebaseAuth()
    }

    fun setLocationCount(id : String){
        val db = FirebaseDataSource.firestoreInstance()
        val docRef =  db.collection(ref).document(id)
        var query = docRef.collection("scooters")
        val count = query.count()
        count.get(AggregateSource.SERVER).addOnCompleteListener{
            task ->
            if(task.isSuccessful){
                Log.d("Count", task.result.count.toInt().toString())
                docRef.update("maxScooter",task.result.count.toInt())
            }
        }

        val query2 = docRef.collection("scooters").whereEqualTo("isUsed", false)
        val count2 = query2.count()
        count2.get(AggregateSource.SERVER).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                Log.d("Count", task.result.count.toInt().toString())
                docRef.update("availableScooter",task.result.count.toInt())
            }
        }
    }

    fun getLocations(callback: (List<DocumentSnapshot>?) -> Unit){
        dataSource.getData(ref){
                documents ->
            callback(documents)
        }
    }

    fun getRealtimeLocation(callback: (List<DocumentSnapshot>?) -> Unit){
        dataSource.getRealTimeData(ref){
                documents ->
            callback(documents)
        }
    }

    fun addLocation(ctx : Context, location : LocationModel){
        val data = hashMapOf(
            "Address" to location.address,
            "name" to location.name,
            "availableScooter" to location.availableScooter,
            "maxScooter" to location.maxScooter

        )
        dataSource.addData(ref,data as HashMap<String, Any>,null,ctx)
//        dataSource.addAuth(user.email,password)
    }
}