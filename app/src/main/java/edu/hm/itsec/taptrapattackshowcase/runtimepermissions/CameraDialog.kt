package edu.hm.itsec.taptrapattackshowcase.runtimepermissions

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.hm.itsec.taptrapattackshowcase.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CameraDialog : DialogFragment() {

    private val TAG = "CameraDialog"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = requireActivity()
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.custom_alert_dialog, null)
        val title = arguments?.getString("DIALOG_TITLE") ?: "Default title"
        val message = arguments?.getString("DIALOG_MESSAGE") ?: "Default message"

        view.findViewById<TextView>(R.id.custom_dialog_title)?.text = title
        view.findViewById<TextView>(R.id.custom_dialog_message)?.text = message

        setCancelable(false)

        val builder = MaterialAlertDialogBuilder(activity)
            .setView(view)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                activity.finish()
            }

        // launch camera after dialog is created
        MainScope().launch {
            startCamera(view.findViewById(R.id.preview_view))
        }

        return builder.create()
    }

    private fun startCamera(previewView: androidx.camera.view.PreviewView) {
        val context = requireContext()

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Camera permission not granted")
            return
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                preview.setSurfaceProvider(previewView.surfaceProvider)

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)

            } catch (e: Exception) {
                Log.e(TAG, "Error starting camera preview", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    companion object {
        fun newInstance(title: String, message: String): CameraDialog {
            val fragment = CameraDialog()
            val args = Bundle()
            args.putString("DIALOG_TITLE", title)
            args.putString("DIALOG_MESSAGE", message)
            fragment.arguments = args
            return fragment
        }
    }
}
