package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel
import edu.bluejack23_1.scoodnkicks.Repository.HistoryRepository
import edu.bluejack23_1.scoodnkicks.Repository.ScooterRepository
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StaffHomeViewModel {

    companion object{
        private fun submitCode(email : String,code : String, context : Context,scooterCode : String, userIsBorrowing : Boolean, dateline : Timestamp){
            val repo = ScooterRepository()
            val userRepo = UserRepository()
            val historyRepo = HistoryRepository()
            var first = true
            Log.d("BORROWING",userIsBorrowing.toString())
            repo.getScooterModel(code,context){scoot->
                Log.d("Masuk1","hehe")
                repo.borrowOrReturnScooter(code,context){
                        cd ->
                    Log.d("Masuk2","hehe")

                    //    val scooterID : String,
                    //    val place : String,
                    //    val time : String,
                    //    val type : String, // return apa borrowing
                    //    val userEmail : String
                    if(userIsBorrowing){
                            Log.d("Masuk3","hehe")

                            if (scoot != null) {
                                val timestamp2: Timestamp = Timestamp.now()
                                var diff = (timestamp2.toDate().time - dateline.toDate().time) / 1000 / 60
                                if(diff < 0){
                                    diff = 0
                                }
                                repo.getScooterInLocation(cd){
                                if(first){
                                    historyRepo.setHistory(HistoryModel(
                                        scooterID = scoot.id.toString(),
                                        place = it,
                                        time = FieldValue.serverTimestamp(),
                                        type = "Return",
                                        userEmail = email,
                                        late = diff.toInt(),
                                    ))
                                    userRepo.setScooterCode(email,"",false)
                                    first =false
                                }
                                return@getScooterInLocation
                            }
                            return@borrowOrReturnScooter
                        }
//                        return@borrowOrReturnScooter


                    }else{
                        userRepo.setScooterCode(email,cd.toString(),true)
                            if (scoot != null) {
                                repo.getScooterInLocation(cd) {
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                    val current = LocalDateTime.now().format(formatter)
                                    if(first){
                                        historyRepo.setHistory(HistoryModel(
                                            scooterID = scoot.id.toString(),
                                            place = it,
                                            time = FieldValue.serverTimestamp(),
                                            type = "Borrow",
                                            userEmail = email,
                                            late = 0,
                                        ))
                                        first = false
                                        return@getScooterInLocation

                                    }
                                    }
                                return@borrowOrReturnScooter

                        }
                        return@borrowOrReturnScooter

                    }
                    return@borrowOrReturnScooter
                }
                return@getScooterModel
            }
            return
        }

        fun validateInput(code : String, userEmail : String, context: Context){
            if(code == "" || userEmail == ""){
                Toast.makeText(context,"Code must be filled!", Toast.LENGTH_SHORT).show()
                return
            }
            val repo = UserRepository()
            var valid = false

            var userScooterCode : String = ""
            var userIsBorrowing : Boolean = false
            var dateline : Timestamp = Timestamp.now()
            repo.getUsers {users->
                valid = false
                if (users != null) {
                    for(user in users){
                        if(user.getString("email").toString() == userEmail){
                            valid = true
                            userScooterCode = user.getString("scooterCode").toString()
                            userIsBorrowing = user.getBoolean("isBorrowing") as Boolean
                            dateline = user.get("deadline") as Timestamp
                        }
                    }
                }
                if(!valid){
                    Toast.makeText(context,"User Email is not valid!",Toast.LENGTH_SHORT).show()
                }else{
                    submitCode(userEmail,code,context,userScooterCode,userIsBorrowing,dateline)
                }
            }
        }
    }
}