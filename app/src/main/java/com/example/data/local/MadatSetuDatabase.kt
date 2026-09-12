package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AuditLogEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.CampaignUpdateEntity
import com.example.data.model.DonationEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReportEntity
import com.example.data.model.VolunteerOfferEntity

@Database(
    entities = [
        CampaignEntity::class,
        DonationEntity::class,
        VolunteerOfferEntity::class,
        CampaignUpdateEntity::class,
        ReportEntity::class,
        NotificationEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MadatSetuDatabase : RoomDatabase() {
    abstract fun campaignDao(): CampaignDao
    abstract fun donationDao(): DonationDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun campaignUpdateDao(): CampaignUpdateDao
    abstract fun reportDao(): ReportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun auditLogDao(): AuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: MadatSetuDatabase? = null

        fun getDatabase(context: Context): MadatSetuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MadatSetuDatabase::class.java,
                    "madatsetu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
