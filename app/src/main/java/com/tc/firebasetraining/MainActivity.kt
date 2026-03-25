package com.tc.firebasetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datadog.android.rum.GlobalRumMonitor
import com.datadog.android.rum.RumActionType
import com.tc.firebasetraining.data.UnifiedTelemetryManager
import com.tc.firebasetraining.ui.theme.FirebaseTrainingTheme

class MainActivity : ComponentActivity() {

    private lateinit var telemetry: UnifiedTelemetryManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalRumMonitor.get().startView("main_screen", "Main Telemetry Screen")

        telemetry = UnifiedTelemetryManager()



        enableEdgeToEdge()
        setContent {
            FirebaseTrainingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   TelemetryScreen(
                       onLogEvent = {
                           telemetry.logEvent("user_action_log",
                               mapOf("screen" to "main"))
                       },
                       onTriggerError = {
                           try {
                               throw RuntimeException("Handled exception for training")
                           } catch (e: Exception) {
                               telemetry.recordError(e, "Handled exception for training")
                           }
                       },
                       onFatalCrash = {
                           throw RuntimeException("Simulated Crash")
                       },
                       onStartTrace = {telemetry.startTrace("checkout_flow") },
                       onStopTrace = {telemetry.stopTrace("checkout_flow") },
                       modifier = Modifier.padding(innerPadding)

                   )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        GlobalRumMonitor.get().stopView("main_screen")

    }

}

@Composable
fun TelemetryScreen(
    onLogEvent: () -> Unit,
    onTriggerError: () -> Unit,
    onFatalCrash: () -> Unit,
    onStartTrace : () -> Unit,
    onStopTrace: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "telemetry training app", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                GlobalRumMonitor.get().addAction(
                    RumActionType.CLICK,
                    "Log Event Button Clicked",
                    emptyMap()
                )
                onLogEvent()
            },
            modifier = Modifier
                .testTag("btn_log_event")
        ) {
            Text("Log Event (Firebase/Datadog)")
        }

        Button(
            onClick = {
                GlobalRumMonitor.get().addAction(
                    RumActionType.CLICK,
                    "Fatal Crash Button Clicked",
                    emptyMap()
                )
                onFatalCrash()
            },
            modifier = Modifier
                .testTag("btn_fatal_crash")
        ) {
            Text("Trigger Fatal Crash")
        }

        Button(
            onClick = {
                GlobalRumMonitor.get().addAction(
                    RumActionType.CLICK,
                    "Trigger Error Button Clicked",
                    emptyMap()
                )
                onTriggerError()
            },
            modifier = Modifier
                .testTag("btn_trigger_error")
        ) {
            Text("Trigger Error")
        }

        Button(
            onClick = {
                GlobalRumMonitor.get().addAction(
                    RumActionType.CLICK,
                    "Start Trace Button Clicked",
                    emptyMap()
                )
                onStartTrace()
            },
            modifier = Modifier
                .testTag("btn_start_trace")
        ) {
            Text("Start Trace")
        }

        Button(
            onClick = {
                GlobalRumMonitor.get().addAction(
                    RumActionType.CLICK,
                    "Stop Trace Button Clicked",
                    emptyMap()
                )
                onStopTrace()
            },
            modifier = Modifier
                .testTag("btn_stop_trace")
        ) {
            Text("Stop Trace")
        }
    }
}