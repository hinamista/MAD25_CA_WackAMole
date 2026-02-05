package com.example.wackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    userId: Long,
    onOpenSettings: () -> Unit,
    onOpenLeaderboard: () -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var moleIndex by remember { mutableIntStateOf(-1) }
    var isRunning by remember { mutableStateOf(false) }
    var showGameOver by remember { mutableStateOf(false) }

    var moleToken by remember { mutableIntStateOf(0) }
    var lastScoredToken by remember { mutableIntStateOf(-1) }

    // Mole movement
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect
        while (isRunning) {
            delay((700..1000).random().toLong())
            moleIndex = (0..8).random()
            moleToken++
        }
    }

    // CountDown Timer
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect
        while (isRunning) {
            delay(1000L)
            timeLeft--
            if (timeLeft <= 0) {
                timeLeft = 0
                isRunning = false
                moleIndex = -1
                showGameOver = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wack-a-Mole") },
                actions = {
                    IconButton(onClick = onOpenLeaderboard) {
                        Icon(Icons.Filled.Leaderboard, contentDescription = "Leaderboard")
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text("Score: $score", Modifier.weight(1f), textAlign = TextAlign.Start)
                Text("Time: $timeLeft", Modifier.weight(1f), textAlign = TextAlign.End)
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                for (r in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        for (c in 0..2) {
                            val index = r * 3 + c
                            FilledTonalButton(
                                onClick = {
                                    if (isRunning && index == moleIndex && lastScoredToken != moleToken) {
                                        score++
                                        lastScoredToken = moleToken
                                    }
                                },
                                enabled = isRunning,
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
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
                    moleToken = 0
                    lastScoredToken = -1
                    showGameOver = false
                    isRunning = true
                },
                shape = RoundedCornerShape(50)
            ) {
                Text(if (isRunning) "Restart" else "Start")
            }

            if (showGameOver) {
                Spacer(Modifier.height(8.dp))
                Text("Game over! Final score: $score")
            }
        }
    }
}