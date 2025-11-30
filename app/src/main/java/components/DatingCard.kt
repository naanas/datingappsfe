package com.dating.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dating.frontend.data.User

@Composable
fun DatingCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f), // Kartu mengambil 75% tinggi layar
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Foto Full Size
            AsyncImage(
                model = if (!user.photoUrl.isNullOrEmpty())
                    "http://10.0.2.2:8081/${user.photoUrl}"
                else "https://via.placeholder.com/600x800", // Fallback image
                contentDescription = "User Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Gradient Overlay (Agar teks putih terbaca)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            // 3. Informasi User di Kiri Bawah
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = "${user.fullName}, ${user.id.takeLast(2)}", // Usia harusnya dihitung, pakai dummy id dulu
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                if (!user.jobTitle.isNullOrEmpty() || !user.company.isNullOrEmpty()) {
                    Text(
                        text = "${user.jobTitle ?: ""} @ ${user.company ?: ""}",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user.bio ?: "Tidak ada bio.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f)),
                    maxLines = 3
                )
            }
        }
    }
}