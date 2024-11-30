package edu.bluejack23_1.scoodnkicks.ViewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import edu.bluejack23_1.scoodnkicks.AdminHomeActivity
import edu.bluejack23_1.scoodnkicks.HomeActivity
import edu.bluejack23_1.scoodnkicks.Repository.UserRepository
import edu.bluejack23_1.scoodnkicks.StaffHomeActivity

class LoginViewModel {
    companion object{
        fun doLogin(email : String, password : String, ctx : Context){
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(ctx, "All form must be filled", Toast.LENGTH_SHORT).show()
            }
            else{
                var repo = UserRepository()
                var auth = repo.getFirebaseAuth()
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {authRes ->
                    var repo = UserRepository()
                    val email = authRes.user?.email
                    if (email != null) {
                        repo.getUserById(email){ user ->
                            val active = user?.result?.getBoolean("isActivated")
                            val role = user?.result?.getString("role").toString()
                            if(active == true){
                                Toast.makeText(ctx, "Login Success!", Toast.LENGTH_SHORT).show()
                                if(role == "Admin"){
                                    val intent = Intent(ctx, AdminHomeActivity::class.java)

                                    intent.putExtra("role","admin")
                                    intent.putExtra("email",email)

                                    ctx.startActivity(intent)

                                }else if(role == "Staff"){
                                    val intent = Intent(ctx, StaffHomeActivity::class.java)
                                    intent.putExtra("role","staff")
                                    intent.putExtra("email",email)

                                    ctx.startActivity(intent)
                                }else{
                                    val intent = Intent(ctx, HomeActivity::class.java)
                                    intent.putExtra("role","user")
                                    intent.putExtra("email",email)

                                    ctx.startActivity(intent)
                                }
                            }else{
                                Toast.makeText(ctx, "Account is not activated!", Toast.LENGTH_SHORT).show()
                            }
                        }
//                        Toast.makeText(ctx, "Account is not activated!", Toast.LENGTH_SHORT).show()
                    }

                }
                .addOnFailureListener{
                    Toast.makeText(ctx, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}