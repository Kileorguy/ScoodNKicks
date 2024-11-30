package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import edu.bluejack23_1.scoodnkicks.ViewModel.LoginViewModel
import edu.bluejack23_1.scoodnkicks.databinding.ActivityLoginBinding
import edu.bluejack23_1.scoodnkicks.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var signInBtn: Button
    private lateinit var signUpHref: TextView
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpHref = binding.singUpRedirect
        signUpHref.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        signUpHref.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        emailEt = binding.loginEmailText
        passwordEt = binding.loginPasswordText
        signInBtn = binding.singInBtn

        signInBtn.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            LoginViewModel.doLogin(email,password,this)
//
//            if(email.isEmpty() || password.isEmpty()){
//                Toast.makeText(this, "All form must be filled", Toast.LENGTH_SHORT).show()
//            }
//            else{
//
//            }

        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}