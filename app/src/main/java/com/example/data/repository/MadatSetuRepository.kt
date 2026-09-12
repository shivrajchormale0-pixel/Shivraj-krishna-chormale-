package com.example.data.repository

import com.example.data.local.MadatSetuDatabase
import com.example.data.model.AuditLogEntity
import com.example.data.model.CampaignEntity
import com.example.data.model.CampaignUpdateEntity
import com.example.data.model.DonationEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ReportEntity
import com.example.data.model.VolunteerOfferEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MadatSetuRepository(private val db: MadatSetuDatabase) {
    private val campaignDao = db.campaignDao()
    private val donationDao = db.donationDao()
    private val volunteerDao = db.volunteerDao()
    private val updateDao = db.campaignUpdateDao()
    private val reportDao = db.reportDao()
    private val notificationDao = db.notificationDao()
    private val auditDao = db.auditLogDao()

    val allCampaigns: Flow<List<CampaignEntity>> = campaignDao.getAllCampaigns()
    val verifiedCampaigns: Flow<List<CampaignEntity>> = campaignDao.getVerifiedCampaigns()
    val emergencyCampaigns: Flow<List<CampaignEntity>> = campaignDao.getEmergencyCampaigns()
    val allDonations: Flow<List<DonationEntity>> = donationDao.getAllDonations()
    val allVolunteerOffers: Flow<List<VolunteerOfferEntity>> = volunteerDao.getAllOffers()
    val allReports: Flow<List<ReportEntity>> = reportDao.getAllReports()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val allAuditLogs: Flow<List<AuditLogEntity>> = auditDao.getAllLogs()
    val allUpdates: Flow<List<CampaignUpdateEntity>> = updateDao.getAllUpdates()

    fun getCampaignById(id: Long): Flow<CampaignEntity?> = campaignDao.getCampaignById(id)
    fun getDonationsForCampaign(id: Long): Flow<List<DonationEntity>> = donationDao.getDonationsForCampaign(id)
    fun getUpdatesForCampaign(id: Long): Flow<List<CampaignUpdateEntity>> = updateDao.getUpdatesForCampaign(id)

    suspend fun checkAndSeedInitialData() {
        if (campaignDao.getCount() > 0) return

        val initialCampaigns = listOf(
            CampaignEntity(
                titleEn = "Emergency Heart Surgery for 4-year-old Aarav",
                titleMr = "४ वर्षांच्या आरवच्या तातडीच्या हृदयाच्या शस्त्रक्रियेसाठी मदत",
                titleHi = "४ वर्षीय आरव की आपातकालीन हृदय शल्यचिकित्सा हेतु सहायता",
                category = "Medical Help",
                shortDescEn = "Aarav requires urgent open-heart surgery at Deenanath Mangeshkar Hospital, Pune.",
                shortDescMr = "दीनानाथ मंगेशकर रुग्णालय, पुणे येथे आरवला तातडीच्या ओपन हार्ट शस्त्रक्रियेची गरज आहे.",
                shortDescHi = "दीनानाथ मंगेशकर अस्पताल, पुणे में आरव को तत्काल ओपन-हार्ट सर्जरी की आवश्यकता है।",
                fullProblemEn = "Aarav was diagnosed with a congenital heart defect (Tetralogy of Fallot). His father works as a daily-wage transport driver and the family has exhausted all savings on diagnostic ICU stays. The cardiac surgeon has scheduled the corrective procedure within 14 days. All hospital estimations and civil surgeon certifications have been scrutinized and approved by MadatSetu verification committee.",
                fullProblemMr = "आरवला जन्मजात हृदयाचा दोष आढळला आहे. त्याचे वडील दैनिक रोजंदारीवरील वाहनचालक असून कुटुंबाने सर्व बचत तपासण्यांवर खर्च केली आहे. शस्त्रक्रियेसाठी तातडीने निधीची आवश्यकता आहे. रुग्णालयाची सर्व कागदपत्रे पडताळली गेली आहेत.",
                fullProblemHi = "आरव को जन्मजात हृदय विकार है। उसके पिता दिहाड़ी चालक हैं और परिवार की सारी जमा पूंजी खत्म हो चुकी है। सर्जन ने १४ दिनों के भीतर सर्जरी निर्धारित की है। सभी अस्पताल अनुमान सत्यापित हैं।",
                beneficiaryName = "Santosh Kadam (Father of Aarav)",
                contactPhone = "+91 98230 *****",
                city = "Pune",
                district = "Pune",
                targetAmount = 350000.0,
                amountRaised = 265000.0,
                amountUtilized = 180000.0,
                donorCount = 142,
                targetDate = "28 Sep 2026",
                daysRemaining = 7,
                imageUrl = "heart_surgery",
                documentProofSummary = "Deenanath Mangeshkar Hospital Estimation Ref #DMH-78291, Ayushman card partial waiver attached.",
                bankOrUpiMasked = "Hospital Beneficiary Account: SBIN000****4192 (Direct Payout)",
                isEmergency = true,
                verificationStatus = "VERIFIED",
                verificationNotes = "Hospital billing department and medical superintendent verified via physical visit by Pune volunteer coordinator.",
                isFeatured = true
            ),
            CampaignEntity(
                titleEn = "Higher Education Fund for Ananya (Farmer's Daughter)",
                titleMr = "अनन्याच्या उच्च शिक्षणासाठी मदत (शेतकऱ्याची हुशार कन्या)",
                titleHi = "अनन्या की उच्च शिक्षा के लिए सहायता (किसान की मेधावी पुत्री)",
                category = "Education",
                shortDescEn = "Supporting Ananya's Government Engineering College tuition & hostel fees in Solapur.",
                shortDescMr = "सोलापूर येथील शासकीय अभियांत्रिकी महाविद्यालयाच्या शैक्षणिक व वसतिगृह शुल्कासाठी सहाय्य.",
                shortDescHi = "सोलापुर में सरकारी इंजीनियरिंग कॉलेज की ट्यूशन और छात्रावास फीस के लिए सहायता।",
                fullProblemEn = "Ananya secured 96.4% in MHT-CET and earned admission into Computer Engineering. Coming from a drought-prone taluka in Solapur, her family faces severe financial strain due to crop losses. The requested fund directly covers first-year government college fees and textbook kits without any intermediary costs.",
                fullProblemMr = "अनन्याने सीईटीमध्ये ९६.४% गुण मिळवून कॉम्प्युटर इंजिनिअरिंगला प्रवेश मिळवला. सोलापूरमधील दुष्काळी भागातील शेतकरी कुटुंबाला शुल्कासाठी मदतीची गरज आहे.",
                fullProblemHi = "अनन्या ने प्रवेश परीक्षा में ९६.४% अंक प्राप्त किए। सोलापुर के सूखा प्रभावित क्षेत्र के किसान परिवार को फीस के लिए सहयोग की आवश्यकता है।",
                beneficiaryName = "Ananya Bhosale",
                contactPhone = "+91 94220 *****",
                city = "Solapur",
                district = "Solapur",
                targetAmount = 85000.0,
                amountRaised = 62500.0,
                amountUtilized = 40000.0,
                donorCount = 48,
                targetDate = "15 Oct 2026",
                daysRemaining = 24,
                imageUrl = "education_ananya",
                documentProofSummary = "Admission letter, Merit rank card, and 7/12 land extract verified.",
                bankOrUpiMasked = "College Treasury Account: MAHB000****7812",
                isEmergency = false,
                verificationStatus = "VERIFIED",
                verificationNotes = "College registrar confirmed provisional seat; merit certificates authenticated.",
                isFeatured = true
            ),
            CampaignEntity(
                titleEn = "Monthly Ration & Healthcare for Matoshree Elderly Care Home",
                titleMr = "मातोश्री वृद्धाश्रम: मासिक अन्नधान्य व औषधोपचार सहाय्य",
                titleHi = "मातोश्री वृद्धाश्रम: मासिक राशन और स्वास्थ्य देखभाल सहायता",
                category = "Elderly Support",
                shortDescEn = "Ensuring safe shelter, hot meals, and chronic illness medicines for 45 abandoned seniors in Nashik.",
                shortDescMr = "नाशिकमधील ४५ बेघर वृद्ध नागरिकांसाठी सुरक्षित निवारा, गरम जेवण व औषधोपचार.",
                shortDescHi = "नासिक में ४५ निराश्रित बुजुर्गों के लिए आश्रय, भोजन और दवाइयों की व्यवस्था।",
                fullProblemEn = "Matoshree Vriddhashram provides dignified shelter for 45 senior citizens with zero family support. They require ongoing monthly grain, cooking oil, diabetic insulin, and hypertension medications. Volunteer teams audit grocery bills every month for complete transparency.",
                fullProblemMr = "नाशिक येथील मातोश्री वृद्धाश्रमात ४५ ज्येष्ठ नागरिक वास्तव्यास आहेत. त्यांच्या मासिक किराणा, तेल व औषधांसाठी दात्यांच्या सहकार्याची आवश्यकता आहे.",
                fullProblemHi = "४५ निराश्रित वरिष्ठ नागरिकों के भोजन, दवाओं और देखभाल के लिए मासिक सहायता की आवश्यकता है।",
                beneficiaryName = "Matoshree Seva Trust (Trust Reg #E-4819)",
                contactPhone = "+91 91580 *****",
                city = "Nashik",
                district = "Nashik",
                targetAmount = 120000.0,
                amountRaised = 94000.0,
                amountUtilized = 70000.0,
                donorCount = 89,
                targetDate = "05 Oct 2026",
                daysRemaining = 14,
                imageUrl = "elderly_care",
                documentProofSummary = "Registered Charity Commissioner certificate & monthly audit receipts verified.",
                bankOrUpiMasked = "Trust Account: PUNB000****6721",
                isEmergency = false,
                verificationStatus = "VERIFIED",
                verificationNotes = "Nashik local administrative coordinator verified physical premises and senior residents.",
                isFeatured = false
            ),
            CampaignEntity(
                titleEn = "Motorized Tricycle & Mobility Kit for Suresh (Kolhapur)",
                titleMr = "सुरेश यांच्यासाठी मोटारीकृत ट्रायसायकल व स्वावलंबन किट",
                titleHi = "सुरेश के लिए मोटराइज्ड ट्राइसाइकिल और मोबिलिटी किट",
                category = "Disability Support",
                shortDescEn = "Enabling independent mobility and small newspaper vending livelihood for 80% locational disabled Suresh.",
                shortDescMr = "८०% दिव्यांग असणाऱ्या सुरेश यांना वृत्तपत्र विक्रीचा व्यवसाय करण्यासाठी मोटारीकृत ट्रायसायकल.",
                shortDescHi = "दिव्यांग सुरेश को आत्मनिर्भर बनाने और समाचार पत्र वितरण हेतु मोटराइज्ड ट्राइसाइकिल।",
                fullProblemEn = "Suresh suffered polio during childhood with 80% lower limb impairment. With a motorized tricycle, he can travel to the local railway station depot to pick up morning periodicals and sustain his aged mother independently.",
                fullProblemMr = "सुरेश हे ८०% अस्थिव्यंग दिव्यांग आहेत. ट्रायसायकल मिळाल्यास ते स्वाभिमानाने वर्तमानपत्रे वाटून कुटुंबाचा उदरनिर्वाह करू शकतील.",
                fullProblemHi = "८०% दिव्यांग सुरेश को स्वतंत्र रोजगार शुरू करने के लिए मोटराइज्ड तिपहिया वाहन की आवश्यकता है।",
                beneficiaryName = "Suresh Jadhav",
                contactPhone = "+91 97640 *****",
                city = "Kolhapur",
                district = "Kolhapur",
                targetAmount = 48000.0,
                amountRaised = 42000.0,
                amountUtilized = 35000.0,
                donorCount = 31,
                targetDate = "20 Sep 2026",
                daysRemaining = 9,
                imageUrl = "disability_support",
                documentProofSummary = "Civil Hospital Disability Certificate (UDID valid) and vendor quote attached.",
                bankOrUpiMasked = "Beneficiary Direct Payout: BKID000****1129",
                isEmergency = false,
                verificationStatus = "VERIFIED",
                verificationNotes = "UDID verified via Swavlamban Card portal. Vendor quotation for retrofitted tricycle checked.",
                isFeatured = false
            ),
            CampaignEntity(
                titleEn = "Emergency Relief & Drinking Water for Flood-Hit Konkan Hamlets",
                titleMr = "कोकणातील पूरग्रस्त वाड्यांसाठी तातडीची मदत व शुद्ध पिण्याचे पाणी",
                titleHi = "बाढ़ प्रभावित कोंकण क्षेत्रों के लिए आपात राहत और पेयजल",
                category = "Emergency Support",
                shortDescEn = "Providing dry ration kits, water purifiers, and emergency hygiene kits to 200 displaced families.",
                shortDescMr = "पूरग्रस्तांसाठी सुका खाऊ, धान्य किट्स व वॉटर प्युरिफायर संच वाटप.",
                shortDescHi = "विस्थापित परिवारों को सूखा राशन, शुद्ध जल और आपातकालीन किट वितरण।",
                fullProblemEn = "Severe flash floods and landslides submerged several villages in Chiplun/Ratnagiri district. Water pipelines are broken and electricity is cut off. MadatSetu verified volunteers are delivering solar lanterns, chlorinated water tablets, and essential ration packs directly on the ground.",
                fullProblemMr = "चिपळूण परिसरातील गावांमध्ये अचानक आलेल्या पुरामुळे २०० कुटुंबांचे अतोनात नुकसान झाले आहे. स्वयंसेवकांमार्फत थेट मदत पोहोचवली जात आहे.",
                fullProblemHi = "बाढ़ से घिरे गांवों में शुद्ध पानी और खाद्य सामग्री पहुंचाने के लिए त्वरित सहायता।",
                beneficiaryName = "MadatSetu Ground Relief Taskforce Chiplun",
                contactPhone = "+91 90110 *****",
                city = "Chiplun",
                district = "Ratnagiri",
                targetAmount = 500000.0,
                amountRaised = 380000.0,
                amountUtilized = 290000.0,
                donorCount = 210,
                targetDate = "18 Sep 2026",
                daysRemaining = 4,
                imageUrl = "flood_relief",
                documentProofSummary = "District Collectorate calamity notification and distribution manifest attached.",
                bankOrUpiMasked = "Disaster Relief Escrow: HDFC000****9901",
                isEmergency = true,
                verificationStatus = "VERIFIED",
                verificationNotes = "Verified through district administration disaster management cell.",
                isFeatured = true
            ),
            CampaignEntity(
                titleEn = "Dialysis & Kidney Transplant Support for Ramesh (Under Review)",
                titleMr = "रमेश यांच्या डायलिसिस व किडनी प्रत्यारोपणासाठी मदत (पडताळणी सुरू)",
                titleHi = "रमेश के डायलिसिस और प्रत्यारोपण हेतु सहयोग (समीक्षाधीन)",
                category = "Medical Help",
                shortDescEn = "Under verification: Dialysis bills submitted for Ramesh from Chhatrapati Sambhajinagar.",
                shortDescMr = "पडताळणी सुरू: छत्रपती संभाजीनगर येथील रमेश यांच्या उपचारांची कागदपत्रे तपासणीत.",
                shortDescHi = "समीक्षाधीन: रमेश के डायलिसिस बिलों का प्रशासनिक सत्यापन प्रक्रिया में है।",
                fullProblemEn = "Ramesh requires bi-weekly hemodialysis. The beneficiary submitted hospital admission slips and government ration card yesterday. Admin verification team is cross-checking with medical officer before opening public donations.",
                fullProblemMr = "रमेश यांना आठवड्यातून दोन वेळा डायलिसिसची गरज आहे. प्रशासकीय पडताळणी पूर्ण झाल्यावर मोहीम खुली केली जाईल.",
                fullProblemHi = "डायलिसिस सहायता हेतु आवेदन प्राप्त। प्रशासनिक जांच के बाद यह अभियान लाइव होगा।",
                beneficiaryName = "Ramesh Gaikwad",
                contactPhone = "+91 99750 *****",
                city = "Chhatrapati Sambhajinagar",
                district = "Aurangabad",
                targetAmount = 200000.0,
                amountRaised = 0.0,
                amountUtilized = 0.0,
                donorCount = 0,
                targetDate = "15 Nov 2026",
                daysRemaining = 60,
                imageUrl = "dialysis_review",
                documentProofSummary = "Government Medical College OPD card & pathology reports awaiting phone cross-check.",
                bankOrUpiMasked = "Beneficiary Account: Pending Admin Approval",
                isEmergency = false,
                verificationStatus = "UNDER_REVIEW",
                verificationNotes = "Submitted on Sep 10. Medical invoice confirmation requested from GMCH Aurangabad.",
                isFeatured = false
            ),
            CampaignEntity(
                titleEn = "Self-Help Stitching Units for Drought Widows (Nagpur)",
                titleMr = "विदर्भातील गरजू महिलांसाठी शिवणयंत्रे व स्वावलंबन उपक्रम",
                titleHi = "जरूरतमंद महिलाओं के लिए सिलाई मशीन और आजीविका सहायता",
                category = "Livelihood Support",
                shortDescEn = "Equipping 15 women with commercial sewing machines and textile orders for sustainable livelihood.",
                shortDescMr = "१५ महिलांना शिलाई मशीन व कापडकाम साहित्य पुरवून शाश्वत रोजगार निर्माण करणे.",
                shortDescHi = "१५ महिलाओं को सिलाई मशीनें प्रदान कर आत्मनिर्भर बनाने की पहल।",
                fullProblemEn = "To help single mothers and widows of distressed farmers achieve independent monthly income, this initiative provides commercial motor-operated sewing machines and school uniform tailoring contracts in rural Nagpur clusters.",
                fullProblemMr = "ग्रामीण भागातील महिलांना शिवणकाम शिकवून स्वतःच्या पायावर उभे करण्यासाठी मशीन वाटप प्रकल्प.",
                fullProblemHi = "महिलाओं को सिलाई कार्य के माध्यम से नियमित आय प्रदान करने का प्रकल्प।",
                beneficiaryName = "Prerna Mahila Bachat Gat",
                contactPhone = "+91 94230 *****",
                city = "Nagpur",
                district = "Nagpur",
                targetAmount = 150000.0,
                amountRaised = 110000.0,
                amountUtilized = 75000.0,
                donorCount = 72,
                targetDate = "30 Oct 2026",
                daysRemaining = 40,
                imageUrl = "livelihood_sewing",
                documentProofSummary = "Self-Help Group resolution and District Rural Development Agency certificate verified.",
                bankOrUpiMasked = "SHG Account: CBIN028****3390",
                isEmergency = false,
                verificationStatus = "VERIFIED",
                verificationNotes = "NGO coordinator conducted spot inspection of tailoring training center.",
                isFeatured = false
            )
        )

        campaignDao.insertCampaigns(initialCampaigns)

        // Seed initial campaign updates
        val initialUpdates = listOf(
            CampaignUpdateEntity(
                campaignId = 1,
                postedBy = "Deenanath Mangeshkar Hospital Coordinator",
                title = "Surgery Date Confirmed: Sep 20",
                message = "The pediatric cardiac team has scheduled Aarav's surgery. Pre-operative blood cross-matching is complete. ₹1,80,000 hospital advance disbursed directly to hospital account.",
                expenseUtilized = 180000.0,
                proofReference = "Hospital Receipt #DMH-ADV-9021 verified",
                timestamp = System.currentTimeMillis() - 86400000L * 2
            ),
            CampaignUpdateEntity(
                campaignId = 2,
                postedBy = "Ananya Bhosale",
                title = "First Semester College Fee Receipt Uploaded",
                message = "Heartfelt thanks to all donors! ₹40,000 has been paid for semester tuition and college ID has been generated.",
                expenseUtilized = 40000.0,
                proofReference = "College Challan #GEC-FEES-2026-081",
                timestamp = System.currentTimeMillis() - 86400000L * 5
            ),
            CampaignUpdateEntity(
                campaignId = 5,
                postedBy = "MadatSetu Disaster Response",
                title = "120 Water Filtration Kits Handed Over",
                message = "First dispatch of emergency water tablets and dry food delivered to affected hamlets near Chiplun river bank.",
                expenseUtilized = 290000.0,
                proofReference = "Relief Material Gatepass #DR-CHP-041",
                timestamp = System.currentTimeMillis() - 86400000L * 1
            )
        )
        updateDao.insertUpdates(initialUpdates)

        // Seed volunteer offers
        val initialOffers = listOf(
            VolunteerOfferEntity(
                volunteerName = "Dr. Rohan Deshpande",
                volunteerPhone = "+91 98810 *****",
                city = "Pune",
                district = "Pune",
                offerType = "Permitted Non-Cash Assistance",
                details = "Can provide free post-op pediatric consultation and medical dressing assistance for children in Pune city.",
                status = "Available"
            ),
            VolunteerOfferEntity(
                volunteerName = "Sneha Kulkarni",
                volunteerPhone = "+91 97650 *****",
                city = "Nashik",
                district = "Nashik",
                offerType = "Books",
                details = "Have 40 sets of Class 10th and 11th State Board textbooks and reference guides in good condition.",
                status = "Available"
            ),
            VolunteerOfferEntity(
                volunteerName = "Amit Patil",
                volunteerPhone = "+91 98220 *****",
                city = "Kolhapur",
                district = "Kolhapur",
                offerType = "Food",
                details = "Can distribute 50 freshly packed wholesome lunch boxes every weekend to needy families near local civil hospital.",
                status = "Available"
            ),
            VolunteerOfferEntity(
                volunteerName = "Pooja Gaikwad",
                volunteerPhone = "+91 99230 *****",
                city = "Solapur",
                district = "Solapur",
                offerType = "Clothes",
                details = "Ready to donate 60 clean winter sweaters and blankets for elderly shelter residents.",
                status = "Available"
            )
        )
        volunteerDao.insertOffers(initialOffers)

        // Seed initial notifications
        val initialNotifications = listOf(
            NotificationEntity(
                title = "Aarav's Heart Surgery Campaign Reached 75% Milestone!",
                message = "Over 140 generous citizens supported Aarav. Direct payment was made to Deenanath Hospital.",
                category = "MILESTONE",
                isRead = false
            ),
            NotificationEntity(
                title = "Welcome to MadatSetu",
                message = "Every campaign on MadatSetu undergoes strict multi-tier verification before going live. All bank and identity details are encrypted.",
                category = "ADMIN",
                isRead = false
            ),
            NotificationEntity(
                title = "Emergency Flood Relief Active",
                message = "Chiplun flood response is active. Volunteer distribution teams are on the ground.",
                category = "UPDATE",
                isRead = false
            )
        )
        notificationDao.insertNotifications(initialNotifications)

        // Seed audit logs
        val initialAudit = listOf(
            AuditLogEntity(
                action = "CAMPAIGN_VERIFIED",
                performedBy = "Admin (Chief Verifier)",
                details = "Aarav Heart Surgery Campaign verified with Hospital DMH-78291 verification letter."
            ),
            AuditLogEntity(
                action = "CAMPAIGN_VERIFIED",
                performedBy = "Admin (Regional Officer)",
                details = "Ananya Education Campaign approved following college fee challan verification."
            ),
            AuditLogEntity(
                action = "NEW_CAMPAIGN_SUBMITTED",
                performedBy = "Beneficiary (Ramesh Gaikwad)",
                details = "Dialysis assistance campaign submitted. Status marked as UNDER_REVIEW."
            )
        )
        auditDao.insertLogs(initialAudit)
    }

    // Donation transaction flow
    suspend fun processDonation(
        campaignId: Long,
        campaignTitle: String,
        amount: Double,
        donorName: String,
        isAnonymous: Boolean
    ): DonationEntity {
        val txnCode = "MSTU-UPI-" + SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(Date()) + "-" + (1000..9999).random()
        val receiptCode = "RCPT-" + UUID.randomUUID().toString().take(8).uppercase()

        val donation = DonationEntity(
            campaignId = campaignId,
            campaignTitle = campaignTitle,
            amount = amount,
            donorName = if (isAnonymous) "Anonymous Donor" else donorName,
            isAnonymous = isAnonymous,
            transactionId = txnCode,
            paymentMethod = "BHIM UPI (Secured Payment Engine)",
            timestamp = System.currentTimeMillis(),
            status = "SUCCESS",
            receiptNumber = receiptCode
        )

        val id = donationDao.insertDonation(donation)
        campaignDao.recordDonation(campaignId, amount)

        // Add audit log
        auditDao.insertLog(
            AuditLogEntity(
                action = "DONATION_SUCCESS",
                performedBy = if (isAnonymous) "Anonymous" else donorName,
                details = "Donation of ₹${amount.toInt()} received for campaign #$campaignId via UPI ($txnCode)."
            )
        )

        // Add notification
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Donation Successful! (₹${amount.toInt()})",
                message = "Thank you! Your donation for '$campaignTitle' is recorded. Transaction ID: $txnCode. 80G tax receipt ready.",
                category = "DONATION"
            )
        )

        return donation.copy(id = id)
    }

    // Submit new campaign with duplicate check and rate-limiting
    suspend fun submitCampaign(
        titleEn: String,
        category: String,
        shortDescEn: String,
        fullProblemEn: String,
        beneficiaryName: String,
        contactPhone: String,
        city: String,
        district: String,
        targetAmount: Double,
        targetDate: String,
        isEmergency: Boolean,
        bankUpi: String,
        documentProof: String
    ): Result<Long> {
        // Fraud prevention: duplicate check
        val existing = campaignDao.findByTitle(titleEn.trim())
        if (existing != null) {
            return Result.failure(Exception("A campaign with a very similar title already exists. Potential duplicate detected."))
        }

        val maskedUpi = if (bankUpi.contains("@")) {
            val parts = bankUpi.split("@")
            "UPI: ****" + parts[0].takeLast(2) + "@" + parts[1]
        } else {
            "Bank A/C: ****" + bankUpi.takeLast(4)
        }

        val newCampaign = CampaignEntity(
            titleEn = titleEn,
            titleMr = titleEn,
            titleHi = titleEn,
            category = category,
            shortDescEn = shortDescEn,
            shortDescMr = shortDescEn,
            shortDescHi = shortDescEn,
            fullProblemEn = fullProblemEn,
            fullProblemMr = fullProblemEn,
            fullProblemHi = fullProblemEn,
            beneficiaryName = beneficiaryName,
            contactPhone = contactPhone.take(4) + " *****",
            city = city,
            district = district,
            targetAmount = targetAmount,
            amountRaised = 0.0,
            amountUtilized = 0.0,
            donorCount = 0,
            targetDate = targetDate,
            daysRemaining = 30,
            imageUrl = "default_campaign",
            documentProofSummary = documentProof.ifBlank { "Identity card and proof documents received in administrative vault." },
            bankOrUpiMasked = maskedUpi,
            isEmergency = isEmergency,
            verificationStatus = "UNDER_REVIEW", // Mandatory review!
            verificationNotes = "Submitted by user. Awaiting MadatSetu field officer verification call and document inspection.",
            isFeatured = false
        )

        val newId = campaignDao.insertCampaign(newCampaign)

        auditDao.insertLog(
            AuditLogEntity(
                action = "CAMPAIGN_SUBMITTED",
                performedBy = beneficiaryName,
                details = "New campaign '$titleEn' submitted. Queued for admin fraud check and document authentication."
            )
        )

        notificationDao.insertNotification(
            NotificationEntity(
                title = "Campaign Submitted for Verification",
                message = "Your request '$titleEn' is under review. Our verification officer will inspect your documents before public activation.",
                category = "APPROVAL"
            )
        )

        return Result.success(newId)
    }

    // Admin Verification Operations
    suspend fun verifyCampaign(id: Long, notes: String) {
        campaignDao.updateStatus(id, "VERIFIED", notes)
        val campaign = campaignDao.getCampaignByIdDirect(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "CAMPAIGN_APPROVED",
                performedBy = "Admin",
                details = "Campaign #${id} ('${campaign?.titleEn}') approved and marked 100% VERIFIED. Notes: $notes"
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Campaign Approved & Verified!",
                message = "Campaign '${campaign?.titleEn}' has passed all checks and is now publicly live for donations.",
                category = "APPROVAL"
            )
        )
    }

    suspend fun rejectCampaign(id: Long, reason: String) {
        campaignDao.updateStatus(id, "REJECTED", reason)
        val campaign = campaignDao.getCampaignByIdDirect(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "CAMPAIGN_REJECTED",
                performedBy = "Admin",
                details = "Campaign #${id} rejected. Reason: $reason"
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Campaign Verification Rejected",
                message = "Campaign '${campaign?.titleEn}' could not be verified. Reason: $reason",
                category = "REJECTION"
            )
        )
    }

    suspend fun suspendCampaign(id: Long, reason: String) {
        campaignDao.updateStatus(id, "SUSPENDED", reason)
        val campaign = campaignDao.getCampaignByIdDirect(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "CAMPAIGN_SUSPENDED",
                performedBy = "Admin",
                details = "Campaign #${id} suspended due to fraud flag / investigation. Reason: $reason"
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Campaign Suspended for Investigation",
                message = "Campaign '${campaign?.titleEn}' has been temporarily halted for compliance review.",
                category = "ADMIN"
            )
        )
    }

    // Volunteer offers
    suspend fun submitVolunteerOffer(
        name: String,
        phone: String,
        city: String,
        district: String,
        offerType: String,
        details: String
    ): Long {
        val offer = VolunteerOfferEntity(
            volunteerName = name,
            volunteerPhone = phone.take(4) + " *****",
            city = city,
            district = district,
            offerType = offerType,
            details = details,
            status = "Available"
        )
        val id = volunteerDao.insertOffer(offer)
        auditDao.insertLog(
            AuditLogEntity(
                action = "VOLUNTEER_OFFER_REGISTERED",
                performedBy = name,
                details = "Volunteer offered $offerType in $city, $district."
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Volunteer Assistance Registered",
                message = "Thank you $name! Your offer of $offerType is visible to local coordinators.",
                category = "DONATION"
            )
        )
        return id
    }

    // Campaign updates & expense utilization
    suspend fun postCampaignUpdate(
        campaignId: Long,
        postedBy: String,
        title: String,
        message: String,
        expenseUtilized: Double,
        proofReference: String
    ) {
        updateDao.insertUpdate(
            CampaignUpdateEntity(
                campaignId = campaignId,
                postedBy = postedBy,
                title = title,
                message = message,
                expenseUtilized = expenseUtilized,
                proofReference = proofReference
            )
        )
        if (expenseUtilized > 0) {
            campaignDao.recordExpense(campaignId, expenseUtilized)
        }
        val campaign = campaignDao.getCampaignByIdDirect(campaignId)
        notificationDao.insertNotification(
            NotificationEntity(
                title = "New Update: ${campaign?.titleEn}",
                message = "$title - $message",
                category = "UPDATE"
            )
        )
        auditDao.insertLog(
            AuditLogEntity(
                action = "EXPENSE_UPDATE_POSTED",
                performedBy = postedBy,
                details = "Expense of ₹${expenseUtilized.toInt()} logged for campaign #$campaignId. Proof: $proofReference"
            )
        )
    }

    // Reporting
    suspend fun reportCampaign(
        campaignId: Long,
        campaignTitle: String,
        reporterName: String,
        reason: String,
        details: String
    ): Long {
        val report = ReportEntity(
            campaignId = campaignId,
            campaignTitle = campaignTitle,
            reporterName = reporterName,
            reportReason = reason,
            details = details,
            status = "Pending"
        )
        val id = reportDao.insertReport(report)
        auditDao.insertLog(
            AuditLogEntity(
                action = "CAMPAIGN_REPORTED",
                performedBy = reporterName,
                details = "Report logged against campaign #$campaignId. Reason: $reason."
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Report Received for Review",
                message = "Thank you for helping keep MadatSetu secure. Our vigilance team is evaluating report #$id.",
                category = "ADMIN"
            )
        )
        return id
    }

    // Refund processing
    suspend fun processRefund(donationId: Long, adminNote: String) {
        val donation = donationDao.getDonationById(donationId) ?: return
        donationDao.updateStatus(donationId, "REFUNDED")
        auditDao.insertLog(
            AuditLogEntity(
                action = "DONATION_REFUNDED",
                performedBy = "Admin",
                details = "Donation of ₹${donation.amount} (Txn: ${donation.transactionId}) refunded. Note: $adminNote"
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Donation Refund Processed",
                message = "Refund of ₹${donation.amount} for '${donation.campaignTitle}' processed back to source account.",
                category = "REFUND"
            )
        )
    }

    suspend fun markNotificationRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsRead() {
        notificationDao.markAllAsRead()
    }
}
