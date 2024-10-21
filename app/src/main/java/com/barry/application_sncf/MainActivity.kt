package com.barry.application_sncf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.barry.application_sncf.ui.theme.ApplicationSNCFTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationSNCFTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    ApiContent()
                }
            }
        }
    }
}

@Composable
fun ApiContent() {
    val client = SncfApiClient(apiToken = "c899601c-13c9-4764-a944-7e4b6f010597")  
    val coroutineScope = rememberCoroutineScope()

    var commercialModes by remember { mutableStateOf<List<CommercialMode>?>(null) }

    // Fetch data on UI load
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            commercialModes = client.getCommercialModes()
        }
    }

    // Display data or loading state
    if (commercialModes == null) {
        Text(text = "Chargement des modes commerciaux...")
    } else {
        CommercialModesList(commercialModes!!)
    }
}

@Composable
fun CommercialModesList(commercialModes: List<CommercialMode>) {
    LazyColumn {
        items(commercialModes) { mode ->
            CommercialModeItem(mode)
        }
    }
}

@Composable
fun CommercialModeItem(mode: CommercialMode) {
    Column {
        Text(text = "ID: ${mode.id}")
        Text(text = "Nom: ${mode.name}")
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApplicationSNCFTheme {
        CommercialModesList(
            listOf(
                CommercialMode("TGV", "TGV INOUI"),
                CommercialMode("TER", "TER")
            )
        )
    }
}
