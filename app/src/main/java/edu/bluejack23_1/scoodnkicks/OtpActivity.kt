package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

import edu.bluejack23_1.scoodnkicks.Firebase.FirebaseDataSource

class OtpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendingToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var sendOtpText: TextView
    private lateinit var OTPConfirmBtn : Button
    private lateinit var otpField : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        val num = intent.getStringExtra("phoneNumber")
        auth = FirebaseAuth.getInstance()
        sendOtpText = findViewById(R.id.sendAgain)


        OTPConfirmBtn = findViewById(R.id.button)



        sendOtpText.setOnClickListener {
            val phoneNumber = "+62$num"
            Log.d("CEKOTP","CEKKKK")
            sendOTP(phoneNumber,false)
        }
        OTPConfirmBtn.setOnClickListener {
//            Log.w("test", "test")
//            Toast.makeText(applicationContext,"test",Toast.LENGTH_SHORT).show()
            otpField = findViewById(R.id.OtpField)
            val otpUser = otpField.text.toString()
            if (storedVerificationId != null && otpUser.isNotEmpty()) {
                val cred = PhoneAuthProvider.getCredential(storedVerificationId.toString(), otpUser)
                validateOTP(cred)
            } else {
                Toast.makeText(applicationContext, "Invalid OTP or Verification ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validateOTP(cred : PhoneAuthCredential){
        auth.signInWithCredential(cred).addOnCompleteListener{task ->
            if (task.isSuccessful) {
                val email = intent.getStringExtra("email")
                val intent = Intent(this, LoginActivity::class.java)
                val db = FirebaseFirestore.getInstance()
                val collectionRef = db.collection("users")
                val documentRef = collectionRef.document(email.toString())
                val update = hashMapOf(
                    "isActivated" to true
                )
                documentRef.update(update as Map<String, Any>)
                Toast.makeText(applicationContext, "Account Activated!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {
                val errorMessage = task.exception?.message
                Log.e("FirebaseAuth", "Verification Failed: $errorMessage")
                Toast.makeText(applicationContext, "Verification Failed! $errorMessage", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun sendOTP(phoneNumber: String, isResend:Boolean) {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = verificationId
                resendingToken = token
                Toast.makeText(applicationContext,"OTP Sent",Toast.LENGTH_SHORT).show()
            }
        }

        val options : PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
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

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Verification successful!", Toast.LENGTH_SHORT).show()
                    val email = intent.getStringExtra("email")
                    val intent = Intent(this, LoginActivity::class.java)
                    val db = FirebaseFirestore.getInstance()
                    val collectionRef = db.collection("users")
                    val documentRef = collectionRef.document(email.toString())
                    val update = hashMapOf(
                        "isActivated" to true
                    )
                    documentRef.update(update as Map<String, Any>)
                    Toast.makeText(applicationContext, "Account Activated!", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Verification failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }









}