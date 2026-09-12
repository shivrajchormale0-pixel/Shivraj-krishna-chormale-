package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.components.CategoryChip
import com.example.ui.components.UrgentBadge
import com.example.ui.components.VerificationBadge
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftBackground
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.TrustGreenContainer
import com.example.ui.theme.UrgentRed
import com.example.ui.theme.UrgentRedContainer
import com.example.ui.viewmodel.MadatSetuViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignDetailScreen(
    campaignId: Long,
    viewModel: MadatSetuViewModel,
    onBack: () -> Unit,
    onDonateClick: (CampaignEntity) -> Unit,
    onReportClick: (CampaignEntity) -> Unit,
    onVolunteerClick: (CampaignEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val campaigns by viewModel.allCampaigns.collectAsState()
    val updates by viewModel.allUpdates.collectAsState()

    val campaign = campaigns.firstOrNull { it.id == campaignId }
    val campaignUpdates = updates.filter { it.campaignId == campaignId }

    if (campaign == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Campaign not found.")
        }
        return
    }

    val title = when (lang) {
        AppLanguage.MARATHI -> campaign.titleMr.ifBlank { campaign.titleEn }
        AppLanguage.HINDI -> campaign.titleHi.ifBlank { campaign.titleEn }
        else -> campaign.titleEn
    }
    val fullProblem = when (lang) {
        AppLanguage.MARATHI -> campaign.fullProblemMr.ifBlank { campaign.fullProblemEn }
        AppLanguage.HINDI -> campaign.fullProblemHi.ifBlank { campaign.fullProblemEn }
        else -> campaign.fullProblemEn
    }

    val progress = if (campaign.targetAmount > 0) {
        (campaign.amountRaised / campaign.targetAmount).coerceIn(0.0, 1.0).toFloat()
    } else 0f
    val percentInt = (progress * 100).toInt()
    val remainingBalance = (campaign.amountRaised - campaign.amountUtilized).coerceAtLeast(0.0)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = { Text("Campaign Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onReportClick(campaign) },
                        modifier = Modifier.testTag("report_icon_button")
                    ) {
                        Icon(Icons.Default.Report, contentDescription = "Report", tint = UrgentRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF0F172A)
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SoftCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onVolunteerClick(campaign) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("volunteer_help_button")
                    ) {
                        Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.volunteer(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onDonateClick(campaign) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (campaign.isEmergency) UrgentRed else AccentSaffron
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("donate_now_detail_button")
                    ) {
                        Text(
                            text = AppStrings.donateNow(lang),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hero Banner Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        if (campaign.isEmergency) listOf(UrgentRed, AccentSaffron)
                                        else listOf(SecondaryNavy, PrimaryTeal)
                                    )
                                )
                                .padding(14.dp)
                        ) {
                            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    CategoryChip(category = campaign.category, lang = lang)
                                    if (campaign.isEmergency) UrgentBadge(lang = lang)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "📍 ${campaign.city}, ${campaign.district} • Due ${campaign.targetDate}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VerificationBadge(status = campaign.verificationStatus, lang = lang)
                                Text(
                                    text = "${campaign.daysRemaining} days left",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Funding Progress
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = if (campaign.isEmergency) UrgentRed else PrimaryTeal,
                                trackColor = Color(0xFFE2E8F0)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = formatInr(campaign.amountRaised),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = PrimaryTeal
                                    )
                                    Text(
                                        text = "Target: " + formatInr(campaign.targetAmount),
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$percentInt%",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "${campaign.donorCount} Donors",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Trust & Privacy Safeguard Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(14.dp))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = "Security",
                            tint = TrustGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "MadatSetu Security & Identity Shield",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF166534)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppStrings.privacyNotice(lang),
                                fontSize = 11.sp,
                                color = Color(0xFF15803D),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Verified Payout: ${campaign.bankOrUpiMasked}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }
            }

            // Detailed Story
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = PrimaryTeal)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Story & Purpose of Need",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = fullProblem,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF334155),
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SoftCardBorder)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Beneficiary: ${campaign.beneficiaryName}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryNavy
                        )
                        Text(
                            text = "Supporting Verification: ${campaign.documentProofSummary}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Transparency Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = SecondaryNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.transparencyTitle(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TransparencyRow("Total Target Required", formatInr(campaign.targetAmount))
                        TransparencyRow("Total Funds Collected", formatInr(campaign.amountRaised), isHighlight = true)
                        TransparencyRow("Verified Donors", "${campaign.donorCount} contributors")
                        TransparencyRow("Amount Utilized & Disbursed", formatInr(campaign.amountUtilized))
                        TransparencyRow("Current Escrow Balance", formatInr(remainingBalance), isAccent = true)

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "Admin Field Verification: ${campaign.verificationNotes}",
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }

            // Campaign Updates & Proof Section
            item {
                Text(
                    text = "Campaign Updates & Expense Proofs (${campaignUpdates.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF0F172A)
                )
            }

            if (campaignUpdates.isEmpty()) {
                item {
                    Text(
                        text = "No updates posted yet. Updates will appear here as funds are utilized.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                items(campaignUpdates) { update ->
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
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = update.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(update.timestamp)),
                                    fontSize = 10.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = update.message,
                                fontSize = 12.sp,
                                color = Color(0xFF475569),
                                lineHeight = 18.sp
                            )
                            if (update.expenseUtilized > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(TrustGreenContainer.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TrustGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Disbursement: ${formatInr(update.expenseUtilized)} (${update.proofReference})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF166534)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransparencyRow(label: String, value: String, isHighlight: Boolean = false, isAccent: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                isHighlight -> PrimaryTeal
                isAccent -> AccentSaffron
                else -> Color(0xFF0F172A)
            }
        )
    }
}
