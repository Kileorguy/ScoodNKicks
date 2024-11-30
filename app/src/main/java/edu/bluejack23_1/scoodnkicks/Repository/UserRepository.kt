package edu.bluejack23_1.scoodnkicks.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_1.scoodnkicks.Firebase.FirebaseDataSource
import edu.bluejack23_1.scoodnkicks.Model.UserModel
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class UserRepository {
    private val ref = "users"
    private var dataSource : FirebaseDataSource= FirebaseDataSource()
    fun addUser(ctx : Context, user : UserModel, password : String){
        val data = hashMapOf(
            "first_name" to user.firstName,
            "last_name" to user.lastName,
            "username" to user.username,
            "email" to user.email,
            "phone" to user.phone,
            "isActivated" to user.isActivated,
            "deadline" to Timestamp.now(),
            "isBorrowing" to false,
            "scooterCode" to ""

        )

        dataSource.addData(ref,data as HashMap<String, Any>,user.email,ctx)
        dataSource.addAuth(user.email,password)
    }

    fun getUsers(callback: (List<DocumentSnapshot>?) -> Unit){
        dataSource.getData(ref){
            documents ->
            callback(documents)
        }
    }

    fun getFirebaseAuth(): FirebaseAuth {
        return dataSource.getFirebaseAuth()
    }

    fun getUserById(id : String,callback: (Task<DocumentSnapshot>?) -> Unit){
        dataSource.getDatabyId(ref,id){documents->
            callback(documents)
        }
    }

    fun changeUsername(id : String,username:String,context: Context){

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        val documentRef = collectionRef.document(id)
        val update = hashMapOf(
            "username" to username
        )
        documentRef.update(update as Map<String, Any>)
        Toast.makeText(context, "Username Changed!", Toast.LENGTH_SHORT).show()

    }

    fun setScooterCode(id : String,scooterCode : String, isBorrowing : Boolean){
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        val documentRef = collectionRef.document(id)

        val currentTimestamp: Timestamp = Timestamp.now()
        val currentDate: Date = currentTimestamp.toDate()

        val timePlus = Calendar.getInstance()
        timePlus.time = currentDate
        timePlus.add(Calendar.HOUR_OF_DAY, 1)

        val newDate: Date = timePlus.time

        val update = hashMapOf(
            "scooterCode" to scooterCode,
            "isBorrowing" to isBorrowing,
            "deadline" to Timestamp(newDate)

        )
        documentRef.update(update as Map<String, Any>).addOnSuccessListener {
            Log.d("Set_Code_Success","hehe")
        }.addOnFailureListener {
            Log.d("Set_Code_FAil","hehe")
        }
    }

    fun changeData(update : Map<String, Any>, id:String, context: Context){
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("users")
        val documentRef = collectionRef.document(id)
        documentRef.update(update)
        Toast.makeText(context, "Data Changed!", Toast.LENGTH_SHORT).show()
    }




}