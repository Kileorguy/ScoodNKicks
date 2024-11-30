package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack23_1.scoodnkicks.HomeActivity
import edu.bluejack23_1.scoodnkicks.Model.UserModel
import edu.bluejack23_1.scoodnkicks.OtpActivity
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository
import edu.bluejack23_1.scoodnkicks.helper.RegisterValidation
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RegisterViewModel {

    companion object{
        private fun sendOTP(repo : UserRepository, phoneNumber : String){
//            val auth = repo.getFirebaseAuth()
//            val options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phoneNumber) // Phone number to verify
//                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                .setActivity(this) // Activity (for callback binding)
//                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//                .build()
//            PhoneAuthProvider.verifyPhoneNumber(options)


        }
        fun validateRegister(ctx : Context,firstName : String, lastName : String, username : String, password : String, email : String, phone : String) {

            if(RegisterValidation.emptyValidation(firstName, lastName, username, password, email, phone)){
                Toast.makeText(ctx, "All form must be filled", Toast.LENGTH_SHORT).show()
            }
            else if(!RegisterValidation.isPasswordValid(password)){
                Toast.makeText(ctx, "Password must contain number and special character", Toast.LENGTH_SHORT).show()
            }
            else if(!email.endsWith("@gmail.com")){
                Toast.makeText(ctx, "Invalid email", Toast.LENGTH_SHORT).show()
            }
            else {
                var valid = true
                RegisterValidation.isEmailUnique(email) { isUnique ->
                    if (!isUnique) {
                        Toast.makeText(ctx, "Email already used", Toast.LENGTH_SHORT).show()
                        valid = false
                    }else{
                        RegisterValidation.isPhoneUnique(phone){ isUnique ->
                            if(!isUnique){5
                                valid = false
                                Toast.makeText(ctx, "Phone number is already used", Toast.LENGTH_SHORT).show()
                            }else{
                                if(valid){
                                    var repo = UserRepository()
                                    var user : UserModel = UserModel(email,firstName, false,lastName, phone, username)
                                    repo.addUser(ctx,user, password)
//                                    sendOTP(repo,phone)
                                    val intent = Intent(ctx, OtpActivity::class.java)
                                    intent.putExtra("phoneNumber",phone)
                                    intent.putExtra("email",email)
                                    ctx.startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}