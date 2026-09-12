package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MadatSetuDatabase
import com.example.data.model.AuditLogEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.CampaignUpdateEntity
import com.example.data.model.DonationEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReportEntity
import com.example.data.model.UserRole
import com.example.data.model.VolunteerOfferEntity
import com.example.data.repository.MadatSetuRepository
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CampaignFilterCriteria(
    val query: String = "",
    val category: String? = null,
    val district: String? = null,
    val verifiedOnly: Boolean = true,
    val emergencyOnly: Boolean = false
)

data class PlatformStats(
    val totalHelped: Int = 0,
    val totalRaised: Double = 0.0,
    val totalVerifiedCampaigns: Int = 0
)

class MadatSetuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MadatSetuRepository

    init {
        val db = MadatSetuDatabase.getDatabase(application)
        repository = MadatSetuRepository(db)
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Language state
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    // User Role state (Donor, Beneficiary, Volunteer, Admin)
    private val _currentRole = MutableStateFlow(UserRole.DONOR)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    // Filter states
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)
    val selectedDistrict = MutableStateFlow<String?>(null)
    val verifiedOnly = MutableStateFlow(true)
    val emergencyOnly = MutableStateFlow(false)
    val maxTargetAmount = MutableStateFlow<Double?>(null)

    // Base flows
    val allCampaigns: StateFlow<List<CampaignEntity>> = repository.allCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val verifiedCampaigns: StateFlow<List<CampaignEntity>> = repository.verifiedCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emergencyCampaigns: StateFlow<List<CampaignEntity>> = repository.emergencyCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDonations: StateFlow<List<DonationEntity>> = repository.allDonations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVolunteerOffers: StateFlow<List<VolunteerOfferEntity>> = repository.allVolunteerOffers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAuditLogs: StateFlow<List<AuditLogEntity>> = repository.allAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUpdates: StateFlow<List<CampaignUpdateEntity>> = repository.allUpdates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val filterCriteria = combine(
        searchQuery,
        selectedCategory,
        selectedDistrict,
        verifiedOnly,
        emergencyOnly
    ) { query, cat, dist, verOnly, emergOnly ->
        CampaignFilterCriteria(query, cat, dist, verOnly, emergOnly)
    }

    // Filtered campaigns stream
    val filteredCampaigns: StateFlow<List<CampaignEntity>> = combine(
        allCampaigns,
        filterCriteria
    ) { list, criteria ->
        list.filter { item ->
            val matchesQuery = criteria.query.isBlank() ||
                item.titleEn.contains(criteria.query, ignoreCase = true) ||
                item.titleMr.contains(criteria.query, ignoreCase = true) ||
                item.titleHi.contains(criteria.query, ignoreCase = true) ||
                item.city.contains(criteria.query, ignoreCase = true) ||
                item.district.contains(criteria.query, ignoreCase = true) ||
                item.category.contains(criteria.query, ignoreCase = true)

            val matchesCat = criteria.category == null || item.category == criteria.category
            val matchesDist = criteria.district == null || item.district.equals(criteria.district, ignoreCase = true)
            val matchesVerified = !criteria.verifiedOnly || item.verificationStatus == "VERIFIED"
            val matchesEmergency = !criteria.emergencyOnly || item.isEmergency

            matchesQuery && matchesCat && matchesDist && matchesVerified && matchesEmergency
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Platform impact stats
    val platformStats: StateFlow<PlatformStats> = allCampaigns.combine(allDonations) { campaigns, donations ->
        val verified = campaigns.filter { it.verificationStatus == "VERIFIED" }
        val totalHelpedCount = verified.sumOf { it.donorCount } + 120
        val totalRaisedAmt = campaigns.sumOf { it.amountRaised }
        PlatformStats(
            totalHelped = totalHelpedCount,
            totalRaised = totalRaisedAmt,
            totalVerifiedCampaigns = verified.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlatformStats())

    // Donation Receipt state
    private val _lastDonationReceipt = MutableStateFlow<DonationEntity?>(null)
    val lastDonationReceipt: StateFlow<DonationEntity?> = _lastDonationReceipt.asStateFlow()

    fun clearDonationReceipt() {
        _lastDonationReceipt.value = null
    }

    // Process Donation
    fun makeDonation(
        campaignId: Long,
        campaignTitle: String,
        amount: Double,
        donorName: String,
        isAnonymous: Boolean,
        onSuccess: (DonationEntity) -> Unit = {}
    ) {
        viewModelScope.launch {
            val donation = repository.processDonation(
                campaignId = campaignId,
                campaignTitle = campaignTitle,
                amount = amount,
                donorName = donorName,
                isAnonymous = isAnonymous
            )
            _lastDonationReceipt.value = donation
            onSuccess(donation)
        }
    }

    // Submit Help Request
    fun submitHelpRequest(
        title: String,
        category: String,
        shortDesc: String,
        fullProblem: String,
        beneficiaryName: String,
        contactPhone: String,
        city: String,
        district: String,
        targetAmount: Double,
        targetDate: String,
        isEmergency: Boolean,
        bankUpi: String,
        documentProof: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.submitCampaign(
                titleEn = title,
                category = category,
                shortDescEn = shortDesc,
                fullProblemEn = fullProblem,
                beneficiaryName = beneficiaryName,
                contactPhone = contactPhone,
                city = city,
                district = district,
                targetAmount = targetAmount,
                targetDate = targetDate,
                isEmergency = isEmergency,
                bankUpi = bankUpi,
                documentProof = documentProof
            )
            if (res.isSuccess) {
                onResult(true, "Help request submitted successfully! Status: Under Review.")
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Submission error")
            }
        }
    }

    // Volunteer offer submission
    fun submitVolunteerOffer(
        name: String,
        phone: String,
        city: String,
        district: String,
        offerType: String,
        details: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.submitVolunteerOffer(
                name = name,
                phone = phone,
                city = city,
                district = district,
                offerType = offerType,
                details = details
            )
            onSuccess()
        }
    }

    // Report campaign
    fun reportCampaign(
        campaignId: Long,
        campaignTitle: String,
        reporterName: String,
        reason: String,
        details: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.reportCampaign(
                campaignId = campaignId,
                campaignTitle = campaignTitle,
                reporterName = reporterName,
                reason = reason,
                details = details
            )
            onSuccess()
        }
    }

    // Admin controls
    fun verifyCampaign(id: Long, notes: String) {
        viewModelScope.launch {
            repository.verifyCampaign(id, notes)
        }
    }

    fun rejectCampaign(id: Long, reason: String) {
        viewModelScope.launch {
            repository.rejectCampaign(id, reason)
        }
    }

    fun suspendCampaign(id: Long, reason: String) {
        viewModelScope.launch {
            repository.suspendCampaign(id, reason)
        }
    }

    fun postCampaignUpdate(
        campaignId: Long,
        postedBy: String,
        title: String,
        message: String,
        expenseUtilized: Double,
        proofReference: String
    ) {
        viewModelScope.launch {
            repository.postCampaignUpdate(
                campaignId = campaignId,
                postedBy = postedBy,
                title = title,
                message = message,
                expenseUtilized = expenseUtilized,
                proofReference = proofReference
            )
        }
    }

    fun processRefund(donationId: Long, note: String) {
        viewModelScope.launch {
            repository.processRefund(donationId, note)
        }
    }

    fun markNotificationAsRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }
}
