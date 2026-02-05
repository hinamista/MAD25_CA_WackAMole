package com.example.wackamoleadvanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wackamoleadvanced.data.DbProvider
import com.example.wackamoleadvanced.data.ScoreEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    userId: Long,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DbProvider.get(context) }

    var topScores by remember { mutableStateOf<List<ScoreEntity>>(emptyList()) }
    var myScores by remember { mutableStateOf<List<ScoreEntity>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    fun refresh() {
        error = null
        scope.launch {
            try {
                topScores = db.scoreDao().getTopScores(10)
                myScores = if (userId > 0) db.scoreDao().getUserScores(userId) else emptyList()
            } catch (e: Exception) {
                error = e.message ?: "Failed to load scores"
            }
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { refresh() }) { Text("Refresh") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
            }

            Text("Top 10 Runs", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (topScores.isEmpty()) {
                Text("No scores yet.")
            } else {
                ScoreTable(scores = topScores, showUserId = true)
            }

            Spacer(Modifier.height(20.dp))

            Text("My Recent Scores", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (userId <= 0L) {
                Text("No user logged in.")
            } else if (myScores.isEmpty()) {
                Text("You have no saved scores yet.")
            } else {
                ScoreTable(scores = myScores.take(10), showUserId = false)
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ScoreTable(
    scores: List<ScoreEntity>,
    showUserId: Boolean
) {
    val dateFmt = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        scores.forEachIndexed { i, s ->
            Surface(
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "#${i + 1}  Score: ${s.score}",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = dateFmt.format(Date(s.createdAt)),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (showUserId) {
                        Text(
                            text = "User ${s.userId}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}