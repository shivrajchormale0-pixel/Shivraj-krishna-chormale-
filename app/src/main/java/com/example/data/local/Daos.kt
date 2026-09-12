package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AuditLogEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.CampaignUpdateEntity
import com.example.data.model.DonationEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReportEntity
import com.example.data.model.VolunteerOfferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns ORDER BY createdTimestamp DESC")
    fun getAllCampaigns(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM campaigns WHERE verificationStatus = 'VERIFIED' ORDER BY isEmergency DESC, isFeatured DESC, createdTimestamp DESC")
    fun getVerifiedCampaigns(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM campaigns WHERE verificationStatus = 'VERIFIED' AND isEmergency = 1 ORDER BY createdTimestamp DESC")
    fun getEmergencyCampaigns(): Flow<List<CampaignEntity>>

    @Query("SELECT * FROM campaigns WHERE id = :id")
    fun getCampaignById(id: Long): Flow<CampaignEntity?>

    @Query("SELECT * FROM campaigns WHERE id = :id")
    suspend fun getCampaignByIdDirect(id: Long): CampaignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: CampaignEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<CampaignEntity>)

    @Update
    suspend fun updateCampaign(campaign: CampaignEntity)

    @Query("UPDATE campaigns SET verificationStatus = :status, verificationNotes = :notes WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, notes: String)

    @Query("UPDATE campaigns SET amountRaised = amountRaised + :amount, donorCount = donorCount + 1 WHERE id = :id")
    suspend fun recordDonation(id: Long, amount: Double)

    @Query("UPDATE campaigns SET amountUtilized = amountUtilized + :amount WHERE id = :id")
    suspend fun recordExpense(id: Long, amount: Double)

    @Query("SELECT COUNT(*) FROM campaigns")
    suspend fun getCount(): Int

    @Query("SELECT * FROM campaigns WHERE LOWER(titleEn) = LOWER(:title) LIMIT 1")
    suspend fun findByTitle(title: String): CampaignEntity?
}

@Dao
interface DonationDao {
    @Query("SELECT * FROM donations ORDER BY timestamp DESC")
    fun getAllDonations(): Flow<List<DonationEntity>>

    @Query("SELECT * FROM donations WHERE campaignId = :campaignId ORDER BY timestamp DESC")
    fun getDonationsForCampaign(campaignId: Long): Flow<List<DonationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonations(donations: List<DonationEntity>)

    @Query("UPDATE donations SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("SELECT * FROM donations WHERE id = :id")
    suspend fun getDonationById(id: Long): DonationEntity?
}

@Dao
interface VolunteerDao {
    @Query("SELECT * FROM volunteer_offers ORDER BY timestamp DESC")
    fun getAllOffers(): Flow<List<VolunteerOfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: VolunteerOfferEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<VolunteerOfferEntity>)

    @Query("UPDATE volunteer_offers SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}

@Dao
interface CampaignUpdateDao {
    @Query("SELECT * FROM campaign_updates WHERE campaignId = :campaignId ORDER BY timestamp DESC")
    fun getUpdatesForCampaign(campaignId: Long): Flow<List<CampaignUpdateEntity>>

    @Query("SELECT * FROM campaign_updates ORDER BY timestamp DESC")
    fun getAllUpdates(): Flow<List<CampaignUpdateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdate(update: CampaignUpdateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdates(updates: List<CampaignUpdateEntity>)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AuditLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<AuditLogEntity>)
}
