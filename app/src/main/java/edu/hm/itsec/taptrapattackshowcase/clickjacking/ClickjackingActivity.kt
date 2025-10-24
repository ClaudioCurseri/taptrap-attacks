package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityClickjackingBinding

class ClickjackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClickjackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickjackingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}