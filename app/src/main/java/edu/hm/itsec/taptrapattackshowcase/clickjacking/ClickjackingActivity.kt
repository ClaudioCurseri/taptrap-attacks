package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityClickjackingBinding

/**
 * Activity with start button to start Clickjacking demonstration.
 * Transparency of customTabs can be deactivated with switch.
 */
class ClickjackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClickjackingBinding

    private var transparencyDeactivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClickjackingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnClickjackingStart.setOnClickListener {
            // start BookSelectionActivity
            val intent = Intent(applicationContext, BookSelectionActivity::class.java)
            // add state of transparency_switch
            intent.putExtra("transparencyDeactivated", transparencyDeactivated)
            startActivity(intent)
        }

        binding.switchClickjackingTransparency.setOnCheckedChangeListener { _, isChecked ->
            transparencyDeactivated = isChecked
        }
    }
}