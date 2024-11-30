package edu.bluejack23_1.scoodnkicks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_1.scoodnkicks.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bckBtn.setOnClickListener{
            this.finish()
        }
    }

}