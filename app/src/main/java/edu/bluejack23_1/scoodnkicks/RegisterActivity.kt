package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack23_1.scoodnkicks.databinding.ActivityRegisterBinding
import edu.bluejack23_1.scoodnkicks.ViewModel.RegisterViewModel
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEt: EditText
    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var registerBtn: Button
    private lateinit var signInHref: TextView
    private  lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerBtn = binding.signUpBtn
        firstNameEt = binding.registerFirstNameText
        lastNameEt = binding.registerLastNameText
        usernameEt = binding.registerUsernameText
        emailEt = binding.registerEmailText
        phoneEt = binding.registerPhoneText
        passwordEt = binding.registerPasswordText
        signInHref = binding.signInRedirect

        signInHref.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        signInHref.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val db = Firebase.firestore

        registerBtn.setOnClickListener{
            val firstName = firstNameEt.text.toString()
            val lastName = lastNameEt.text.toString()
            val username = usernameEt.text.toString()
            val password = passwordEt.text.toString()
            val email = emailEt.text.toString()
            val phone = phoneEt.text.toString()

            RegisterViewModel.validateRegister(this,firstName,lastName,username,password, email, phone)
        }
    }
}