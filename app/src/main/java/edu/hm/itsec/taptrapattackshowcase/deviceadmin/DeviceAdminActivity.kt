package edu.hm.itsec.taptrapattackshowcase.deviceadmin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityDeviceAdminBinding

class DeviceAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}