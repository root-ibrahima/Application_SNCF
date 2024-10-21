package com.barry.application_sncf

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

// Data classes to map JSON response
data class CommercialMode(val id: String, val name: String)

data class SncfApiResponse(val commercial_modes: List<CommercialMode>)

class SncfApiClient(private val apiToken: String) {

    suspend fun getCommercialModes(): List<CommercialMode>? {
        return getRequest("coverage/sncf/commercial_modes")?.commercial_modes
    }

    private suspend fun getRequest(endpoint: String): SncfApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://api.sncf.com/v1/$endpoint")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    val auth = Base64.getEncoder().encodeToString("$apiToken:".toByteArray())
                    setRequestProperty("Authorization", "Basic $auth")

                    // Read response
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val responseText = reader.use { it.readText() }

                    // Parse the response with Gson
                    Gson().fromJson(responseText, SncfApiResponse::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
