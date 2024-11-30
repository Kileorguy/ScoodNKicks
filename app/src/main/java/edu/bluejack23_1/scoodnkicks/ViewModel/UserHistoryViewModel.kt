package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import edu.bluejack23_1.scoodnkicks.Model.HistoryModel
import edu.bluejack23_1.scoodnkicks.Repository.HistoryRepository
import edu.bluejack23_1.scoodnkicks.Repository.ScooterRepository
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository

class UserHistoryViewModel {
    companion object {


        fun getRVData(
            email: String,
            isBorrowing: Boolean,
            gajadi: String,
            context: Context,
            callback: (ArrayList<HistoryModel>?) -> Unit
        ) {
            var first = true
            val repo = HistoryRepository()
            var list: ArrayList<HistoryModel> = ArrayList()
            list.clear()
            repo.getUserHistory(email) { data ->
                list.clear()

                val timeNow = Timestamp.now()
                if (isBorrowing) {
                    val repo = ScooterRepository()
                    val userRepo = UserRepository()
                    val historyRepo = HistoryRepository()
                    Log.d("Masuk_User0", "hehe")
                    var dateline: Timestamp = Timestamp.now()
                    var scooterCode = ""
                    userRepo.getUserById(email) { user ->
                        dateline = user?.result?.get("deadline") as Timestamp
                        scooterCode = user?.result?.get("scooterCode").toString()

                        repo.getScooterModel(scooterCode, context) { scoot ->
                            Log.d("Masuk_User1", "hehe")

//                        repo.borrowOrReturnScooter(scooterCode,context){
//                                cd ->
                            if (isBorrowing) {
                                repo.getScooterInLocation(scooterCode) {
                                    Log.d("Masuk_User2", "hehe")
                                    if (scoot != null) {
                                        val timestamp2: Timestamp = Timestamp.now()

                                        var diff =
                                            (dateline.toDate().time - timestamp2.toDate().time) / 1000 / 60
//                                        if(diff > 0){
//                                            diff = 0
//                                        }
                                        if(first){
                                            list.add(
                                                HistoryModel(
                                                    scoot.id.toString(),
                                                    it,
                                                    Timestamp.now(),
                                                    "Current",
                                                    email,
                                                    diff.toInt()
                                                )
                                        )
                                            first = false
                                        }
                                        if (data != null) {
                                            for (d in data) {
                                                val scooterID = d?.data?.get("scooterID").toString()
                                                val place = d?.data?.get("place").toString()
                                                val time = d?.data?.get("time")
                                                val type = d?.data?.get("type").toString()
                                                val userEmail = d?.data?.get("userEmail").toString()
                                                val late: Long = d?.data?.get("late") as Long
                                                Log.d("Scooter", scooterID)
                                                Log.d("place", place)
//                        Log.d("time",time)
                                                Log.d("type", type)
                                                Log.d("email", userEmail)

//                        var idx = list.count()
//                        if(idx<=0){
                                                if (userEmail == email) {
                                                    list.add(
                                                        HistoryModel(
                                                            scooterID,
                                                            place,
                                                            time,
                                                            type,
                                                            userEmail,
                                                            late.toInt()
                                                        )
                                                    )
                                                }
//                        }else{
//                            list.add(idx-1,HistoryModel(scooterID,place,time,type,userEmail))

//                        }

                                            }
                                            callback(list)
                                        }
                                        return@getScooterInLocation
                                    }
                                }
                            }else{

//                                repo.getUserHistory(email) { data ->
                                    if (data != null) {
                                        for (d in data) {
                                            val scooterID = d?.data?.get("scooterID").toString()
                                            val place = d?.data?.get("place").toString()
                                            val time = d?.data?.get("time")
                                            val type = d?.data?.get("type").toString()
                                            val userEmail = d?.data?.get("userEmail").toString()
                                            val late: Long = d?.data?.get("late") as Long
                                            Log.d("Scooter", scooterID)
                                            Log.d("place", place)
//                        Log.d("time",time)
                                            Log.d("type", type)
                                            Log.d("email", userEmail)

//                        var idx = list.count()
//                        if(idx<=0){
                                            if (userEmail == email) {
                                                list.add(
                                                    HistoryModel(
                                                        scooterID,
                                                        place,
                                                        time,
                                                        type,
                                                        userEmail,
                                                        late.toInt()
                                                    )
                                                )
                                            }
//                        }else{
//                            list.add(idx-1,HistoryModel(scooterID,place,time,type,userEmail))

//                        }
                                            callback(list)


                                        }
                                    }

                                }


//                            }
//                            return@borrowOrReturnScooter
//                        }
                            return@getScooterModel
                        }
                    }


                } else {
                    repo.getUserHistory(email) { data ->
                        if (data != null) {
                            for (d in data) {
                                val scooterID = d?.data?.get("scooterID").toString()
                                val place = d?.data?.get("place").toString()
                                val time = d?.data?.get("time")
                                val type = d?.data?.get("type").toString()
                                val userEmail = d?.data?.get("userEmail").toString()
                                val late: Long = d?.data?.get("late") as Long
                                Log.d("Scooter", scooterID)
                                Log.d("place", place)
//                        Log.d("time",time)
                                Log.d("type", type)
                                Log.d("email", userEmail)

//                        var idx = list.count()
//                        if(idx<=0){
                                if (userEmail == email) {
                                    list.add(
                                        HistoryModel(
                                            scooterID,
                                            place,
                                            time,
                                            type,
                                            userEmail,
                                            late.toInt()
                                        )
                                    )
                                }
//                        }else{
//                            list.add(idx-1,HistoryModel(scooterID,place,time,type,userEmail))

//                        }
                                callback(list)


                            }
                        }

                    }


                }

            }

        }
    }
}