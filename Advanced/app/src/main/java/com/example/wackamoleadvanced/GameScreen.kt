package com.example.wackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wackamoleadvanced.data.DbProvider
import com.example.wackamoleadvanced.data.ScoreEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    userId: Long,
    onOpenSettings: () -> Unit,
    onOpenLeaderboard: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DbProvider.get(context) }

    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(30) }
    var moleIndex by remember { mutableIntStateOf(-1) }
    var isRunning by remember { mutableStateOf(false) }
    var showGameOver by remember { mutableStateOf(false) }

    var moleToken by remember { mutableIntStateOf(0) }
    var lastScoredToken by remember { mutableIntStateOf(-1) }

    var savedThisRound by remember { mutableStateOf(false) }

    // Mole movement
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect

        while (isRunning) {
            val nextDelay = (700..1000).random()
            delay(nextDelay.toLong())
            moleIndex = (0..8).random()
            moleToken += 1
        }
    }

    // Countdown loop
    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect

        while (isRunning) {
            delay(1000L)
            timeLeft -= 1

            if (timeLeft <= 0) {
                timeLeft = 0
                isRunning = false
                moleIndex = -1
                showGameOver = true

                if (!savedThisRound && userId > 0) {
                    savedThisRound = true
                    scope.launch {
                        db.scoreDao().insert(
                            ScoreEntity(
                                userId = userId,
                                score = score,
                                createdAt = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wack-a-Mole") },
                actions = {
                    IconButton(onClick = onOpenLeaderboard) {
                        Icon(Icons.Filled.List, contentDescription = "Leaderboard")
                    }
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
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Score: $score",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Time: $timeLeft",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(Modifier.height(18.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (r in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        for (c in 0..2) {
                            val index = r * 3 + c

                            FilledTonalButton(
                                onClick = {
                                    if (
                                        isRunning &&
                                        index == moleIndex &&
                                        lastScoredToken != moleToken
                                    ) {
                                        score += 1
                                        lastScoredToken = moleToken
                                    }
                                },
                                enabled = isRunning,
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp),
                                contentPadding = PaddingValues(0.dp)
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
                    isRunning = true

                    moleToken = 0
                    lastScoredToken = -1

                    savedThisRound = false
                },
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(if (isRunning) "Restart" else "Start")
            }

            Spacer(Modifier.height(10.dp))

            if (showGameOver) {
                Text("Game over! Final score: $score")
            }
        }
    }
}