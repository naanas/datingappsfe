package com.dating.frontend.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dating.frontend.data.RegisterRequest
import com.dating.frontend.data.RetrofitClient
import com.dating.frontend.ui.theme.DatingGradient
import com.dating.frontend.ui.theme.PrimaryPink
import com.dating.frontend.ui.theme.SecondaryOrange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SecondaryOrange.copy(alpha = 0.1f), PrimaryPink.copy(alpha = 0.1f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Buat Akun Baru",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    brush = DatingGradient
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Input Nama
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nama Lengkap") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryPink) },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryPink) },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryPink) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio Singkat") },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryPink) },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPink,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Pilihan Gender
            Text("Jenis Kelamin", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderOption(label = "Laki-laki", selected = gender == "MALE") { gender = "MALE" }
                GenderOption(label = "Perempuan", selected = gender == "FEMALE") { gender = "FEMALE" }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Register
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            val request = RegisterRequest(
                                email = email,
                                password = password,
                                fullName = fullName,
                                gender = gender,
                                bio = bio
                            )
                            RetrofitClient.api.register(request)
                            Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()
                            navController.navigateUp()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Registrasi Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DatingGradient)
                        .then(if (isLoading) Modifier.background(Color.Gray) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("DAFTAR SEKARANG", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigateUp() }) {
                Text("Sudah punya akun? Login di sini", color = PrimaryPink)
            }
        }
    }
}

// Komponen Gender Button
@Composable
fun GenderOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) PrimaryPink else Color.LightGray.copy(alpha = 0.3f),
            contentColor = if (selected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label)
    }
}