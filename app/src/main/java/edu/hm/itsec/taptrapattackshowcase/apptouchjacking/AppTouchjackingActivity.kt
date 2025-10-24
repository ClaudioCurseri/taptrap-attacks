package edu.hm.itsec.taptrapattackshowcase.apptouchjacking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityAppTouchjackingBinding

class AppTouchjackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppTouchjackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppTouchjackingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}