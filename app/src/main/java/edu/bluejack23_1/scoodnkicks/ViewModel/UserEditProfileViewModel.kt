package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.text.Editable
import android.widget.Toast
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository

class UserEditProfileViewModel {
    companion object{

        fun changeUsername(newUsername: String, email:String, context: Context){
            if(newUsername.isEmpty()){
                Toast.makeText(context, "Username Field must be filled!", Toast.LENGTH_SHORT).show()
                return
            }
            val repo = UserRepository()
            repo.changeUsername(email,newUsername,context)
        }

        fun changePhoneNumber(newPhone : String, context:Context, id:String){
            val repo = UserRepository()
            val update = hashMapOf(
                "phone" to newPhone
            )
            repo.changeData(update, id, context)
        }




    }


}