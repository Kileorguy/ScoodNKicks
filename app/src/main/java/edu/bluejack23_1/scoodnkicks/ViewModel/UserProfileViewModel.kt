package edu.bluejack23_1.scoodnkicks.ViewModel

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository
import javax.security.auth.callback.Callback

class UserProfileViewModel {

    companion object{

        fun getUserById(email : String,callback: (Task<DocumentSnapshot>?) -> Unit) {
            var repo = UserRepository()

            repo.getUserById(email){user->
                callback(user)
            }

        }

    }

}