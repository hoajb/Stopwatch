package vn.hoanguyen.compose.stopwatch

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import vn.hoanguyen.compose.stopwatch.service.StopwatchService
import vn.hoanguyen.compose.stopwatch.ui.theme.StopwatchTheme
import vn.hoanguyen.compose.stopwatch.viewmodel.EmptyScreen

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            Log.d(TAG, "isBound = true")
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
            Log.d(TAG, "isBound = false")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchTheme {
                if (isBound) {
                    MainScreenUI(stopwatchStateManagement = stopwatchService.stopwatchStateManagement)
                }
                else {
                    EmptyScreen()
                }
            }
        }

        requestPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        Log.d(TAG, "isBound = onStart")
    }


    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false

        Log.d(TAG, "isBound = onStop")
    }
}