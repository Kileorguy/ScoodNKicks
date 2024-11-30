package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_1.scoodnkicks.ViewModel.ScooterViewModel
import edu.bluejack23_1.scoodnkicks.ViewModel.UserEditProfileViewModel
import edu.bluejack23_1.scoodnkicks.ViewModel.UserProfileViewModel
import edu.bluejack23_1.scoodnkicks.databinding.ActivityUserEditProfileBinding
import java.util.concurrent.TimeUnit

class UserEditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserEditProfileBinding
    private lateinit var usernameField : EditText
    private lateinit var emailField : EditText
    private lateinit var phoneField : EditText

    private lateinit var usernameChangeButton : Button
    private lateinit var emailChangeButton : Button
    private lateinit var phoneChangeButton : Button
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendingToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEditProfileBinding.inflate(layoutInflater)

        usernameField = binding.usernameField
        usernameChangeButton = binding.changeUsernameBtn
        emailField = binding.emailField
        phoneField = binding.phoneField
        phoneChangeButton = binding.changePhoneBtn
        auth = FirebaseAuth.getInstance()

        setContentView(binding.root)

        val email = intent.getStringExtra("email").toString()

        UserProfileViewModel.getUserById(email){ user->
            usernameField.hint = user?.result?.getString("first_name").toString()
            emailField.hint = email
            phoneField.hint = user?.result?.getString("phone").toString()
        }

        usernameChangeButton.setOnClickListener{
            UserEditProfileViewModel.changeUsername(usernameField.text.toString(),email,this)
        }

        phoneChangeButton.setOnClickListener {
            var phoneNumber = phoneField.text.toString()
            sendOTP(phoneNumber,false,email)
            displayModal(phoneNumber,email)
        }
    }

    private fun sendOTP(phoneNumber: String, isResend:Boolean,email : String) {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential,phoneNumber,email)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                resendingToken = token
                Toast.makeText(applicationContext,"OTP Sent", Toast.LENGTH_SHORT).show()
            }
        }

        val options : PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+62$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendingToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
//        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("TESTOTP","T")
    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,phone : String, email : String) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Verification successful!", Toast.LENGTH_SHORT).show()
                    UserEditProfileViewModel.changePhoneNumber(phone,this,email)
                } else {
                    Toast.makeText(applicationContext, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun displayModal(phone :  String, email : String){
        val builder = AlertDialog.Builder(this)
        val modal = LayoutInflater.from(this).inflate(R.layout.otp_confirmation_dialog,null)
        builder.setView(modal)

        val otpField = modal.findViewById<EditText>(R.id.otp_code)
        val confirmBtn = modal.findViewById<Button>(R.id.confirm_otp_button)

        confirmBtn.setOnClickListener {
            val otpInput = otpField.text.toString()
            if (storedVerificationId != null && otpInput.isNotEmpty()) {
                val cred = PhoneAuthProvider.getCredential(storedVerificationId.toString(), otpInput)
                signInWithPhoneAuthCredential(cred,phone,email)
            } else {
                Toast.makeText(applicationContext, "Invalid OTP or Verification ID", Toast.LENGTH_SHORT).show()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}