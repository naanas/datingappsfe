package com.dating.frontend.ui

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dating.frontend.data.*
import com.dating.frontend.ui.components.DatingCard
import com.dating.frontend.ui.theme.PrimaryPink
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// TAMBAHKAN ANOTASI INI UNTUK MENGHILANGKAN ERROR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // State Data
    var potentialMatches by remember { mutableStateOf<List<User>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // State Gesture
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Load Data Awal
    LaunchedEffect(Unit) {
        try {
            val userId = SessionManager.currentUser?.id ?: return@LaunchedEffect
            val token = SessionManager.getAuthHeader()
            potentialMatches = RetrofitClient.api.getFeed(token, userId)
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal load feed: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    fun resetCard() {
        offsetX = 0f
        offsetY = 0f
    }

    fun onSwipeComplete(action: String, targetId: String) {
        scope.launch {
            try {
                val myId = SessionManager.currentUser?.id ?: return@launch
                val req = SwipeRequest(myId, targetId, action)
                val response = RetrofitClient.api.swipe(SessionManager.getAuthHeader(), req)

                if (response["status"] == "MATCH") {
                    Toast.makeText(context, "IT'S A MATCH! 😍", Toast.LENGTH_LONG).show()
                }
                currentIndex++
                resetCard()
            } catch (e: Exception) {
                Toast.makeText(context, "Error swipe", Toast.LENGTH_SHORT).show()
                resetCard()
            }
        }
    }

    Scaffold(
        topBar = {
            // Komponen ini yang menyebabkan error jika tanpa @OptIn
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Discover",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = PrimaryPink)
            } else if (currentIndex >= potentialMatches.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tidak ada profil lagi di sekitarmu!", color = Color.Gray)
                    Button(
                        onClick = {
                            isLoading = true
                            // Trigger reload logic here
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Refresh") }
                }
            } else {
                val user = potentialMatches[currentIndex]
                val rotation by animateFloatAsState(targetValue = offsetX / 60f, label = "rotation")

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .graphicsLayer(rotationZ = rotation)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        if (offsetX > 300) {
                                            onSwipeComplete("LIKE", user.id)
                                        } else if (offsetX < -300) {
                                            onSwipeComplete("PASS", user.id)
                                        } else {
                                            resetCard()
                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        DatingCard(user = user)

                        if (offsetX > 100) {
                            Text(
                                "LIKE",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = Color.Green,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                                ),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(40.dp)
                                    .rotate(-20f)
                                    .graphicsLayer { alpha = offsetX / 500f }
                            )
                        } else if (offsetX < -100) {
                            Text(
                                "NOPE",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    color = Color.Red,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                                ),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(40.dp)
                                    .rotate(20f)
                                    .graphicsLayer { alpha = -offsetX / 500f }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp, start = 32.dp, end = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onSwipeComplete("PASS", user.id) },
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.White, CircleShape)
                                .graphicsLayer {
                                    shadowElevation = 10f
                                    shape = CircleShape
                                    clip = true
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Pass",
                                tint = Color(0xFFFF655B),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        IconButton(
                            onClick = { onSwipeComplete("LIKE", user.id) },
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color(0xFFFD297B), Color(0xFFFF655B))
                                    ),
                                    shape = CircleShape
                                )
                                .graphicsLayer {
                                    shadowElevation = 10f
                                    shape = CircleShape
                                    clip = true
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Like",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}