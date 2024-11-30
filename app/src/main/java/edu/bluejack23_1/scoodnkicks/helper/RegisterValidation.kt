package edu.bluejack23_1.scoodnkicks.helper

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository

class RegisterValidation {
    companion object{
        fun isPasswordValid(password: String): Boolean {
            val numberRegex = ".*\\d.*"
            val specialCharRegex = ".*[!@#\$%^&*()_+\\-=[\\\\]{};':\"|,.<>/?].*"

            return password.matches(Regex(numberRegex)) && password.matches(Regex(specialCharRegex))
        }

        private val db = Firebase.firestore

        fun isEmailUnique(email: String, callback: (Boolean) -> Unit) {
            var isUnique = true
            var repo = UserRepository()
            repo.getUsers { documents ->
                Log.d("Documents","$documents")
                if(documents != null){
                    for(d in documents){
                        Log.d("dataa","docs : ${d.data?.get("email")}")
                        if(d.data?.get("email").toString() == email){

                            isUnique = false
                            break
                        }
                    }
                    callback(isUnique)
                }
            }
        }

        fun isPhoneUnique(phone: String, callback: (Boolean) -> Unit) {
            var isUnique = true
            var repo = UserRepository()
            repo.getUsers { documents ->
                Log.d("Documents", "$documents")
                if (documents != null) {
                    for (d in documents) {
                        Log.d("dataa", "docs : ${d.data?.get("phone")}")
                        if (d.data?.get("phone").toString() == phone) {
                            isUnique = false
                            break
                        }
                    }
                    callback(isUnique)
                }

            }
        }

        fun emptyValidation(firstName : String, lastName : String, username : String, password : String, email : String, phone : String) : Boolean{
            return firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()
        }
    }
}