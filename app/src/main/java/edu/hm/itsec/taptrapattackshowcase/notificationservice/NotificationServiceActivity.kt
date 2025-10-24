package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityNotificationServiceBinding

class NotificationServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationServiceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}