package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.data.model.UserRole
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.UrgentRed

@Composable
fun LanguageAndRoleBar(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    currentRole: UserRole,
    onRoleChange: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }
    var roleMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Language Selector
        Box {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .border(1.dp, SoftCardBorder, RoundedCornerShape(20.dp))
                    .clickable { langMenuExpanded = true }
                    .testTag("language_selector_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Translate,
                        contentDescription = "Select Language",
                        tint = PrimaryTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentLanguage.nativeName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF0F172A)
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = langMenuExpanded,
                onDismissRequest = { langMenuExpanded = false }
            ) {
                AppLanguage.values().forEach { lang ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (lang == currentLanguage) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PrimaryTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = "${lang.nativeName} (${lang.displayName})",
                                    fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        onClick = {
                            onLanguageChange(lang)
                            langMenuExpanded = false
                        },
                        modifier = Modifier.testTag("lang_option_${lang.code}")
                    )
                }
            }
        }

        // Role Switcher
        Box {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SecondaryNavy.copy(alpha = 0.08f),
                modifier = Modifier
                    .clickable { roleMenuExpanded = true }
                    .testTag("role_selector_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Role",
                        tint = SecondaryNavy,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Role: ${currentRole.title}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = SecondaryNavy
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = SecondaryNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = roleMenuExpanded,
                onDismissRequest = { roleMenuExpanded = false }
            ) {
                UserRole.values().forEach { role ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (role == currentRole) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = SecondaryNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = role.title,
                                    fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        onClick = {
                            onRoleChange(role)
                            roleMenuExpanded = false
                        },
                        modifier = Modifier.testTag("role_option_${role.name.lowercase()}")
                    )
                }
            }
        }
    }
}

@Composable
fun DonationModalDialog(
    campaign: CampaignEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onConfirmDonation: (amount: Double, donorName: String, isAnonymous: Boolean) -> Unit
) {
    var selectedPreset by remember { mutableStateOf(100.0) }
    var customAmountText by remember { mutableStateOf("") }
    var isCustomSelected by remember { mutableStateOf(false) }
    var donorName by remember { mutableStateOf("Kind Citizen") }
    var isAnonymous by remember { mutableStateOf(false) }
    var selectedPaymentApp by remember { mutableStateOf("Google Pay UPI") }

    val currentAmount = if (isCustomSelected) {
        customAmountText.toDoubleOrNull() ?: 0.0
    } else {
        selectedPreset
    }

    val presetOptions = listOf(10.0, 50.0, 100.0, 500.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (currentAmount >= 1.0) {
                        onConfirmDonation(currentAmount, donorName, isAnonymous)
                    }
                },
                enabled = currentAmount >= 1.0,
                colors = ButtonDefaults.buttonColors(containerColor = AccentSaffron),
                modifier = Modifier.testTag("confirm_donate_button")
            ) {
                Text(
                    text = "Pay " + formatInr(currentAmount),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_donate_button")
            ) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        },
        title = {
            Column {
                Text(
                    text = when (lang) {
                        AppLanguage.ENGLISH -> "Make a Contribution"
                        AppLanguage.MARATHI -> "मदतीचा हात द्या"
                        AppLanguage.HINDI -> "सहयोग राशि प्रदान करें"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = campaign.titleEn,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select Amount:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Presets: ₹10, ₹50, ₹100, ₹500
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetOptions.forEach { amt ->
                        val isSelected = !isCustomSelected && selectedPreset == amt
                        Surface(
                            color = if (isSelected) PrimaryTeal else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedPreset = amt
                                    isCustomSelected = false
                                }
                                .testTag("preset_amt_${amt.toInt()}")
                        ) {
                            Text(
                                text = "₹${amt.toInt()}",
                                color = if (isSelected) Color.White else Color(0xFF0F172A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Custom Amount input
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = {
                        customAmountText = it.filter { char -> char.isDigit() }
                        isCustomSelected = true
                    },
                    label = { Text("Custom Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_amount_input"),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.CurrencyRupee, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Donor Name
                OutlinedTextField(
                    value = donorName,
                    onValueChange = { donorName = it },
                    label = { Text("Donor Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donor_name_input"),
                    singleLine = true,
                    enabled = !isAnonymous
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Anonymous Donation toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isAnonymous = !isAnonymous },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAnonymous,
                        onCheckedChange = { isAnonymous = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryTeal),
                        modifier = Modifier.testTag("anonymous_checkbox")
                    )
                    Text(
                        text = "Donate Anonymously (Hide name on donor list)",
                        fontSize = 12.sp,
                        color = Color(0xFF475569)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Indian UPI / Payment Gateway Selection
                Text(
                    text = "Payment Gateway / UPI:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(4.dp))

                val upiApps = listOf("Google Pay UPI", "PhonePe UPI", "BHIM / Paytm", "Bank NetBanking")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    upiApps.take(3).forEach { app ->
                        val isSelected = selectedPaymentApp == app
                        Surface(
                            color = if (isSelected) SecondaryNavy.copy(alpha = 0.12f) else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SecondaryNavy else Color(0xFFCBD5E1)
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedPaymentApp = app }
                        ) {
                            Text(
                                text = app.replace(" UPI", ""),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SecondaryNavy else Color(0xFF64748B),
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Escrow Security Notice
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = "Secure",
                        tint = TrustGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MadatSetu 256-bit Encrypted Escrow. Raw card credentials are never stored.",
                        fontSize = 10.sp,
                        color = Color(0xFF475569),
                        lineHeight = 13.sp
                    )
                }
            }
        }
    )
}

@Composable
fun ReportModalDialog(
    campaign: CampaignEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitReport: (reason: String, details: String, reporterName: String) -> Unit
) {
    val reportReasons = listOf(
        "Fraud",
        "Fake documents",
        "Misleading information",
        "Duplicate campaign",
        "Abuse"
    )
    var selectedReason by remember { mutableStateOf(reportReasons[0]) }
    var detailsText by remember { mutableStateOf("") }
    var reporterName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (detailsText.isNotBlank()) {
                        onSubmitReport(
                            selectedReason,
                            detailsText,
                            reporterName.ifBlank { "Concerned Citizen" }
                        )
                    }
                },
                enabled = detailsText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = UrgentRed),
                modifier = Modifier.testTag("submit_report_button")
            ) {
                Text("Submit Report", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_report_button")
            ) {
                Text("Cancel")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Report,
                    contentDescription = "Report",
                    tint = UrgentRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Report Campaign", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Help maintain community trust. Select violation reason:",
                    fontSize = 12.sp,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(8.dp))

                reportReasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = reason }
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(selectedColor = UrgentRed),
                            modifier = Modifier.testTag("reason_radio_${reason.lowercase().replace(" ", "_")}")
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = reason, fontSize = 13.sp, color = Color(0xFF1E293B))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = detailsText,
                    onValueChange = { detailsText = it },
                    label = { Text("Details / Evidence Summary") },
                    placeholder = { Text("Please explain the issue or invalid documents...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("report_details_input"),
                    minLines = 3,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reporterName,
                    onValueChange = { reporterName = it },
                    label = { Text("Your Name or Phone (Kept strictly private)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reporter_contact_input"),
                    singleLine = true
                )
            }
        }
    )
}
