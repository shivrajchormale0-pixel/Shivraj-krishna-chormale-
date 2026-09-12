package com.example.localization

object AppStrings {
    fun home(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.MARATHI -> "मुख्यपृष्ठ"
        AppLanguage.HINDI -> "होम"
    }

    fun appName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "MadatSetu"
        AppLanguage.MARATHI -> "मदतसेतू"
        AppLanguage.HINDI -> "मददसेतु"
    }

    fun slogan(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Help Someone. Change a Life."
        AppLanguage.MARATHI -> "कोणाला तरी मदत करा. एक जीवन बदला."
        AppLanguage.HINDI -> "किसी की मदद करें. एक जीवन बदलें."
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Search campaigns by name, city, or cause..."
        AppLanguage.MARATHI -> "मोहीम, शहर किंवा कारण शोधा..."
        AppLanguage.HINDI -> "अभियान, शहर या कारण खोजें..."
    }

    fun emergencyHelp(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Emergency Help"
        AppLanguage.MARATHI -> "आपत्कालीन मदत"
        AppLanguage.HINDI -> "आपातकालीन सहायता"
    }

    fun urgentOnly(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Urgent Need"
        AppLanguage.MARATHI -> "तातडीची गरज"
        AppLanguage.HINDI -> "अति आवश्यक"
    }

    fun featuredCampaigns(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Featured Verified Campaigns"
        AppLanguage.MARATHI -> "विशेष पडताळणी मोहिमा"
        AppLanguage.HINDI -> "विशेष सत्यापित अभियान"
    }

    fun recentCampaigns(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Recently Added"
        AppLanguage.MARATHI -> "नुकतेच जोडलेले"
        AppLanguage.HINDI -> "हाल ही में जोड़े गए"
    }

    fun totalHelped(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Total People Helped"
        AppLanguage.MARATHI -> "एकूण मदत मिळालेले लोक"
        AppLanguage.HINDI -> "कुल लाभान्वित लोग"
    }

    fun totalRaised(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Total Funds Raised"
        AppLanguage.MARATHI -> "एकूण उभारलेला निधी"
        AppLanguage.HINDI -> "कुल जुटाई गई राशि"
    }

    fun totalVerified(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Verified Campaigns"
        AppLanguage.MARATHI -> "पडताळणी झालेले प्रकल्प"
        AppLanguage.HINDI -> "सत्यापित अभियान"
    }

    fun donateNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Donate Now"
        AppLanguage.MARATHI -> "आताच देणगी द्या"
        AppLanguage.HINDI -> "अभी दान करें"
    }

    fun viewDetails(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "View Details"
        AppLanguage.MARATHI -> "तपशील पहा"
        AppLanguage.HINDI -> "विवरण देखें"
    }

    fun createHelpRequest(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Request Help"
        AppLanguage.MARATHI -> "मदतीची विनंती"
        AppLanguage.HINDI -> "मदद का अनुरोध"
    }

    fun volunteer(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Volunteer"
        AppLanguage.MARATHI -> "स्वयंसेवक"
        AppLanguage.HINDI -> "स्वयंसेवक"
    }

    fun adminPanel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Admin Portal"
        AppLanguage.MARATHI -> "प्रशासक पोर्टल"
        AppLanguage.HINDI -> "व्यवस्थापक पोर्टल"
    }

    fun profile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Profile"
        AppLanguage.MARATHI -> "माझे खाते"
        AppLanguage.HINDI -> "मेरी प्रोफ़ाइल"
    }

    fun transparencyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Fund Transparency & Utilization"
        AppLanguage.MARATHI -> "निधी पारदर्शकता आणि वापर"
        AppLanguage.HINDI -> "निधि पारदर्शिता और उपयोग"
    }

    fun reportCampaign(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Report Campaign"
        AppLanguage.MARATHI -> "मोहिमेची तक्रार करा"
        AppLanguage.HINDI -> "अभियान की रिपोर्ट करें"
    }

    fun verifiedBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "100% Verified"
        AppLanguage.MARATHI -> "१००% पडताळणीकृत"
        AppLanguage.HINDI -> "१००% सत्यापित"
    }

    fun underReview(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Under Review"
        AppLanguage.MARATHI -> "तपासणी सुरू"
        AppLanguage.HINDI -> "समीक्षाधीन"
    }

    fun privacyNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Privacy Protected: Beneficiary Aadhaar, Bank and UPI details are securely encrypted and never shown publicly."
        AppLanguage.MARATHI -> "गोपनीयता सुरक्षित: आधार, बँक किंवा युपीआय तपशील कधीही सार्वजनिक केले जात नाहीत. केवळ अधिकृत प्रशासक तपासतात."
        AppLanguage.HINDI -> "गोपनीयता सुरक्षित: आधार, बैंक या यूपीआई विवरण कभी सार्वजनिक नहीं किए जाते। केवल अधिकृत व्यवस्थापक सत्यापन करते हैं."
    }

    fun getCategoryName(category: String, lang: AppLanguage): String = when (category) {
        "Medical Help" -> when (lang) {
            AppLanguage.ENGLISH -> "Medical Help"
            AppLanguage.MARATHI -> "वैद्यकीय मदत"
            AppLanguage.HINDI -> "चिकित्सा सहायता"
        }
        "Education" -> when (lang) {
            AppLanguage.ENGLISH -> "Education"
            AppLanguage.MARATHI -> "शिक्षण सहाय्य"
            AppLanguage.HINDI -> "शिक्षा सहायता"
        }
        "Food & Essentials" -> when (lang) {
            AppLanguage.ENGLISH -> "Food & Essentials"
            AppLanguage.MARATHI -> "अन्न आणि जीवनावश्यक वस्तू"
            AppLanguage.HINDI -> "भोजन एवं आवश्यक सामग्री"
        }
        "Elderly Support" -> when (lang) {
            AppLanguage.ENGLISH -> "Elderly Support"
            AppLanguage.MARATHI -> "ज्येष्ठ नागरिक सहाय्य"
            AppLanguage.HINDI -> "बुजुर्ग सहायता"
        }
        "Disability Support" -> when (lang) {
            AppLanguage.ENGLISH -> "Disability Support"
            AppLanguage.MARATHI -> "दिव्यांग बांधव मदत"
            AppLanguage.HINDI -> "दिव्यांग सहायता"
        }
        "Emergency Support" -> when (lang) {
            AppLanguage.ENGLISH -> "Emergency Support"
            AppLanguage.MARATHI -> "आपत्कालीन सहाय्य"
            AppLanguage.HINDI -> "आपातकालीन सहायता"
        }
        "Livelihood Support" -> when (lang) {
            AppLanguage.ENGLISH -> "Livelihood Support"
            AppLanguage.MARATHI -> "रोजगार व उपजीविका"
            AppLanguage.HINDI -> "आजीविका सहायता"
        }
        else -> category
    }
}
