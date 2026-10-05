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
import kotlin.random.Random

private val IndexKey = intPreferencesKey("verb_index")

class IrregularWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val verbs = Verbs.irregular(context)
        provideContent {
            val prefs = currentState<Preferences>()
            val index = (prefs[IndexKey] ?: 0) % verbs.size
            IrregularContent(verbs[index])
        }
    }
}

class IrregularWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = IrregularWidget()
}

class NextVerbAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val total = Verbs.irregular(context).size
        updateAppWidgetState(context, glanceId) { prefs ->
            val current = prefs[IndexKey] ?: 0
            var next = Random.nextInt(total)
            while (next == current && total > 1) {
                next = Random.nextInt(total)
            }
            prefs[IndexKey] = next
        }
        IrregularWidget().update(context, glanceId)
    }
}

private val white = ColorProvider(day = Color.White, night = Color.White)
private val soft = ColorProvider(day = Color(0xFFCFCAFF), night = Color(0xFFCFCAFF))
private val muted = ColorProvider(day = Color(0xFF9A94E0), night = Color(0xFF9A94E0))
private val translateColor = ColorProvider(day = Color(0xFF8A84C8), night = Color(0xFF8A84C8))

@Composable
private fun IrregularContent(v: IrregularVerb) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF0F0C2B))
            .cornerRadius(20.dp)
            .padding(12.dp)
            .clickable(actionRunCallback<NextVerbAction>())
    ) {
        Text(
            v.base,
            style = TextStyle(color = white, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(v.basePhon, style = TextStyle(color = muted, fontSize = 12.sp))
            Text("  ·  ", style = TextStyle(color = muted, fontSize = 12.sp))
            Text(v.es, style = TextStyle(color = soft, fontSize = 13.sp))
        }
        Spacer(modifier = GlanceModifier.height(8.dp))
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text("PAST", style = TextStyle(color = muted, fontSize = 10.sp))
                Text(
                    v.past,
                    style = TextStyle(color = white, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
                Text(v.pastPhon, style = TextStyle(color = muted, fontSize = 11.sp))
            }
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text("PARTICIPLE", style = TextStyle(color = muted, fontSize = 10.sp))
                Text(
                    v.participle,
                    style = TextStyle(color = white, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
                Text(v.participlePhon, style = TextStyle(color = muted, fontSize = 11.sp))
            }
        }
        if (v.exampleEn.isNotEmpty()) {
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text("\"${v.exampleEn}\"", style = TextStyle(color = white, fontSize = 13.sp))
            if (v.exampleEs.isNotEmpty()) {
                Text(
                    v.exampleEs,
                    style = TextStyle(color = translateColor, fontSize = 11.sp, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}
