package com.cikup.sumopod.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cikup.sumopod.sample.di.sampleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            startKoin {
                androidContext(applicationContext)
                modules(sampleModule)
            }
        } catch (_: Exception) {
            // Koin already started
        }

        setContent {
            App()
        }
    }
}
