package edu.bluejack23_1.scoodnkicks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_1.scoodnkicks.databinding.ActivityAdminUserBinding
import edu.bluejack23_1.scoodnkicks.databinding.ActivityHomeBinding

class AdminUser : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bckBtn.setOnClickListener{
            this.finish()
        }
    }
}