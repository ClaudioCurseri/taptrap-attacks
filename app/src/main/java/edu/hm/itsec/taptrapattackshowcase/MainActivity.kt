package edu.hm.itsec.taptrapattackshowcase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.hm.itsec.taptrapattackshowcase.apptouchjacking.AppTouchjackingActivity
import edu.hm.itsec.taptrapattackshowcase.clickjacking.ClickjackingActivity
import edu.hm.itsec.taptrapattackshowcase.controluielements.ControlUiElementsActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityMainBinding
import edu.hm.itsec.taptrapattackshowcase.deviceadmin.DeviceAdminActivity
import edu.hm.itsec.taptrapattackshowcase.displayoverapps.DisplayOverAppsActivity
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
        binding.btnAppTouchjacking.setOnClickListener {
            val intent = Intent(applicationContext, AppTouchjackingActivity::class.java)
            startActivity(intent)
        }
        binding.btnClickjacking.setOnClickListener {
            val intent = Intent(applicationContext, ClickjackingActivity::class.java)
            startActivity(intent)
        }
        binding.btnControlUiElements.setOnClickListener {
            val intent = Intent(applicationContext, ControlUiElementsActivity::class.java)
            startActivity(intent)
        }
        binding.btnDeviceAdmin.setOnClickListener {
            val intent = Intent(applicationContext, DeviceAdminActivity::class.java)
            startActivity(intent)
        }
        binding.btnDisplayOverApps.setOnClickListener {
            val intent = Intent(applicationContext, DisplayOverAppsActivity::class.java)
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