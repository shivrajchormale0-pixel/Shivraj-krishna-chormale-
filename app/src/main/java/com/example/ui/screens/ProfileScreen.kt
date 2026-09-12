package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DonationEntity
import com.example.data.model.UserRole
import com.example.localization.AppStrings
import com.example.ui.components.LanguageAndRoleBar
import com.example.ui.components.VerificationBadge
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftBackground
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.viewmodel.MadatSetuViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MadatSetuViewModel,
    onViewReceipt: (DonationEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val allDonations by viewModel.allDonations.collectAsState()
    val allCampaigns by viewModel.allCampaigns.collectAsState()
    val volunteerOffers by viewModel.allVolunteerOffers.collectAsState()

    val totalDonated = allDonations.sumOf { it.amount }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.profile(lang), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Role & Language Switcher
            item {
                LanguageAndRoleBar(
                    currentLanguage = lang,
                    onLanguageChange = { viewModel.setLanguage(it) },
                    currentRole = currentRole,
                    onRoleChange = { viewModel.setRole(it) }
                )
            }

            // User Identity Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PrimaryTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Citizen User (Verified)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Current Mode: ${currentRole.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryNavy
                            )
                            Text(
                                text = "Maharashtra / India • KYC Verified",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            // Privacy Guarantee Notice
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = TrustGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Privacy Shield: Your Aadhaar, PAN & Bank numbers are encrypted in admin vault and never displayed publicly.",
                            fontSize = 11.sp,
                            color = Color(0xFF166534),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Donations Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Contributions (${allDonations.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Total: " + formatInr(totalDonated),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = PrimaryTeal
                    )
                }
            }

            if (allDonations.isEmpty()) {
                item {
                    Text(
                        text = "No donations made yet. Help someone today to see receipts here.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                items(allDonations) { donation ->
                    DonationHistoryCard(donation = donation, onViewReceipt = { onViewReceipt(donation) })
                }
            }

            // My Submitted Campaigns Section
            item {
                Text(
                    text = "My Help Requests & Verification Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF0F172A)
                )
            }

            items(allCampaigns.take(2)) { campaign ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = campaign.titleEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.weight(1f)
                            )
                            VerificationBadge(status = campaign.verificationStatus, lang = lang)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target: ${formatInr(campaign.targetAmount)} • Raised: ${formatInr(campaign.amountRaised)}",
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Note: ${campaign.verificationNotes}",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DonationHistoryCard(
    donation: DonationEntity,
    onViewReceipt: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(donation.timestamp))
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
            .testTag("donation_history_${donation.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = donation.campaignTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$dateStr • ${donation.receiptNumber}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = formatInr(donation.amount),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryTeal
                )
            }

            OutlinedButton(
                onClick = onViewReceipt,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("view_receipt_${donation.id}")
            ) {
                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Receipt", fontSize = 11.sp)
            }
        }
    }
}
