package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import edu.bluejack23_1.scoodnkicks.Model.ScooterModel
import edu.bluejack23_1.scoodnkicks.Repository.ScooterRepository
import edu.bluejack23_1.scoodnkicks.helper.ScooterHelper
import kotlin.random.Random

class ScooterViewModel {
    companion object{
        fun addScooter(locationId : String,scooter : ScooterModel,  ctx : Context){
            val repo = ScooterRepository()

            repo.addScooter(locationId,ctx, scooter)
        }

        fun generateCode(id : String, callback: (String?) -> Unit){

            val random = Random
            val characters = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            var code = id + characters.random() + characters.random() + characters.random() + characters.random()
            var loop = false
            do{
                code = id.toString() + characters.random() + characters.random() + characters.random() + characters.random()+ characters.random()
                ScooterHelper.validateCodeUnique(code){
                    if (it != null) {
                        loop = it
                    }else{
                        loop = false
                    }
                }

            }while(!loop)
            callback(code)
        }

        fun getScooter(locationID : String,ctx : Context,callback: (ArrayList<ScooterModel>?) -> Unit){
            val repo = ScooterRepository()
            var dataList = ArrayList<ScooterModel>()
            repo.getScooter(locationID,ctx){
                scooters->
                dataList.clear()
                if (scooters != null) {
                    for (s in scooters){
                        val idd = s.data?.get("id") as Long
                        val codes : String = s.data?.get("code").toString()
                        val isUsed = s.data?.get("isUsed") as Boolean
                        dataList.add(ScooterModel(idd.toInt(),codes,isUsed))
                    }
                    callback(dataList)
                }
            }
        }
        fun deleteScooter(locationID: String, scooterID: Int, ctx: Context){
            val repo = ScooterRepository()
            repo.deleteScooter(locationID,scooterID,ctx)
        }

    }


}