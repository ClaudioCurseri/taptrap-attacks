package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityNotificationServiceBinding

class NotificationServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationServiceBinding

    private var transparencyDeactivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationServiceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStartNotifAttack.setOnClickListener {
            val intent = Intent(applicationContext, NotificationServiceAttackActivity::class.java).apply {
                putExtra("transparencyDeactivated", transparencyDeactivated)
            }
            startActivity(intent)
        }

        binding.switchNotificationTransparency.setOnCheckedChangeListener { _, isChecked ->
            transparencyDeactivated = isChecked
        }
    }
}