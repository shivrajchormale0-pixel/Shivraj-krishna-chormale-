package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.CampaignEntity
import com.example.data.model.DonationEntity
import com.example.data.model.UserRole
import com.example.localization.AppStrings
import com.example.ui.components.DonationModalDialog
import com.example.ui.components.DonationReceiptDialog
import com.example.ui.components.ReportModalDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CampaignDetailScreen
import com.example.ui.screens.CreateCampaignScreen
import com.example.ui.screens.EmergencyScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VolunteerScreen
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.UrgentRed
import com.example.ui.viewmodel.MadatSetuViewModel

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Emergency : Screen("emergency", "Emergency")
    object Volunteer : Screen("volunteer", "Volunteer")
    object Admin : Screen("admin", "Admin")
    object Profile : Screen("profile", "Profile")
    object Create : Screen("create", "Request Help")
    object Notifications : Screen("notifications", "Notifications")
    object Details : Screen("details/{campaignId}", "Details") {
        fun createRoute(campaignId: Long) = "details/$campaignId"
    }
}

@Composable
fun MainAppNavigation(viewModel: MadatSetuViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val lang by viewModel.currentLanguage.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val lastReceipt by viewModel.lastDonationReceipt.collectAsState()

    var activeDonationCampaign by remember { mutableStateOf<CampaignEntity?>(null) }
    var activeReportCampaign by remember { mutableStateOf<CampaignEntity?>(null) }
    var viewReceiptEntity by remember { mutableStateOf<DonationEntity?>(null) }

    val bottomNavItems = listOf(
        Triple(Screen.Home.route, AppStrings.home(lang), Icons.Default.Home),
        Triple(Screen.Emergency.route, AppStrings.emergencyHelp(lang), Icons.Default.Emergency),
        Triple(Screen.Volunteer.route, AppStrings.volunteer(lang), Icons.Default.Handshake),
        Triple(Screen.Admin.route, "Admin", Icons.Default.AdminPanelSettings),
        Triple(Screen.Profile.route, AppStrings.profile(lang), Icons.Default.Person)
    )

    val showBottomBar = currentDestination in listOf(
        Screen.Home.route,
        Screen.Emergency.route,
        Screen.Volunteer.route,
        Screen.Admin.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = PrimaryTeal
                ) {
                    bottomNavItems.forEach { (route, label, icon) ->
                        val isSelected = currentDestination == route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentDestination != route) {
                                    navController.navigate(route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) {
                                        if (route == Screen.Emergency.route) UrgentRed else PrimaryTeal
                                    } else Color(0xFF64748B)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    color = if (isSelected) {
                                        if (route == Screen.Emergency.route) UrgentRed else PrimaryTeal
                                    } else Color(0xFF64748B)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (route == Screen.Emergency.route) UrgentRed.copy(alpha = 0.12f)
                                else PrimaryTeal.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.testTag("nav_${route}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDetails = { campaignId ->
                        navController.navigate(Screen.Details.createRoute(campaignId))
                    },
                    onNavigateToCreate = {
                        navController.navigate(Screen.Create.route)
                    },
                    onNavigateToEmergency = {
                        navController.navigate(Screen.Emergency.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications.route)
                    },
                    onDonateClick = { campaign ->
                        activeDonationCampaign = campaign
                    }
                )
            }

            composable(Screen.Emergency.route) {
                EmergencyScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onDonateClick = { campaign -> activeDonationCampaign = campaign },
                    onDetailsClick = { id -> navController.navigate(Screen.Details.createRoute(id)) }
                )
            }

            composable(Screen.Volunteer.route) {
                VolunteerScreen(viewModel = viewModel)
            }

            composable(Screen.Admin.route) {
                AdminScreen(viewModel = viewModel)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onViewReceipt = { donation -> viewReceiptEntity = donation }
                )
            }

            composable(Screen.Create.route) {
                CreateCampaignScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Details.route,
                arguments = listOf(navArgument("campaignId") { type = NavType.LongType })
            ) { backStackEntry ->
                val campaignId = backStackEntry.arguments?.getLong("campaignId") ?: 0L
                CampaignDetailScreen(
                    campaignId = campaignId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onDonateClick = { campaign -> activeDonationCampaign = campaign },
                    onReportClick = { campaign -> activeReportCampaign = campaign },
                    onVolunteerClick = {
                        navController.navigate(Screen.Volunteer.route)
                    }
                )
            }
        }

        // Global Donation Modal
        if (activeDonationCampaign != null) {
            val camp = activeDonationCampaign!!
            DonationModalDialog(
                campaign = camp,
                lang = lang,
                onDismiss = { activeDonationCampaign = null },
                onConfirmDonation = { amount, donorName, isAnon ->
                    activeDonationCampaign = null
                    viewModel.makeDonation(
                        campaignId = camp.id,
                        campaignTitle = camp.titleEn,
                        amount = amount,
                        donorName = donorName,
                        isAnonymous = isAnon
                    )
                }
            )
        }

        // Global Report Modal
        if (activeReportCampaign != null) {
            val camp = activeReportCampaign!!
            ReportModalDialog(
                campaign = camp,
                lang = lang,
                onDismiss = { activeReportCampaign = null },
                onSubmitReport = { reason, details, reporterName ->
                    activeReportCampaign = null
                    viewModel.reportCampaign(
                        campaignId = camp.id,
                        campaignTitle = camp.titleEn,
                        reporterName = reporterName,
                        reason = reason,
                        details = details
                    ) {}
                }
            )
        }

        // Global Receipt Dialog after successful donation
        if (lastReceipt != null) {
            DonationReceiptDialog(
                donation = lastReceipt!!,
                lang = lang,
                onDismiss = { viewModel.clearDonationReceipt() }
            )
        }

        // Profile View Receipt Dialog
        if (viewReceiptEntity != null) {
            DonationReceiptDialog(
                donation = viewReceiptEntity!!,
                lang = lang,
                onDismiss = { viewReceiptEntity = null }
            )
        }
    }
}
