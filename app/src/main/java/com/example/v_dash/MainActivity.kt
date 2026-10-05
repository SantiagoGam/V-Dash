package com.example.v_dash

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Muestra un pequeño mensaje flotante para avisar que es solo un widget
        Toast.makeText(this, "V-Dash: Usa los widgets en tu pantalla de inicio", Toast.LENGTH_SHORT).show()

        // Cierra la pantalla de inmediato antes de que llegue a mostrarse
        finish()
    }
}