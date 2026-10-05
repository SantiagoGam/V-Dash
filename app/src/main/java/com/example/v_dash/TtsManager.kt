package com.example.v_dash

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object TtsManager {
    private val lock = Any()
    private var tts: TextToSpeech? = null
    private var ready = false
    private var pending: String? = null

    // Prepara el motor de voz en segundo plano para que el primer toque ya lo encuentre listo
    fun warmUp(context: Context) {
        synchronized(lock) {
            if (tts != null) return
            tts = TextToSpeech(context.applicationContext) { status ->
                synchronized(lock) {
                    if (status == TextToSpeech.SUCCESS) {
                        tts?.language = Locale.US
                        tts?.setSpeechRate(0.85f)
                        ready = true
                        pending?.let { speakNow(it) }
                        pending = null
                    } else {
                        tts = null
                        ready = false
                        pending = null
                    }
                }
            }
        }
    }

    fun speak(context: Context, text: String) {
        synchronized(lock) {
            if (ready) {
                speakNow(text)
            } else {
                // Si el motor aún no está listo, guarda el texto y lo dice apenas termine de arrancar
                pending = text
                warmUp(context)
            }
        }
    }

    private fun speakNow(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "vdash")
    }
}
