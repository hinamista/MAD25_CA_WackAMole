package com.example.wackamoleadvanced

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLogin: (Long) -> Unit, onSignup: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Login") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Button(onClick = { onLogin(1L) }) {
                Text("Temporary Login")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onSignup) {
                Text("Go to Sign Up")
            }
        }
    }
}