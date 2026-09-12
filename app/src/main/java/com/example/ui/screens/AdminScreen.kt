package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.data.model.DonationEntity
import com.example.data.model.ReportEntity
import com.example.localization.AppLanguage
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
fun AdminScreen(
    viewModel: MadatSetuViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang by viewModel.currentLanguage.collectAsState()
    val allCampaigns by viewModel.allCampaigns.collectAsState()
    val donations by viewModel.allDonations.collectAsState()
    val reports by viewModel.allReports.collectAsState()
    val auditLogs by viewModel.allAuditLogs.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Pending Review", "Active & Suspended", "Reports / Fraud", "Donations & Refunds", "Audit Logs")

    val pendingCampaigns = allCampaigns.filter { it.verificationStatus == "UNDER_REVIEW" }
    val activeCampaigns = allCampaigns.filter { it.verificationStatus != "UNDER_REVIEW" }
    val pendingReports = reports.filter { it.status == "PENDING" }

    // Dialog state for verification notes
    var verifyingCampaign by remember { mutableStateOf<CampaignEntity?>(null) }
    var adminVerificationNotes by remember { mutableStateOf("Hospital and identity documents verified genuine.") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SecondaryNavy)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Admin Control Console", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // High level metrics row
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AdminMetricItem("Pending Review", "${pendingCampaigns.size}", Color(0xFFB45309))
                    AdminMetricItem("Active", "${allCampaigns.count { it.verificationStatus == "VERIFIED" }}", TrustGreen)
                    AdminMetricItem("Reports", "${pendingReports.size}", UrgentRed)
                    AdminMetricItem("Donations", "${donations.size}", PrimaryTeal)
                }
            }

            // Scrollable Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = PrimaryTeal,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = PrimaryTeal
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        modifier = Modifier.testTag("admin_tab_$index")
                    )
                }
            }

            // Tab Content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTabIndex) {
                    // TAB 0: Pending Review
                    0 -> {
                        if (pendingCampaigns.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No pending campaigns under review.", color = Color(0xFF64748B))
                                }
                            }
                        } else {
                            items(pendingCampaigns, key = { it.id }) { campaign ->
                                PendingCampaignAdminCard(
                                    campaign = campaign,
                                    lang = lang,
                                    onVerifyClick = { verifyingCampaign = campaign },
                                    onRejectClick = {
                                        viewModel.rejectCampaign(campaign.id, "Documents insufficient / invalid")
                                        Toast.makeText(context, "Campaign Rejected", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    // TAB 1: Active & Suspended
                    1 -> {
                        items(activeCampaigns, key = { it.id }) { campaign ->
                            ActiveCampaignAdminCard(
                                campaign = campaign,
                                lang = lang,
                                onSuspend = {
                                    viewModel.suspendCampaign(campaign.id, "Suspended for safety audit")
                                    Toast.makeText(context, "Campaign Suspended", Toast.LENGTH_SHORT).show()
                                },
                                onReVerify = {
                                    viewModel.verifyCampaign(campaign.id, "Re-verified and cleared")
                                    Toast.makeText(context, "Campaign Re-verified", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    // TAB 2: Reports / Fraud
                    2 -> {
                        if (reports.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                    Text("No fraud or violation reports lodged.", color = Color(0xFF64748B))
                                }
                            }
                        } else {
                            items(reports, key = { it.id }) { report ->
                                ReportAdminCard(
                                    report = report,
                                    onSuspendCampaign = {
                                        viewModel.suspendCampaign(report.campaignId, "Suspended due to user report #${report.id}")
                                        Toast.makeText(context, "Campaign Suspended", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    // TAB 3: Donations & Refunds
                    3 -> {
                        items(donations, key = { it.id }) { donation ->
                            DonationAdminCard(
                                donation = donation,
                                onRefund = {
                                    viewModel.processRefund(donation.id, "Refund processed by admin request")
                                    Toast.makeText(context, "Refund Processed", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    // TAB 4: Audit Logs
                    4 -> {
                        items(auditLogs, key = { it.id }) { log ->
                            AuditLogItemCard(log = log)
                        }
                    }
                }
            }
        }
    }

    // Dialog for Admin Verification confirmation
    if (verifyingCampaign != null) {
        val target = verifyingCampaign!!
        AlertDialog(
            onDismissRequest = { verifyingCampaign = null },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.verifyCampaign(target.id, adminVerificationNotes)
                        verifyingCampaign = null
                        Toast.makeText(context, "Campaign Verified & Activated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TrustGreen),
                    modifier = Modifier.testTag("confirm_verify_button")
                ) {
                    Text("Approve & Make Active", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { verifyingCampaign = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Approve & Verify Campaign", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Campaign: ${target.titleEn}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Target: ${formatInr(target.targetAmount)} • Beneficiary: ${target.beneficiaryName}",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = adminVerificationNotes,
                        onValueChange = { adminVerificationNotes = it },
                        label = { Text("Verification Notes (Shown to donors)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }
        )
    }
}

@Composable
fun AdminMetricItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = title, fontSize = 10.sp, color = Color(0xFF64748B))
    }
}

@Composable
fun PendingCampaignAdminCard(
    campaign: CampaignEntity,
    lang: AppLanguage,
    onVerifyClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
            .testTag("admin_pending_card_${campaign.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerificationBadge(status = campaign.verificationStatus, lang = lang)
                Text(
                    text = "ID #${campaign.id}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = campaign.titleEn,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Beneficiary: ${campaign.beneficiaryName} • Contact: ${campaign.contactPhone}",
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )
            Text(
                text = "Location: ${campaign.city}, ${campaign.district} • Target: ${formatInr(campaign.targetAmount)}",
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )
            Text(
                text = "Submitted Documents: ${campaign.documentProofSummary}",
                fontSize = 11.sp,
                color = SecondaryNavy,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f).testTag("admin_reject_${campaign.id}"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = UrgentRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject", color = UrgentRed, fontSize = 12.sp)
                }

                Button(
                    onClick = onVerifyClick,
                    colors = ButtonDefaults.buttonColors(containerColor = TrustGreen),
                    modifier = Modifier.weight(1f).testTag("admin_verify_${campaign.id}"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Verify & Approve", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ActiveCampaignAdminCard(
    campaign: CampaignEntity,
    lang: AppLanguage,
    onSuspend: () -> Unit,
    onReVerify: () -> Unit
) {
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
                    text = campaign.titleEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.weight(1f)
                )
                VerificationBadge(status = campaign.verificationStatus, lang = lang)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Raised: ${formatInr(campaign.amountRaised)} / ${formatInr(campaign.targetAmount)} • Donors: ${campaign.donorCount}",
                fontSize = 11.sp,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (campaign.verificationStatus != "SUSPENDED") {
                    OutlinedButton(
                        onClick = onSuspend,
                        modifier = Modifier.testTag("admin_suspend_${campaign.id}")
                    ) {
                        Text("Suspend Campaign", color = UrgentRed, fontSize = 11.sp)
                    }
                } else {
                    Button(
                        onClick = onReVerify,
                        colors = ButtonDefaults.buttonColors(containerColor = TrustGreen)
                    ) {
                        Text("Clear & Re-verify", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportAdminCard(
    report: ReportEntity,
    onSuspendCampaign: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH).format(Date(report.timestamp))
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, UrgentRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Report, contentDescription = null, tint = UrgentRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Report: ${report.reportReason}",
                        fontWeight = FontWeight.Bold,
                        color = UrgentRed,
                        fontSize = 13.sp
                    )
                }
                Text(text = dateStr, fontSize = 10.sp, color = Color(0xFF64748B))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Target Campaign: ${report.campaignTitle} (ID: ${report.campaignId})",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Evidence: ${report.details}",
                fontSize = 12.sp,
                color = Color(0xFF334155)
            )
            Text(
                text = "Reporter: ${report.reporterName}",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onSuspendCampaign,
                colors = ButtonDefaults.buttonColors(containerColor = UrgentRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_suspend_from_report_${report.id}")
            ) {
                Text("Suspend Campaign Immediately", fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun DonationAdminCard(
    donation: DonationEntity,
    onRefund: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Date(donation.timestamp))
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
                    text = donation.receiptNumber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = SecondaryNavy
                )
                Text(
                    text = formatInr(donation.amount),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = PrimaryTeal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${donation.campaignTitle} • Donor: ${if (donation.isAnonymous) "Anonymous" else donation.donorName}",
                fontSize = 11.sp,
                color = Color(0xFF475569)
            )
            Text(
                text = "Txn: ${donation.transactionId} • ${donation.paymentMethod} • $dateStr",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )

            if (donation.status != "REFUNDED") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onRefund,
                    modifier = Modifier.testTag("admin_refund_${donation.id}")
                ) {
                    Text("Process Security Refund", color = UrgentRed, fontSize = 11.sp)
                }
            } else {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "STATUS: REFUNDED TO SOURCE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = UrgentRed
                )
            }
        }
    }
}

@Composable
fun AuditLogItemCard(log: com.example.data.model.AuditLogEntity) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Date(log.timestamp))
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftCardBorder, RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.History, contentDescription = null, tint = SecondaryNavy, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = log.action, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SecondaryNavy)
                    Text(text = dateStr, fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = log.details, fontSize = 11.sp, color = Color(0xFF334155))
                Text(text = "By: ${log.performedBy}", fontSize = 10.sp, color = Color(0xFF64748B))
            }
        }
    }
}
