package edu.bluejack23_1.scoodnkicks.Repository

import android.security.KeyChainAliasCallback
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import edu.bluejack23_1.scoodnkicks.Firebase.FirebaseDataSource
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel

class HistoryRepository {

    private var ref = "history"
    fun setHistory(history : HistoryModel ){
        // type disini itu dia borrow / return
        val db = FirebaseDataSource.firestoreInstance()
        // val scooterID : String,
        //    val place : String,
        //    val time : String,
        //    val type : String, // return apa borrowing
        //    val userEmail : String
        val data = hashMapOf(
            "scooterID" to history.scooterID,
            "place" to history.place,
            "time" to history.time,
            "type" to history.type,
            "userEmail" to history.userEmail,
            "late" to history.late
        )

        db.collection(ref).add(data).addOnSuccessListener {
            Log.d("QUERY_BERHASIL","HEHE")
        }.addOnFailureListener {
            Log.d("QUERY_GAGAL","HEHE")

        }
    }
    fun getAllHistory(callback: (List<DocumentSnapshot>?) -> Unit){
        val dataSource = FirebaseDataSource()
        dataSource.getData(ref){history->
            callback(history)
        }
    }

    fun getUserHistory(email : String,callback: (List<DocumentSnapshot>?) -> Unit){
        val db = FirebaseDataSource.firestoreInstance()
        db.collection(ref)
            .whereEqualTo("userEmail", email)
            .get()

            .addOnSuccessListener { querySnapshot ->
                callback(querySnapshot.documents)
            }



    }







}