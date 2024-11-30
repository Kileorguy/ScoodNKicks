package edu.bluejack23_1.scoodnkicks.Firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseDataSource {
    companion object{
        var Firestore : FirebaseFirestore? = null
        var Auth : FirebaseAuth? = null

        fun firestoreInstance() : FirebaseFirestore {
            if(Firestore == null){
                Firestore = FirebaseFirestore.getInstance()
            }
            return Firestore as FirebaseFirestore
        }
        fun authInstance() : FirebaseAuth{
            if(Auth == null){
                Auth = Firebase.auth
            }
            return Auth as FirebaseAuth
        }
    }
    fun addAuth(email : String, password : String){
        val auth = authInstance()
        auth.createUserWithEmailAndPassword(email,password)
    }
    fun getAuth(): FirebaseUser? {
        val auth = authInstance()
        var currUser = auth?.currentUser
        if(currUser !=null){
            return currUser
        }
        return null
    }
    fun getFirebaseAuth() : FirebaseAuth{
        return authInstance()
    }

    fun addData(ref: String, data: HashMap<String, Any>, id : String?,ctx: Context){
        val db = firestoreInstance()
        if(id !=null){
            db.collection(ref).document(id).set(data)
                .addOnSuccessListener {
                    Toast.makeText(ctx, "Data added successfully with the email : $id", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(ctx, "Failed to add data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }else{
            db.collection(ref).add(data).addOnSuccessListener {
                Toast.makeText(ctx, "Data added successfully with ID: ${it.id}", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(ctx, "Failed to add data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addDataWithSubCollection(ref : String, subRef : String,id : String,data: HashMap<String, Any> ,ctx : Context ){
        val db = firestoreInstance()
        val docRef = db.collection(ref).document(id).collection(subRef)
        Log.d("Add SubCollection","Add $ref $subRef $id ${data.toString()}")
        docRef.document(data["id"].toString()).set(data).addOnSuccessListener {
            Toast.makeText(ctx, "Data added successfully with ID: ${data["id"]}", Toast.LENGTH_SHORT).show()
            Log.d("Added Data Sub","Data added successfully")
        }.addOnFailureListener {
            Toast.makeText(ctx, "Failed to add data", Toast.LENGTH_SHORT).show()
            Log.d("Failed Data Sub","Failed to add data!")

        }
    }

    fun getDataWithSubCollection(ref : String, subRef : String,id : String,callback: (List<DocumentSnapshot>?) -> Unit){
        val db = firestoreInstance()

        db.collection(ref).document(id).collection(subRef).orderBy("id")
            .get()
            .addOnSuccessListener { documents ->
                callback(documents.documents)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getDataWithSubCollectionRealtime(ref : String, subRef : String,id : String,callback: (List<DocumentSnapshot>?) -> Unit){
        val db = firestoreInstance()

        db.collection(ref).document(id).collection(subRef).orderBy("id")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    callback(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val documents = snapshot.documents
                    callback(documents)
                } else {
                    callback(emptyList())
                }
            }
    }

    fun getData(ref: String, callback: (List<DocumentSnapshot>?) -> Unit) {
        val db = firestoreInstance()

        db.collection(ref)
            .get()
            .addOnSuccessListener { documents ->
                callback(documents.documents)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getRealTimeData(ref: String, callback: (List<DocumentSnapshot>?) -> Unit){
        val db = firestoreInstance()
        db.collection(ref)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    callback(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    Log.d("Realtime", snapshot.documents.toString())
                    val documents = snapshot.documents
                    callback(documents)
                } else {
                    callback(null)
                }
            }

    }

    fun getDatabyId(ref : String, id : String,callback: (Task<DocumentSnapshot>?) -> Unit){
        val db = firestoreInstance()
        val docRef: DocumentReference = db.collection(ref).document(id)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    Log.d("TAG", "DocumentSnapshot data: " + document.data)
                    callback(task)
                } else {
                    Log.d("TAG", "No such document")
                    callback(null)
                }
            } else {
                Log.d("TAG", "get failed with ", task.exception)
                callback(null)
            }
        }
    }

    fun deleteDoc(){

    }


}