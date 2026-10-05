package com.example.v_dash

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import org.json.JSONArray
import kotlin.random.Random

data class PhrasalVerb(
    val phrasal: String,
    val phon: String,
    val es: String,
    val exampleEn: String,
    val exampleEs: String,
    val exampleEn2: String,
    val exampleEs2: String
)

object PhrasalData {
    @Volatile
    private var cachedList: List<PhrasalVerb>? = null

    fun getList(context: Context): List<PhrasalVerb> {
        cachedList?.let { return it }
        return try {
            val jsonString = context.assets.open("phrasal_verbs.json")
                .bufferedReader().use { it.readText() }
            val array = JSONArray(jsonString)
            val list = (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                PhrasalVerb(
                    phrasal = obj.optString("v", ""),
                    phon = obj.optString("ph", ""),
                    es = obj.optString("es", ""),
                    exampleEn = obj.optString("ex", ""),
                    exampleEs = obj.optString("exEs", ""),
                    exampleEn2 = obj.optString("ex2", ""),
                    exampleEs2 = obj.optString("exEs2", "")
                )
            }
            if (list.isEmpty()) {
                listOf(PhrasalVerb("Error", "", "JSON vacío", "", "", "", ""))
            } else {
                cachedList = list
                list
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(PhrasalVerb("Error", "", "No se encontró phrasal_verbs.json", "", "", "", ""))
        }
    }
}

private val PhrasalIndexKey = intPreferencesKey("phrasal_index")
private val SpeakKey = ActionParameters.Key<String>("speak_text")

class NextPhrasalAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val total = PhrasalData.getList(context).size
        updateAppWidgetState(context, glanceId) { prefs ->
            val current = prefs[PhrasalIndexKey] ?: 0
            var next = Random.nextInt(total)
            while (next == current && total > 1) { next = Random.nextInt(total) }
            prefs[PhrasalIndexKey] = next
        }
        PhrasalWidget().update(context, glanceId)
    }
}

class SpeakAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val textToSpeak = parameters[SpeakKey] ?: return
        TtsManager.speak(context, textToSpeak)
    }
}

class PhrasalWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        TtsManager.warmUp(context)
        val verbs = PhrasalData.getList(context)
        provideContent {
            val prefs = currentState<Preferences>()
            val index = (prefs[PhrasalIndexKey] ?: 0) % verbs.size
            PhrasalContent(verbs[index])
        }
    }
}

class PhrasalWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PhrasalWidget()
}

private val white = ColorProvider(day = Color.White, night = Color.White)
private val soft = ColorProvider(day = Color(0xFFCFCAFF), night = Color(0xFFCFCAFF))
private val muted = ColorProvider(day = Color(0xFF9A94E0), night = Color(0xFF9A94E0))
private val translateColor = ColorProvider(day = Color(0xFF8A84C8), night = Color(0xFF8A84C8))

@Composable
private fun PhrasalContent(v: PhrasalVerb) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF0F0C2B))
            .cornerRadius(20.dp)
            .padding(12.dp)
            .clickable(actionRunCallback<NextPhrasalAction>())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = GlanceModifier.fillMaxWidth()) {
            Text(
                v.phrasal,
                style = TextStyle(color = white, fontSize = 22.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                "🔊",
                style = TextStyle(fontSize = 20.sp),
                modifier = GlanceModifier
                    .padding(4.dp)
                    .clickable(actionRunCallback<SpeakAction>(actionParametersOf(SpeakKey to v.phrasal)))
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(v.phon, style = TextStyle(color = muted, fontSize = 12.sp))
            Text("  ·  ", style = TextStyle(color = muted, fontSize = 12.sp))
            Text(v.es, style = TextStyle(color = soft, fontSize = 13.sp))
        }
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text("EXAMPLES", style = TextStyle(color = muted, fontSize = 10.sp))
        Text("\"${v.exampleEn}\"", style = TextStyle(color = white, fontSize = 13.sp))
        if (v.exampleEs.isNotEmpty()) {
            Text(
                v.exampleEs,
                style = TextStyle(color = translateColor, fontSize = 11.sp, fontStyle = FontStyle.Italic)
            )
        }
        if (v.exampleEn2.isNotEmpty()) {
            Spacer(modifier = GlanceModifier.height(6.dp))
            Text("\"${v.exampleEn2}\"", style = TextStyle(color = white, fontSize = 13.sp))
            if (v.exampleEs2.isNotEmpty()) {
                Text(
                    v.exampleEs2,
                    style = TextStyle(color = translateColor, fontSize = 11.sp, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}
