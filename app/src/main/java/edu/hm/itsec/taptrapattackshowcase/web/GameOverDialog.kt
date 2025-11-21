package edu.hm.itsec.taptrapattackshowcase.web

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.hm.itsec.taptrapattackshowcase.R

class GameOverDialog : DialogFragment() {

    private val TAG = "GameOverDialog"

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
                activity.finish()
            }

        return builder.create()
    }

    companion object {
        fun newInstance(title: String, message: String): GameOverDialog {
            val fragment = GameOverDialog()
            val args = Bundle()
            args.putString("DIALOG_TITLE", title)
            args.putString("DIALOG_MESSAGE", message)
            fragment.arguments = args
            return fragment
        }
    }

}