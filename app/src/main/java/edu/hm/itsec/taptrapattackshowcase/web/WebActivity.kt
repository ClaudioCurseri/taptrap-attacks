package edu.hm.itsec.taptrapattackshowcase.web

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityWebBinding

/**
 * Activity with start button to start Web Permission Bypass attack.
 * Transparency of CustomTabs can be deactivated with switch.
 */
class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding

    private var transparencyDeactivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnWebStartGame.setOnClickListener {
            // start TapGameWebActivity
            val intent = Intent(applicationContext, TapGameWebActivity::class.java)
            // add state of transparency_switch
            intent.putExtra("transparencyDeactivated", transparencyDeactivated)
            startActivity(intent)
        }

        binding.switchWebTransparency.setOnCheckedChangeListener { _, isChecked ->
            transparencyDeactivated = isChecked
        }
    }
}