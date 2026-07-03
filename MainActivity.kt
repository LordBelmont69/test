package com.example.storygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StoryGameScreen()
                }
            }
        }
    }
}

/**
 * Holds the mutable game state: which node we're on, and which flags
 * have been set by past choices.
 */
class GameState {
    var currentId by mutableStateOf(Story.START_ID)
    private val flags = mutableStateListOf<String>()

    val currentNode: StoryNode
        get() = Story.nodes[currentId] ?: error("Missing node: $currentId")

    val availableChoices: List<Choice>
        get() = currentNode.choices.filter { it.requiresFlag == null || it.requiresFlag in flags }

    fun choose(choice: Choice) {
        choice.setsFlag?.let { flags.add(it) }
        currentId = choice.nextId
    }

    fun restart() {
        flags.clear()
        currentId = Story.START_ID
    }
}

@Composable
fun StoryGameScreen() {
    val gameState = remember { GameState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Shattered Crown",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = gameState.currentId,
            modifier = Modifier.weight(1f),
            label = "story-node"
        ) {
            val node = Story.nodes[it] ?: return@AnimatedContent
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = node.text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (gameState.currentNode.isEnding) {
            Button(
                onClick = { gameState.restart() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Play Again")
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                gameState.availableChoices.forEach { choice ->
                    OutlinedButton(
                        onClick = { gameState.choose(choice) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = choice.text,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
