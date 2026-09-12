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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftBackground
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.UrgentRed
import com.example.ui.viewmodel.MadatSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCampaignScreen(
    viewModel: MadatSetuViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang by viewModel.currentLanguage.collectAsState()

    val categories = listOf(
        "Medical Help",
        "Education",
        "Food & Essentials",
        "Elderly Support",
        "Disability Support",
        "Emergency Support",
        "Livelihood Support"
    )

    var campaignTitle by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var beneficiaryName by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var targetAmountText by remember { mutableStateOf("") }
    var targetDate by remember { mutableStateOf("30 Days (Standard)") }
    var isEmergency by remember { mutableStateOf(false) }
    var detailedProblem by remember { mutableStateOf("") }
    var bankOrUpi by remember { mutableStateOf("") }
    var documentProofNote by remember { mutableStateOf("Hospital/School certificate & Government Photo ID ready for verification") }
    var consentAgreed by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppStrings.createHelpRequest(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button_create")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF0F172A)
                )
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
            // Trust & Mandatory Verification Guidance
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SecondaryNavy.copy(alpha = 0.06f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SecondaryNavy.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = SecondaryNavy, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Zero Fraud Guarantee: Mandatory Admin Verification",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SecondaryNavy
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "All requests are submitted with status 'Under Review'. Our regional vigilance coordinators verify hospital bills, college challans, or identity records before public activation.",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Error banner
            if (errorMessage != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = UrgentRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = errorMessage ?: "", color = UrgentRed, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Section 1: Campaign Details
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "1. Help Request Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryTeal
                        )

                        OutlinedTextField(
                            value = campaignTitle,
                            onValueChange = { campaignTitle = it },
                            label = { Text("Campaign Title *") },
                            placeholder = { Text("e.g., Medical treatment for Aarav in Pune") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_title_input"),
                            singleLine = true
                        )

                        // Category Dropdown
                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = AppStrings.getCategoryName(selectedCategory, lang),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("create_category_input")
                            )
                            ExposedDropdownMenu(
                                expanded = categoryDropdownExpanded,
                                onDismissRequest = { categoryDropdownExpanded = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(AppStrings.getCategoryName(cat, lang)) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Emergency Flag Checkbox
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isEmergency = !isEmergency }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isEmergency,
                                onCheckedChange = { isEmergency = it },
                                colors = CheckboxDefaults.colors(checkedColor = UrgentRed),
                                modifier = Modifier.testTag("emergency_checkbox")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Emergency Need (Life-threatening surgery or disaster)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isEmergency) UrgentRed else Color(0xFF334155)
                            )
                        }

                        OutlinedTextField(
                            value = targetAmountText,
                            onValueChange = { targetAmountText = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Required Target Amount (₹) *") },
                            placeholder = { Text("e.g., 150000") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null, modifier = Modifier.size(18.dp)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_target_amount_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = detailedProblem,
                            onValueChange = { detailedProblem = it },
                            label = { Text("Detailed Problem & Reason for Help *") },
                            placeholder = { Text("Describe the medical condition, education obstacle, or family situation clearly...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_description_input"),
                            minLines = 4,
                            maxLines = 6
                        )
                    }
                }
            }

            // Section 2: Beneficiary & Location
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "2. Beneficiary & Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryTeal
                        )

                        OutlinedTextField(
                            value = beneficiaryName,
                            onValueChange = { beneficiaryName = it },
                            label = { Text("Beneficiary Full Name *") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_beneficiary_name_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = contactInfo,
                            onValueChange = { contactInfo = it },
                            label = { Text("Contact Phone Number * (Kept Secure)") },
                            placeholder = { Text("10-digit mobile number for verifier call") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_phone_input"),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City / Town *") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("create_city_input"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = district,
                                onValueChange = { district = it },
                                label = { Text("District *") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("create_district_input"),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Section 3: Supporting Documents & Bank/UPI
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "3. Supporting Documents & Payout Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryTeal
                        )

                        OutlinedTextField(
                            value = documentProofNote,
                            onValueChange = { documentProofNote = it },
                            label = { Text("Supporting Document Details *") },
                            placeholder = { Text("Attach hospital bill, doctor prescription, or college fee invoice details") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_documents_input"),
                            minLines = 2
                        )

                        // Bank / UPI details
                        OutlinedTextField(
                            value = bankOrUpi,
                            onValueChange = { bankOrUpi = it },
                            label = { Text("Bank Account or UPI ID for Direct Payout *") },
                            placeholder = { Text("e.g., Hospital A/C or UPI ID: name@okhdfcbank") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("create_payout_input"),
                            singleLine = true
                        )

                        // Privacy Warning Box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Privacy Protocol: Sensitive bank accounts, Aadhaar, PAN, and UPI credentials are encrypted in an administrative escrow vault and never revealed to the general public.",
                                fontSize = 10.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            // Consent Agreement
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { consentAgreed = !consentAgreed },
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = consentAgreed,
                        onCheckedChange = { consentAgreed = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryTeal),
                        modifier = Modifier.testTag("consent_checkbox")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "I consent to the collection and verification of my documents by authorized MadatSetu field officers. I declare that all information provided is accurate and genuine.",
                        fontSize = 11.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        val amount = targetAmountText.toDoubleOrNull() ?: 0.0
                        if (campaignTitle.isBlank() || beneficiaryName.isBlank() || city.isBlank() || amount <= 0) {
                            errorMessage = "Please fill in all mandatory fields (Title, Name, City, Target Amount)."
                            return@Button
                        }
                        if (!consentAgreed) {
                            errorMessage = "Please agree to the verification consent."
                            return@Button
                        }
                        errorMessage = null
                        isSubmitting = true
                        viewModel.submitHelpRequest(
                            title = campaignTitle.trim(),
                            category = selectedCategory,
                            shortDesc = detailedProblem.take(120),
                            fullProblem = detailedProblem,
                            beneficiaryName = beneficiaryName.trim(),
                            contactPhone = contactInfo.trim(),
                            city = city.trim(),
                            district = district.ifBlank { city }.trim(),
                            targetAmount = amount,
                            targetDate = "30 Days",
                            isEmergency = isEmergency,
                            bankUpi = bankOrUpi.ifBlank { "Direct Verified Escrow" },
                            documentProof = documentProofNote
                        ) { success, msg ->
                            isSubmitting = false
                            if (success) {
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                onSuccess()
                            } else {
                                errorMessage = msg
                            }
                        }
                    },
                    enabled = !isSubmitting && consentAgreed,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentSaffron),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_campaign_button")
                ) {
                    Text(
                        text = if (isSubmitting) "Submitting for Verification..." else "Submit for Admin Review",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
