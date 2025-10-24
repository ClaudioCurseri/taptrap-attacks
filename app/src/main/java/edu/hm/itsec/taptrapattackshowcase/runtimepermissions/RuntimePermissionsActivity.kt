package edu.hm.itsec.taptrapattackshowcase.runtimepermissions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.hm.itsec.taptrapattackshowcase.databinding.ActivityRuntimePermissionsBinding

class RuntimePermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRuntimePermissionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRuntimePermissionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}