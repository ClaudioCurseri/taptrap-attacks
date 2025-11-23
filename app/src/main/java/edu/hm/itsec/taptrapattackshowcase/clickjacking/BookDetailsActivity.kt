package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import edu.hm.itsec.taptrapattackshowcase.Constants
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityBookDetailsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Activity to show the details of a book.
 * The activity starts a CustomTab and shows the cart of the web shop.
 * A tap on the "start reading" button is a tap on "purchase" in the web shop.
 */
class BookDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding

    private var transparencyDeactivated = false

    private var customTabOpened = false

    private var timer: CountDownTimer? = null

    private var purchased = false

    private lateinit var selectedBookTitle: String
    private lateinit var selectedBookAuthor: String
    private lateinit var selectedBookSummary: String

    private var addedProductToCart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        transparencyDeactivated = intent.getBooleanExtra("transparencyDeactivated", false)
        customTabOpened = intent.getBooleanExtra("customTabOpened", false)
        addedProductToCart = intent.getBooleanExtra("addedProductToCart", false)
        selectedBookTitle = intent.getStringExtra("selectedBookTitle").toString()
        selectedBookAuthor = intent.getStringExtra("selectedBookAuthor").toString()
        selectedBookSummary = intent.getStringExtra("selectedBookSummary").toString()

        binding.textBookDetailsTitle.text = Html.fromHtml(getString(R.string.book_title, selectedBookTitle), FROM_HTML_MODE_LEGACY)
        binding.textBookDetailsAuthor.text = Html.fromHtml(getString(R.string.book_author, selectedBookAuthor), FROM_HTML_MODE_LEGACY)
        binding.textBookDetailsSummary.text = getString(R.string.book_summary, selectedBookSummary)

        binding.btnStartReading.setOnClickListener {
            // Show Dialog
            val title: String = if(purchased) {
                "Clickjacking Succeeded"
            } else {
                "Clickjacking Not Succeeded"
            }

            val message: String = if(purchased) {
                "You bought a product in a web shop. Did you notice?"
            } else {
                "Congratulations! You did not buy any product."
            }

            val dialog = CustomDialog.newInstance(
                title,
                message
            )
            dialog.show(supportFragmentManager, "CustomDialog")
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Start BookSelectionActivity with flags to prevent taking CustomTab from BackStack
                val bookSelectionActivity = Intent(this@BookDetailsActivity, BookSelectionActivity::class.java)
                bookSelectionActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(bookSelectionActivity)
                finish()
            }
        })
    }

    override fun onPostResume() {
        super.onPostResume()

        if (!customTabOpened && addedProductToCart) {
            customTabOpened = true
            // Start CustomTab after short delay to make sure activity is rendered
            lifecycleScope.launch {
                delay(400)
                openCustomTab()
            }
        }
    }

    /**
     * Called, when the user is redirected to the app from the web shop.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val data = intent.data
        if (data?.scheme == "myapp" && data.host == "clickjacking-second-success-button") {
            timer?.cancel()
            purchased = true
            binding.btnStartReading.performClick()
        }
    }

    /**
     * Starts CustomTab with web shop (/cart page) and hides it by a custom animation.#
     * The activity is restarted after 6 seconds to hide CustomTab.
     */
    private fun openCustomTab() {
        // Create an Intent to open the CustomTabs activity
        val url = Constants.DEMO_WEBSITE_URL
        var fadeInAnimation = R.anim.fade_in_clickjacking_transparent
        if (transparencyDeactivated) {
            fadeInAnimation = R.anim.fade_in_clickjacking
        }
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(false)
            .setUrlBarHidingEnabled(true)
            .setStartAnimations(
                this,
                fadeInAnimation,
                R.anim.fade_out
            )
            .build()

        customTabsIntent.launchUrl(this, "$url/cart".toUri())

        // Relaunch activity after 6 seconds to hide CustomTab
        timer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val bookDetailsActivityIntent = Intent(
                    this@BookDetailsActivity,
                    BookDetailsActivity::class.java
                )
                bookDetailsActivityIntent.putExtra("customTabOpened", customTabOpened)
                bookDetailsActivityIntent.putExtra("selectedBookTitle", selectedBookTitle)
                bookDetailsActivityIntent.putExtra("selectedBookAuthor", selectedBookAuthor)
                bookDetailsActivityIntent.putExtra("selectedBookSummary", selectedBookSummary)
                val toast = Toast(applicationContext)
                toast.setText("Restarting...")
                toast.show()
                startActivity(bookDetailsActivityIntent)
                finish()
            }
        }.start()
    }
}