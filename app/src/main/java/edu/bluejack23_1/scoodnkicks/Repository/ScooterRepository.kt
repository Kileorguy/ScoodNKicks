package edu.bluejack23_1.scoodnkicks.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import edu.bluejack23_1.scoodnkicks.Firebase.FirebaseDataSource
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel
import edu.bluejack23_1.scoodnkicks.Model.ScooterModel
import edu.bluejack23_1.scoodnkicks.ViewModel.ScooterViewModel

class ScooterRepository{
    private val ref = "locations"
    private val subRef = "scooters"
    private var dataSource : FirebaseDataSource = FirebaseDataSource()

    fun getScooter(id : String,ctx : Context,callback: (List<DocumentSnapshot>?) -> Unit){

        dataSource.getDataWithSubCollectionRealtime(ref,subRef,id){
            data->
            callback(data)
        }
    }

    fun getAllScooterInLocation(callback: (List<String>) -> Unit){
        val db = FirebaseDataSource.firestoreInstance()
        val allScooters = mutableListOf<String>()

        db.collection(ref).get()
            .addOnSuccessListener { locations ->
                for (location in locations) {
                    val locationId = location.id
                    db.collection(ref).document(locationId)
                        .collection(subRef).get()
                        .addOnSuccessListener { scooters ->
                            for (scooter in scooters) {
                                val scooterCode = scooter.getString("code").toString()
                                allScooters.add(scooterCode)
                            }
                            callback(allScooters)
                        }
                        .addOnFailureListener {
                            callback(emptyList())
                        }
                }
            }
            .addOnFailureListener {
                callback(emptyList())
            }

    }
    fun deleteScooter(locationID: String, scooterID: Int, ctx: Context){
        val db = FirebaseDataSource.firestoreInstance()
        db.collection(ref).document(locationID).collection(subRef).document(scooterID.toString()).delete()
            .addOnSuccessListener {
                Toast.makeText(ctx, "Item successfully deleted!", Toast.LENGTH_SHORT).show()
            }

    }


    fun getScooterInLocation(code : String, callback: (String) -> Unit){
        var first = true
        val db = FirebaseDataSource.firestoreInstance()
        db.collection(ref).get()
            .addOnSuccessListener {
                    locations->
                for (loc in locations){
                    val locationID = loc.id
                    db.collection(ref).document(locationID).collection(subRef).get()
                        .addOnSuccessListener {scooters->
                            for(s in scooters){
                                Log.d("DEBUG PLIS","test "+s.getString("code")+" CODE $code")
                                if(s.getString("code").toString() == code){
                                    callback(loc.getString("name").toString())
                                    break
                                }
                                }

                            }

                        }
                }
            }

    fun addScooter(id : String,ctx : Context, scooter : ScooterModel){

        dataSource.getDataWithSubCollection(ref,subRef,id){fetched ->
            var get = false;
            if (fetched != null) {
                Log.d("Scooter Fetch",fetched.toString())
                var counter = 1
                for (f in fetched){
                    if(counter>=15){
                        Toast.makeText(ctx, "Max Scooter Has Reached!", Toast.LENGTH_SHORT).show()
                        return@getDataWithSubCollection
                    }
//                    var f = fe.data
                    val idScooter = f.data?.get("id") as Long

                    if(idScooter.toInt() != counter){

                        ScooterViewModel.generateCode(counter.toString()){
                            if (it != null) {
                                scooter.code = it
                            }
                        }

                        val data = hashMapOf(
                            "id" to counter,
                            "code" to scooter.code,
                            "isUsed" to false
                        )
                        dataSource.addDataWithSubCollection(ref,subRef,id,data as HashMap<String, Any>,ctx)
                        get = true
                        break
                    }else{
                        counter++
                    }


                }
                if(!get){
                    ScooterViewModel.generateCode(counter.toString()){
                        if (it != null) {
                            scooter.code = it
                        }
                    }
                    val data = hashMapOf(
                        "id" to counter,
                        "code" to scooter.code,
                        "isUsed" to false
                    )
                    dataSource.addDataWithSubCollection(ref,subRef,id,data as HashMap<String, Any>,ctx)
                }
            }
        }


    }

    fun borrowOrReturnScooter(
        code: String,
        context: Context,
        callback: (String) -> Unit
    ){
        val db = FirebaseDataSource.firestoreInstance()
        db.collection(ref).get()
            .addOnSuccessListener {
                    locations->
                for (loc in locations){
                    val locationID = loc.id
                    db.collection(ref).document(locationID).collection(subRef).get()
                        .addOnSuccessListener {scooters->
                            for(s in scooters){
                                val isUsed = s.getBoolean("isUsed") ?: false
                                if(s.getString("code") == code){
                                    s.reference.update("isUsed", !isUsed)
                                    ScooterViewModel.generateCode(s.id.toString()) { newCode ->
                                        s.reference.update("code", newCode)
                                        Toast.makeText(context,"Code Valid!",Toast.LENGTH_SHORT).show()

                                        callback(newCode.toString())
                                    }
                                }
                            }

                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,"Code is not valid!",Toast.LENGTH_SHORT).show()
            }
    }

    fun getScooterModel(
        code: String,
        context: Context,
        callback: (QueryDocumentSnapshot?) -> Unit
    ){
            Log.d("ScootMOdel1","asd")
        val db = FirebaseDataSource.firestoreInstance()
        db.collection(ref).get()
            .addOnSuccessListener {
                    locations->
                Log.d("ScootMOdel2","asd")

                for (loc in locations){
                    val locationID = loc.id
                    db.collection(ref).document(locationID).collection(subRef).get()
                        .addOnSuccessListener {scooters->
                            Log.d("ScootMOdel3","asd")

                            for(s in scooters){
                                val isUsed = s.getBoolean("isUsed") ?: false
                                Log.d("debug","Code : "+ code +" Scooter Code"+s.getString("code"))

                                if(s.getString("code") == code){
//                                    s.reference.update("isUsed", !isUsed)

                                        callback(s)
                                }
                            }

                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,"Code is not valid!",Toast.LENGTH_SHORT).show()
            }
    }
}