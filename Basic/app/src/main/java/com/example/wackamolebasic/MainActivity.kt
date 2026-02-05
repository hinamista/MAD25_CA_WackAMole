package com.example.wackamolebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppNav() } }
    }
}

@Composable
private fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "game") {
        composable("game") { GameScreen(onOpenSettings = { nav.navigate("settings") }) }
        composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScreen(onOpenSettings: () -> Unit) {
    var score by remember { mutableIntStateOf(0) }
    var highScore by remember { mutableIntStateOf(5) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var moleIndex by remember { mutableIntStateOf(-1) }
    var isRunning by remember { mutableStateOf(false) }
    var showGameOver by remember { mutableStateOf(false) }

    // Mole Movement
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect

        while (isRunning) {
            val nextDelay = (700..1000).random()
            delay(nextDelay.toLong())
            moleIndex = (0..8).random()
        }
    }

    // Countdown Timer
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect

        while (isRunning) {
            delay(1000L)
            timeLeft -= 1

            if (timeLeft <= 0) {
                timeLeft = 0
                isRunning = false
                showGameOver = true
                moleIndex = -1
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wack-a-Mole") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Score: $score", modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                Text("High score: $highScore", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text("Time: $timeLeft", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Spacer(Modifier.height(18.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                for (r in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        for (c in 0..2) {
                            val index = r * 3 + c
                            FilledTonalButton(
                                onClick = {
                                    if (isRunning && index == moleIndex) {
                                        score += 1
                                    }
                                },
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(if (index == moleIndex) "M" else "")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    score = 0
                    timeLeft = 30
                    moleIndex = -1
                    showGameOver = false
                    isRunning = !isRunning
                },
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(if (isRunning) "Restart" else "Start")
            }

            Spacer(Modifier.height(10.dp))

            if (showGameOver) {
                Text("Game over! Final score: $score")
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Text(
                text = "Settings screen",
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
