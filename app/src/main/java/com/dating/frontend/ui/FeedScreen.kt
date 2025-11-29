package com.dating.frontend.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dating.frontend.data.*
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var potentialMatches by remember { mutableStateOf<List<User>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }

    // Load data saat pertama kali dibuka
    LaunchedEffect(Unit) {
        try {
            val userId = SessionManager.currentUser?.id ?: return@LaunchedEffect
            val token = SessionManager.getAuthHeader()
            potentialMatches = RetrofitClient.api.getFeed(token, userId)
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal load feed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Temukan Jodohmu", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (potentialMatches.isNotEmpty() && currentIndex < potentialMatches.size) {
            val user = potentialMatches[currentIndex]
            
            // Kartu Profil
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Foto User
                    AsyncImage(
                        model = if (user.photoUrl != null) "http://10.0.2.2:8081/${user.photoUrl}" else "https://via.placeholder.com/300",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Info User
                    Column(modifier = Modifier.padding(16.dp).weight(0.3f)) {
                        Text(text = "${user.fullName}", style = MaterialTheme.typography.headlineSmall)
                        Text(text = "${user.jobTitle ?: "-"} at ${user.company ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = user.bio ?: "Tidak ada bio", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { handleSwipe(scope, context, user.id, "PASS") { currentIndex++ } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("PASS")
                }
                
                Button(
                    onClick = { handleSwipe(scope, context, user.id, "LIKE") { currentIndex++ } },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("LIKE")
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tidak ada profil lagi di sekitarmu!")
            }
        }
    }
}

fun handleSwipe(
    scope: kotlinx.coroutines.CoroutineScope,
    context: android.content.Context,
    targetId: String,
    action: String,
    onNext: () -> Unit
) {
    scope.launch {
        try {
            val myId = SessionManager.currentUser?.id ?: return@launch
            val req = SwipeRequest(myId, targetId, action)
            val response = RetrofitClient.api.swipe(SessionManager.getAuthHeader(), req)
            
            if (response["status"] == "MATCH") {
                Toast.makeText(context, "IT'S A MATCH! 😍", Toast.LENGTH_LONG).show()
            }
            onNext()
        } catch (e: Exception) {
            Toast.makeText(context, "Error swipe", Toast.LENGTH_SHORT).show()
        }
    }
}