package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val title: String) {
    DONOR("Donor"),
    BENEFICIARY("Beneficiary"),
    VOLUNTEER("Volunteer"),
    ADMIN("Admin")
}

@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titleEn: String,
    val titleMr: String,
    val titleHi: String,
    val category: String,
    val shortDescEn: String,
    val shortDescMr: String,
    val shortDescHi: String,
    val fullProblemEn: String,
    val fullProblemMr: String,
    val fullProblemHi: String,
    val beneficiaryName: String,
    val contactPhone: String, // Kept safe/masked in public UI
    val city: String,
    val district: String,
    val targetAmount: Double,
    val amountRaised: Double = 0.0,
    val amountUtilized: Double = 0.0,
    val donorCount: Int = 0,
    val targetDate: String,
    val daysRemaining: Int = 30,
    val imageUrl: String = "",
    val documentProofSummary: String = "Government ID verified & Hospital/School bill attached",
    val bankOrUpiMasked: String = "UPI ID: ****setu@upi",
    val isEmergency: Boolean = false,
    val verificationStatus: String = "UNDER_REVIEW", // VERIFIED, UNDER_REVIEW, REJECTED, COMPLETED, SUSPENDED
    val verificationNotes: String = "",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val isFeatured: Boolean = false
)

@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val campaignId: Long,
    val campaignTitle: String,
    val amount: Double,
    val donorName: String,
    val isAnonymous: Boolean = false,
    val transactionId: String,
    val paymentMethod: String = "UPI (Auto-verified)",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS", // SUCCESS, REFUNDED, PENDING_REFUND
    val receiptNumber: String
)

@Entity(tableName = "volunteer_offers")
data class VolunteerOfferEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val campaignId: Long? = null,
    val volunteerName: String,
    val volunteerPhone: String,
    val city: String,
    val district: String,
    val offerType: String, // Food, Clothes, Books, Educational Materials, Permitted Non-Cash
    val details: String,
    val status: String = "Available", // Available, Connected, Delivered
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "campaign_updates")
data class CampaignUpdateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val campaignId: Long,
    val postedBy: String,
    val title: String,
    val message: String,
    val expenseUtilized: Double = 0.0,
    val proofReference: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val campaignId: Long,
    val campaignTitle: String,
    val reporterName: String,
    val reportReason: String, // Fraud, Fake documents, Misleading information, Duplicate campaign, Abuse
    val details: String,
    val status: String = "Pending", // Pending, Investigating, Resolved, Dismissed
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val category: String, // APPROVAL, REJECTION, DONATION, MILESTONE, UPDATE, REFUND, ADMIN
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val action: String,
    val performedBy: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
