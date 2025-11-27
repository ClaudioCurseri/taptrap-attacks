package edu.hm.itsec.taptrapattackshowcase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.hm.itsec.taptrapattackshowcase.clickjacking.ClickjackingActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityMainBinding
import edu.hm.itsec.taptrapattackshowcase.notificationservice.NotificationServiceActivity
import edu.hm.itsec.taptrapattackshowcase.runtimepermissions.RuntimePermissionsActivity
import edu.hm.itsec.taptrapattackshowcase.web.WebActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // set up click listeners for all buttons
        setUpButtons()
    }

    private fun setUpButtons() {
        binding.btnClickjacking.setOnClickListener {
            val intent = Intent(applicationContext, ClickjackingActivity::class.java)
            startActivity(intent)
        }
        binding.btnNotificationService.setOnClickListener {
            val intent = Intent(applicationContext, NotificationServiceActivity::class.java)
            startActivity(intent)
        }
        binding.btnRuntimePermissions.setOnClickListener {
            val intent = Intent(applicationContext, RuntimePermissionsActivity::class.java)
            startActivity(intent)
        }
        binding.btnWeb.setOnClickListener {
            val intent = Intent(applicationContext, WebActivity::class.java)
            startActivity(intent)
        }
    }
}