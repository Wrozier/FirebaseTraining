package com.tc.firebasetraining.domain

interface TelemetryManager {
    fun logEvent(name:String , properties: Map<String, Any> = emptyMap())
    fun recordError(e: Throwable, message: String)
    fun startTrace(traceName: String)
    fun stopTrace(traceName: String)
}