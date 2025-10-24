package edu.hm.itsec.taptrapattackshowcase.controluielements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityControlUiElementsBinding

class ControlUiElementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlUiElementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlUiElementsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}