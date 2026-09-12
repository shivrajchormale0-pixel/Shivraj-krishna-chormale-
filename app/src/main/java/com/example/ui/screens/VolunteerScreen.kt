package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.VolunteerOfferEntity
import com.example.localization.AppStrings
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftBackground
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.TrustGreenContainer
import com.example.ui.viewmodel.MadatSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerScreen(
    viewModel: MadatSetuViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang by viewModel.currentLanguage.collectAsState()
    val offers by viewModel.allVolunteerOffers.collectAsState()

    var showOfferForm by remember { mutableStateOf(false) }
    var selectedDistrictFilter by remember { mutableStateOf<String?>(null) }

    val offerTypes = listOf(
        "Food",
        "Clothes",
        "Books",
        "Educational Materials",
        "Permitted Non-Cash Assistance"
    )

    var volName by remember { mutableStateOf("") }
    var volPhone by remember { mutableStateOf("") }
    var volCity by remember { mutableStateOf("") }
    var volDistrict by remember { mutableStateOf("") }
    var selectedOfferType by remember { mutableStateOf(offerTypes[0]) }
    var offerTypeDropdownExpanded by remember { mutableStateOf(false) }
    var volDetails by remember { mutableStateOf("") }

    val filteredOffers = if (selectedDistrictFilter == null) {
        offers
    } else {
        offers.filter { it.district.equals(selectedDistrictFilter, ignoreCase = true) || it.city.equals(selectedDistrictFilter, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Handshake, contentDescription = null, tint = PrimaryTeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Volunteer & Non-Cash Help", fontWeight = FontWeight.Bold)
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
            // Hero Explainer Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryTeal.copy(alpha = 0.08f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PrimaryTeal.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Community Non-Cash Giving",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PrimaryTeal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Offer cooked food, winter clothes, school textbooks, learning kits, or medical support directly to local families in your city or district.",
                            fontSize = 12.sp,
                            color = Color(0xFF334155),
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { showOfferForm = !showOfferForm },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("toggle_volunteer_offer_form")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (showOfferForm) "Hide Offer Form" else "Register Volunteer Offer")
                        }
                    }
                }
            }

            // Volunteer Offer Submission Form
            item {
                AnimatedVisibility(visible = showOfferForm) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SoftCardBorder, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Submit Non-Cash Assistance Offer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SecondaryNavy
                            )

                            OutlinedTextField(
                                value = volName,
                                onValueChange = { volName = it },
                                label = { Text("Your Name *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("vol_name_input"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = volPhone,
                                onValueChange = { volPhone = it },
                                label = { Text("Phone Number * (Masked publicly)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("vol_phone_input"),
                                singleLine = true
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = volCity,
                                    onValueChange = { volCity = it },
                                    label = { Text("City *") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("vol_city_input"),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = volDistrict,
                                    onValueChange = { volDistrict = it },
                                    label = { Text("District *") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("vol_district_input"),
                                    singleLine = true
                                )
                            }

                            // Assistance Type Dropdown
                            ExposedDropdownMenuBox(
                                expanded = offerTypeDropdownExpanded,
                                onExpandedChange = { offerTypeDropdownExpanded = !offerTypeDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedOfferType,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Assistance Category *") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = offerTypeDropdownExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .testTag("vol_type_dropdown")
                                )
                                ExposedDropdownMenu(
                                    expanded = offerTypeDropdownExpanded,
                                    onDismissRequest = { offerTypeDropdownExpanded = false }
                                ) {
                                    offerTypes.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type) },
                                            onClick = {
                                                selectedOfferType = type
                                                offerTypeDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = volDetails,
                                onValueChange = { volDetails = it },
                                label = { Text("Details of Offered Materials / Service *") },
                                placeholder = { Text("e.g. 50 kg rice packs, 30 warm sweaters, class 10 books...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("vol_details_input"),
                                minLines = 3
                            )

                            Button(
                                onClick = {
                                    if (volName.isBlank() || volCity.isBlank() || volDetails.isBlank()) {
                                        Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    viewModel.submitVolunteerOffer(
                                        name = volName.trim(),
                                        phone = volPhone.trim(),
                                        city = volCity.trim(),
                                        district = volDistrict.ifBlank { volCity }.trim(),
                                        offerType = selectedOfferType,
                                        details = volDetails.trim()
                                    ) {
                                        Toast.makeText(context, "Offer registered! Local coordinators can now connect.", Toast.LENGTH_LONG).show()
                                        volName = ""
                                        volPhone = ""
                                        volCity = ""
                                        volDistrict = ""
                                        volDetails = ""
                                        showOfferForm = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentSaffron),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_volunteer_button")
                            ) {
                                Text("Publish Volunteer Offer", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // District Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Opportunities (${filteredOffers.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            // List of Volunteer Offers
            items(filteredOffers, key = { it.id }) { offer ->
                VolunteerOfferCard(offer = offer)
            }
        }
    }
}

@Composable
fun VolunteerOfferCard(offer: VolunteerOfferEntity) {
    val (icon, tintColor) = when (offer.offerType) {
        "Food" -> Pair(Icons.Default.Restaurant, AccentSaffron)
        "Clothes" -> Pair(Icons.Default.Checkroom, PrimaryTeal)
        "Books", "Educational Materials" -> Pair(Icons.Default.School, SecondaryNavy)
        else -> Pair(Icons.Default.Handshake, TrustGreen)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
            .testTag("volunteer_card_${offer.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = tintColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(icon, contentDescription = null, tint = tintColor, modifier = Modifier.padding(6.dp).size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = offer.offerType,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Offered by ${offer.volunteerName}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Surface(
                    color = TrustGreenContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = offer.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TrustGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = offer.details,
                fontSize = 12.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${offer.city}, ${offer.district}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
