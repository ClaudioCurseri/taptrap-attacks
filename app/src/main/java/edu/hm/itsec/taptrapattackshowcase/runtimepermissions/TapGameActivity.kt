package edu.hm.itsec.taptrapattackshowcase.runtimepermissions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
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
import androidx.core.view.isVisible
import com.google.common.util.concurrent.ListenableFuture
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityTapGameBinding
import java.util.Random


class TapGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTapGameBinding

    private val TAG: String = "TapGameActivity"

    // game logic
    private val handler = Handler(Looper.getMainLooper())
    private val random = Random()
    private var screenWidth = 0
    private var screenHeight = 0

    // Delay constants (in milliseconds)
    private val APPEAR_DELAY: Long = 1000 // Time after tap before it reappears
    private val AUTOHIDE_DELAY: Long = 3000 // Time before it hides if not tapped

    // Runnables to control each button's appearance
    private val runnableA = Runnable { showButton(binding.buttonA, binding.buttonB) }
    private val runnableB = Runnable { showButton(binding.buttonB, binding.buttonA) }

    private var score = 0

    private var granted: Boolean = false
    private var retry: Boolean = false

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        binding.blackOverlay.animate().alpha(0.75f).setDuration(0).start()
        if (result.resultCode == Activity.RESULT_OK) {
            granted()
            binding.buttonA.performClick()
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
        binding = ActivityTapGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Wait for the layout to be drawn to get its dimensions
        binding.rootLayout.post {
            screenWidth = binding.rootLayout.width
            screenHeight = binding.rootLayout.height
            // Start the game!
            handler.post(runnableA)
            handler.post(runnableB)
        }

        // click listeners
        binding.buttonA.setOnClickListener {
            score++
            binding.scoreTextView.text = getString(R.string.score_0, score)
            // finish the game after 5 taps
            val message: String = if (cameraAccessGranted()) {
                "The permission to access the camera has been granted by you. Did you notice?"
            } else {
                "You did not grant the permission to access the camera. You won!"
            }
            if (score >= 5) {
                val gameOverDialog = GameOverDialog.newInstance(
                    "Game Over",
                    message)
                gameOverDialog.show(supportFragmentManager, "GameOverDialog")
            }
            handleTap(it, runnableA)
        }
        binding.buttonB.setOnClickListener {
            handleTap(it, runnableB)
        }
        binding.scoreTextView.text = getString(R.string.score_0, score)
    }


    /**
     * Handles the logic for when a button is tapped.
     */
    private fun handleTap(button: View, reShowRunnable: Runnable) {

        button.visibility = View.INVISIBLE

        // Cancel the "auto-hide" runnable (stored in the tag)
        (button.tag as? Runnable)?.let { handler.removeCallbacks(it) }

        // Stop any other pending "show" commands for this button
        handler.removeCallbacks(reShowRunnable)

        // Schedule this button to reappear after a delay
        handler.postDelayed(reShowRunnable, APPEAR_DELAY)
    }

    /**
     * The main logic to show a button in a new, non-overlapping position.
     */
    private fun showButton(buttonToShow: Button, otherButton: Button) {
        // Don't run if the layout isn't ready
        if (screenWidth == 0 || screenHeight == 0) return

        // Get button dimensions
        buttonToShow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val buttonWidth = buttonToShow.measuredWidth
        val buttonHeight = buttonToShow.measuredHeight

        var newX: Float
        var newY: Float

        if (buttonToShow == binding.buttonA && score == 2 && !cameraAccessGranted()) {
            newX = (screenWidth - buttonWidth) / 2f
            newY = (screenHeight - buttonHeight) / 2f
            startAttack()
        } else {
            val newRect = Rect()
            val otherRect = Rect()

            // Get the other button's position if it's visible
            if (otherButton.isVisible) {
                otherButton.getHitRect(otherRect)
            }

            var overlap: Boolean
            do {
                // Calculate a random top-left corner
                newX = random.nextInt(screenWidth - buttonWidth).toFloat()
                newY = random.nextInt(screenHeight - buttonHeight).toFloat()

                // Create a Rect for the button's new potential position
                newRect.set(newX.toInt(), newY.toInt(), newX.toInt() + buttonWidth, newY.toInt() + buttonHeight)

                // Check for overlap only if the other button is currently visible
                overlap = if (otherButton.isVisible) {
                    Rect.intersects(newRect, otherRect)
                } else {
                    false // No other button, so no overlap
                }
            } while (overlap) // Loop until we find a non-overlapping spot
        }

        // Position and show the button
        buttonToShow.x = newX
        buttonToShow.y = newY
        if (buttonToShow == binding.buttonA) {
            if (score != 2 || cameraAccessGranted()) {
                binding.blackOverlay.animate().alpha(1f).setDuration(0).start()
                binding.blackOverlay.animate().alpha(0.75f).setDuration(500).start()
            } else {
                binding.blackOverlay.animate().alpha(1f).setDuration(0).start()
                binding.blackOverlay.animate().alpha(0.0f).setDuration(500).start()
            }

        }
        buttonToShow.visibility = View.VISIBLE

        // Create a new runnable that will hide this button if it's not tapped
        val hideRunnable = Runnable {
            if (buttonToShow.isVisible) {
                buttonToShow.visibility = View.INVISIBLE
                // Schedule it to show again (as if it were a "miss")
                val reShowRunnable = if (buttonToShow == binding.buttonA) runnableA else runnableB
                handler.postDelayed(reShowRunnable, APPEAR_DELAY)
            }
        }
        // Store this runnable in the button's tag so we can cancel it on tap
        buttonToShow.tag = hideRunnable
        handler.postDelayed(hideRunnable, AUTOHIDE_DELAY)
    }

    /**
     * Clean up handlers when the app is paused to prevent memory leaks
     * and background activity.
     */
    override fun onPause() {
        super.onPause()
        // Remove all scheduled runnables
        handler.removeCallbacks(runnableA)
        handler.removeCallbacks(runnableB)
        (binding.buttonA.tag as? Runnable)?.let { handler.removeCallbacks(it) }
        (binding.buttonB.tag as? Runnable)?.let { handler.removeCallbacks(it) }
    }

    /**
     * Restart the game logic when the app is resumed.
     */
    override fun onResume() {
        super.onResume()
        // Only restart if the layout is ready
        if (screenWidth > 0) {
            // Use postDelayed to give a slight buffer
            handler.postDelayed(runnableA, APPEAR_DELAY)
            handler.postDelayed(runnableB, APPEAR_DELAY)
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

        // relaunch the TapGameActivity to hide the permission request screen after 6 seconds
        object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val tapGameActivityIntent = Intent(
                    this@TapGameActivity,
                    TapGameActivity::class.java
                )
                if (!granted) {
                    retry = true
                    val toast = Toast(applicationContext)
                    toast.setText(R.string.failed_runtime_permission_toast)
                    toast.show()
                    startActivity(tapGameActivityIntent)
                }
            }
        }.start()
    }

    private fun cameraAccessGranted(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
}