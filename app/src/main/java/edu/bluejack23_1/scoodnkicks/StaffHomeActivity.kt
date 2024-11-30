package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.bluejack23_1.scoodnkicks.ViewModel.StaffHomeViewModel
import edu.bluejack23_1.scoodnkicks.databinding.ActivityStaffHomeBinding

class StaffHomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStaffHomeBinding
    private lateinit var scooterCodeField : EditText
    private lateinit var userEmailField : EditText

    private lateinit var submitBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffHomeBinding.inflate(layoutInflater)

        scooterCodeField = binding.scooterCodeField
        submitBtn = binding.submitButton
        userEmailField = binding.userEmailField

        setContentView(binding.root)

        submitBtn.setOnClickListener {
            val code = scooterCodeField.text.toString()
            val userEmail = userEmailField.text.toString()
            StaffHomeViewModel.validateInput(code,userEmail,this)
        }

        binding.logOutGroup.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }

    }
}