package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.CampaignCard
import com.example.ui.components.CategoryChip
import com.example.ui.components.LanguageAndRoleBar
import com.example.ui.components.UrgentBadge
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftBackground
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.UrgentRed
import com.example.ui.theme.UrgentRedContainer
import com.example.ui.viewmodel.MadatSetuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MadatSetuViewModel,
    onNavigateToDetails: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEmergency: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onDonateClick: (CampaignEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val campaigns by viewModel.filteredCampaigns.collectAsState()
    val emergencyCampaigns by viewModel.emergencyCampaigns.collectAsState()
    val stats by viewModel.platformStats.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val unreadNotifs = notifications.count { !it.isRead }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val verifiedOnly by viewModel.verifiedOnly.collectAsState()
    val emergencyOnly by viewModel.emergencyOnly.collectAsState()

    var showFilterPanel by remember { mutableStateOf(false) }

    val categories = listOf(
        "Medical Help",
        "Education",
        "Food & Essentials",
        "Elderly Support",
        "Disability Support",
        "Emergency Support",
        "Livelihood Support"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SoftBackground,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = AccentSaffron,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Request Help") },
                text = {
                    Text(
                        text = AppStrings.createHelpRequest(lang),
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier.testTag("fab_create_campaign")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Header: Branding & Notifications
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(SecondaryNavy, Color(0xFF0F2B48))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AccentSaffron),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Shield,
                                    contentDescription = "MadatSetu Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppStrings.appName(lang),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = AppStrings.slogan(lang),
                                    fontSize = 11.sp,
                                    color = Color(0xFFE2E8F0),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Notification Icon with Badge
                        IconButton(
                            onClick = onNavigateToNotifications,
                            modifier = Modifier.testTag("notifications_button")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifs > 0) {
                                        Badge(containerColor = UrgentRed) {
                                            Text(text = "$unreadNotifs", color = Color.White)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Language and Role Bar
            item {
                LanguageAndRoleBar(
                    currentLanguage = lang,
                    onLanguageChange = { viewModel.setLanguage(it) },
                    currentRole = currentRole,
                    onRoleChange = { viewModel.setRole(it) },
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Impact Statistics Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ImpactStatColumn(
                            title = AppStrings.totalHelped(lang),
                            value = "${stats.totalHelped}+",
                            highlightColor = PrimaryTeal
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(SoftCardBorder)
                        )
                        ImpactStatColumn(
                            title = AppStrings.totalRaised(lang),
                            value = formatInr(stats.totalRaised),
                            highlightColor = AccentSaffron
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(SoftCardBorder)
                        )
                        ImpactStatColumn(
                            title = AppStrings.totalVerified(lang),
                            value = "${stats.totalVerifiedCampaigns}",
                            highlightColor = TrustGreen
                        )
                    }
                }
            }

            // Search Bar & Filter Toggle
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.searchQuery.value = it },
                            placeholder = { Text(AppStrings.searchPlaceholder(lang), fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryTeal)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = PrimaryTeal,
                                unfocusedBorderColor = SoftCardBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (showFilterPanel || selectedCategory != null || emergencyOnly) PrimaryTeal else Color.White,
                            modifier = Modifier
                                .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
                                .clickable { showFilterPanel = !showFilterPanel }
                                .testTag("filter_toggle_button")
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = if (showFilterPanel || selectedCategory != null || emergencyOnly) Color.White else PrimaryTeal,
                                modifier = Modifier
                                    .padding(14.dp)
                                    .size(20.dp)
                            )
                        }
                    }

                    // Expandable Filter Panel
                    AnimatedVisibility(visible = showFilterPanel) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .border(1.dp, SoftCardBorder, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Advanced Search Filters",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = verifiedOnly,
                                        onClick = { viewModel.verifiedOnly.value = !verifiedOnly },
                                        label = { Text("Verified Only", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = TrustGreen.copy(alpha = 0.15f),
                                            selectedLabelColor = TrustGreen
                                        ),
                                        modifier = Modifier.testTag("filter_verified_only")
                                    )
                                    FilterChip(
                                        selected = emergencyOnly,
                                        onClick = { viewModel.emergencyOnly.value = !emergencyOnly },
                                        label = { Text("Emergency Only", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = UrgentRedContainer,
                                            selectedLabelColor = UrgentRed
                                        ),
                                        modifier = Modifier.testTag("filter_emergency_only")
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Category Horizontal Chips
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectedCategory.value = null },
                            label = { Text("All Causes / सर्व") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SecondaryNavy,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_all")
                        )
                    }
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.selectedCategory.value = if (isSelected) null else cat
                            },
                            label = { Text(AppStrings.getCategoryName(cat, lang)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryTeal,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_${cat.lowercase().replace(" ", "_")}")
                        )
                    }
                }
            }

            // Emergency Banner Section (if emergency campaigns exist)
            if (emergencyCampaigns.isNotEmpty() && !emergencyOnly) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(UrgentRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.emergencyHelp(lang),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = UrgentRed
                                )
                            }
                            Text(
                                text = "View All Urgent >",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = UrgentRed,
                                modifier = Modifier
                                    .clickable { onNavigateToEmergency() }
                                    .testTag("view_all_emergency_button")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val topEmergency = emergencyCampaigns.firstOrNull()
                        if (topEmergency != null) {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = UrgentRedContainer),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToDetails(topEmergency.id) }
                                    .border(1.dp, UrgentRed.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                    .testTag("emergency_featured_card")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "Urgent",
                                        tint = UrgentRed,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = topEmergency.titleEn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF991B1B),
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "Raised ${formatInr(topEmergency.amountRaised)} of ${formatInr(topEmergency.targetAmount)} • ${topEmergency.daysRemaining} days left",
                                            fontSize = 11.sp,
                                            color = Color(0xFFB91C1C)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Featured & Verified Campaigns Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (emergencyOnly) AppStrings.emergencyHelp(lang) else AppStrings.featuredCampaigns(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${campaigns.size} campaigns",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Campaign List
            if (campaigns.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.HealthAndSafety,
                            contentDescription = "No Campaigns",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No campaigns match the selected filters.",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(campaigns, key = { it.id }) { campaign ->
                    CampaignCard(
                        campaign = campaign,
                        lang = lang,
                        onDonateClick = { onDonateClick(campaign) },
                        onDetailsClick = { onNavigateToDetails(campaign.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ImpactStatColumn(title: String, value: String, highlightColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = highlightColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            color = Color(0xFF64748B),
            fontWeight = FontWeight.Medium
        )
    }
}
