package edu.hm.itsec.taptrapattackshowcase.clickjacking

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.hm.itsec.taptrapattackshowcase.R

/**
 * Custom Dialog, that starts the ClickjackingActivity when the Ok-button is tapped.
 */
class CustomDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = requireActivity()
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.standard_alert_dialog, null)
        val title = arguments?.getString("DIALOG_TITLE") ?: "Default title"
        val message = arguments?.getString("DIALOG_MESSAGE") ?: "Default message"

        view.findViewById<TextView>(R.id.standard_dialog_title)?.text = title
        view.findViewById<TextView>(R.id.standard_dialog_message)?.text = message

        setCancelable(false)

        val builder = MaterialAlertDialogBuilder(activity)
            .setView(view)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(activity, ClickjackingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                activity.finish()
            }

        return builder.create()
    }

    companion object {
        fun newInstance(title: String, message: String): CustomDialog {
            val fragment = CustomDialog()
            val args = Bundle()
            args.putString("DIALOG_TITLE", title)
            args.putString("DIALOG_MESSAGE", message)
            fragment.arguments = args
            return fragment
        }
    }
}