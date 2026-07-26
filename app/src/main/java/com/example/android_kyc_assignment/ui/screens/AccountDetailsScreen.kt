package com.example.android_kyc_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android_kyc_assignment.data.model.Customer
import com.example.android_kyc_assignment.ui.theme.Green
import com.example.android_kyc_assignment.ui.theme.LightGreen
import com.example.android_kyc_assignment.ui.theme.LightOrange
import com.example.android_kyc_assignment.ui.theme.Orange
import com.example.android_kyc_assignment.ui.viewmodel.AccountDetailsUiState
import com.example.android_kyc_assignment.ui.viewmodel.AccountDetailsViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    customerId: Int,
    onBackClick: () -> Unit,
    onCaptureClick: (Int) -> Unit,
    viewModel: AccountDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(customerId) {
        viewModel.loadCustomerDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "Something went wrong",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadCustomerDetails() }) {
                            Text("Retry")
                        }
                    }
                }
            }

            uiState.customer != null -> {
                val customer = uiState.customer!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    ProfileHeader(customer = customer)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (customer.isVerified) {
                        // KYC Selfie preview section for verified customers
                        SectionTitle("KYC Selfie")
                        Spacer(modifier = Modifier.height(8.dp))
                        SelfiePreviewCard(customer = customer)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onCaptureClick(customer.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Re-take Selfie")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Button(
                            onClick = { onCaptureClick(customer.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Do KYC")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    SectionTitle("Personal Information")
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(Icons.Default.CalendarToday, "Date of Birth", customer.birthDate)
                    InfoRow(Icons.Default.Phone, "Phone", customer.phone)
                    InfoRow(Icons.Default.Email, "Email", customer.email)
                    InfoRow(Icons.Default.LocationOn, "Address", "${customer.address}, ${customer.city}, ${customer.state}")
                    InfoRow(Icons.Default.Public, "Nationality", customer.country)

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle("Account Information")
                    Spacer(modifier = Modifier.height(8.dp))
                    AccountInfoCard(customer = customer)

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle("Bank & Branch Details")
                    Spacer(modifier = Modifier.height(8.dp))
                    BankDetailsCard(
                        ifscCode = customer.ifscCode,
                        uiState = uiState,
                        onRetry = { viewModel.loadCustomerDetails() }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(customer: Customer) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Edge case: validate selfie file actually exists on disk before loading
        val selfieFileExists = remember(customer.selfiePath) {
            customer.selfiePath?.let { File(it).exists() } ?: false
        }

        val imageModel = if (selfieFileExists) {
            ImageRequest.Builder(LocalContext.current)
                .data(File(customer.selfiePath!!))
                .crossfade(true)
                .build()
        } else {
            customer.imageUrl
        }

        AsyncImage(
            model = imageModel,
            contentDescription = customer.fullName,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = customer.fullName,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        val badgeColor = if (customer.isVerified) Green else Orange
        val badgeBgColor = if (customer.isVerified) LightGreen else LightOrange
        val badgeText = if (customer.isVerified) "Verified" else "Pending KYC"

        Text(
            text = badgeText,
            color = badgeColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .background(badgeBgColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun SelfiePreviewCard(customer: Customer) {
    val context = LocalContext.current
    val selfieExists = remember(customer.selfiePath) {
        customer.selfiePath?.let { File(it).exists() } ?: false
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selfieExists) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(File(customer.selfiePath!!))
                        .crossfade(true)
                        .build(),
                    contentDescription = "KYC Selfie",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selfie file not found",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Captured via in-app camera (CameraX)",
                fontSize = 12.sp,
                color = Green,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "No system camera intent used",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Color.Black
    )
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun AccountInfoCard(customer: Customer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Account (IBAN)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = customer.maskedIban,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Card Type",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = customer.cardType,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Balance",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${customer.currency} ${String.format("%,.2f", customer.balance)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun BankDetailsCard(
    ifscCode: String,
    uiState: AccountDetailsUiState,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "IFSC: $ifscCode",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                uiState.isBankLoading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Loading bank details...", fontSize = 13.sp)
                    }
                }

                uiState.bankError != null -> {
                    Text(
                        text = uiState.bankError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                    TextButton(onClick = onRetry) {
                        Text("Retry")
                    }
                }

                uiState.bankDetails != null -> {
                    val bank = uiState.bankDetails
                    BankInfoRow("Bank", bank.bank)
                    BankInfoRow("Branch", bank.branch)
                    BankInfoRow("City", bank.city)
                    BankInfoRow("State", bank.state)
                }
            }
        }
    }
}

@Composable
private fun BankInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color.Black
        )
    }
}
