package com.tc.firebasetraining
import android.app.Application
import android.util.Log
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.compose.enableComposeActionTracking
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.splunk.rum.integration.agent.api.AgentConfiguration
import com.splunk.rum.integration.agent.api.EndpointConfiguration
import com.splunk.rum.integration.agent.api.SplunkRum

class FirebaseTrainingApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val datadogConfig = Configuration.Builder(
            clientToken = BuildConfig.DATADOG_CLIENT_TOKEN,
            env = "training",
            variant = "debug",
        )
            .useSite(DatadogSite.US1)
            .build()
        Datadog.initialize(this, datadogConfig, TrackingConsent.GRANTED)
        Datadog.setVerbosity(Log.VERBOSE)


        val rumConfig = RumConfiguration.Builder(BuildConfig.DATADOG_APPLICATION_ID)
            .trackUserInteractions()
            .trackLongTasks()
            .enableComposeActionTracking()
            .build()
        Rum.enable(rumConfig)

        try{
            val endpointConfiguration = EndpointConfiguration(
                realm = BuildConfig.SPLUNK_REALM,
                rumAccessToken =BuildConfig.SPLUNK_RUM_ACCESS_TOKEN,
            )
            val agentConfiguration = AgentConfiguration(
                endpoint = endpointConfiguration,
                appName = "FirebaseTraining",
                "dev",
                enableDebugLogging = true,

            )
            SplunkRum.install(application= this, agentConfiguration)
        } catch (e: Exception ){
            Log.e("Telemetry", "Error installing Splunk RUM")
        }
    }
}