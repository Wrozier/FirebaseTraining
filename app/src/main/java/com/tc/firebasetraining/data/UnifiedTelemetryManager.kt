package com.tc.firebasetraining.data

import android.os.Bundle
import com.datadog.android.rum.GlobalRumMonitor
import com.datadog.android.rum.RumActionType
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.tc.firebasetraining.domain.TelemetryManager
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.Span

class UnifiedTelemetryManager: TelemetryManager {
    private val tracer = GlobalOpenTelemetry.getTracer("unified-telemtry")
    private val activeSpans = mutableMapOf<String , Span>()

    override fun logEvent(name: String, properties: Map<String, Any>) {
        //firebase
        val bundle = Bundle().apply{
            properties.forEach { (k, v) -> putString(k, v.toString())}
        }
        Firebase.analytics.logEvent(name, bundle)

        //Datadog
        GlobalRumMonitor.get().addAction(RumActionType.CUSTOM, name, properties)

        //Splunk
        tracer.spanBuilder(name)
            .setAttribute("workflow.name", name) //makes it appear in Events tab
            .startSpan()
            .end()
    }


    override fun recordError(e: Throwable, message: String){
        //Crashlytics
        Firebase.crashlytics.recordException(e)

        //Splunk RUM ERROR
        val span = tracer.spanBuilder("error").startSpan()
        span.recordException(e)
        span.setAttribute("message", message)
        span.end()

    }

    override fun startTrace(traceName: String){
        //datadog manual view
        GlobalRumMonitor.get().startView(traceName, traceName)

        //Splunk Span
        val span = tracer.spanBuilder(traceName)
            .setAttribute("workflow.name", traceName)
            .startSpan()
        activeSpans[traceName] = span
    }

    override fun stopTrace(traceName: String){
        //Datadog
        GlobalRumMonitor.get().stopView(traceName)

        //Splunk stop span
        activeSpans[traceName]?.end()
        activeSpans.remove(traceName)
    }

}