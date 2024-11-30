package edu.bluejack23_1.scoodnkicks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import edu.bluejack23_1.scoodnkicks.ViewModel.UserProfileViewModel
import edu.bluejack23_1.scoodnkicks.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var profile_name : TextView
    private lateinit var editProfileButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        profile_name = binding.profileName
        editProfileButton = binding.editProfileBtn

        setContentView(binding.root)

        val email = intent.getStringExtra("email").toString()

        UserProfileViewModel.getUserById(email){user->
            profile_name.text = user?.result?.getString("first_name").toString() + " " + user?.result?.getString("last_name").toString()


        }

        editProfileButton.setOnClickListener {
            val intent = Intent(this, UserEditProfileActivity::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
        }



    }
}