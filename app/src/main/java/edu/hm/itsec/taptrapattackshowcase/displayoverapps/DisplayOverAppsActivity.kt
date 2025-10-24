package edu.hm.itsec.taptrapattackshowcase.displayoverapps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityDisplayOverAppsBinding

class DisplayOverAppsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayOverAppsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayOverAppsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}