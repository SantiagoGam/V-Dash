package com.example.v_dash

import android.content.Context
import org.json.JSONArray

data class IrregularVerb(
    val base: String,
    val basePhon: String,
    val past: String,
    val pastPhon: String,
    val participle: String,
    val participlePhon: String,
    val es: String,
    val exampleEn: String,
    val exampleEs: String
)

object Verbs {
    @Volatile
    private var cache: List<IrregularVerb>? = null

    private fun cap(s: String) = s.replaceFirstChar { it.uppercase() }

    fun irregular(context: Context): List<IrregularVerb> {
        cache?.let { return it }
        return try {
            val text = context.assets.open("irregular_verbs.json")
                .bufferedReader().use { it.readText() }
            val arr = JSONArray(text)
            val list = (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                IrregularVerb(
                    base = cap(o.getString("b")),
                    basePhon = o.getString("bp"),
                    past = cap(o.getString("p")),
                    pastPhon = o.getString("pp"),
                    participle = cap(o.getString("pt")),
                    participlePhon = o.getString("ptp"),
                    es = o.optString("es"),
                    exampleEn = o.optString("ex"),
                    exampleEs = o.optString("exEs")
                )
            }
            cache = list
            list
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(IrregularVerb("Error", "", "", "", "", "", "No se pudo leer irregular_verbs.json", "", ""))
        }
    }
}
