package edu.bluejack23_1.scoodnkicks.helper

import edu.bluejack23_1.scoodnkicks.Repository.ScooterRepository

class ScooterHelper {
    companion object{
        fun validateCodeUnique(code : String,callback: (Boolean?) -> Unit){
            val repo = ScooterRepository()
            repo.getAllScooterInLocation {
                    scooterCodes->
                for(c in scooterCodes){
                    if(c == code){
                        callback(false)
                    }
                }
            }
            callback(true)
        }
    }
}