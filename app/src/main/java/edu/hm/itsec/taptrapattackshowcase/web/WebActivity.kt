package edu.hm.itsec.taptrapattackshowcase.web

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnWebStartGame.setOnClickListener {
            // start TapGameWebActivity
            val intent = Intent(applicationContext, TapGameWebActivity::class.java)
            startActivity(intent)
        }
    }
}