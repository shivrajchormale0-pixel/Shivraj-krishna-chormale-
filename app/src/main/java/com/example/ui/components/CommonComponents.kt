package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.data.model.DonationEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.AccentSaffron
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryNavy
import com.example.ui.theme.SoftCardBorder
import com.example.ui.theme.TrustGreen
import com.example.ui.theme.TrustGreenContainer
import com.example.ui.theme.UrgentRed
import com.example.ui.theme.UrgentRedContainer
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatInr(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    return "₹" + formatter.format(amount.toLong())
}

@Composable
fun VerificationBadge(status: String, lang: AppLanguage, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon, label) = when (status) {
        "VERIFIED" -> {
            val lbl = when (lang) {
                AppLanguage.ENGLISH -> "100% Verified"
                AppLanguage.MARATHI -> "१००% पडताळणीकृत"
                AppLanguage.HINDI -> "१००% सत्यापित"
            }
            Tuple4(TrustGreenContainer, TrustGreen, Icons.Default.Verified, lbl)
        }
        "UNDER_REVIEW" -> {
            val lbl = when (lang) {
                AppLanguage.ENGLISH -> "Under Review"
                AppLanguage.MARATHI -> "तपासणी सुरू"
                AppLanguage.HINDI -> "समीक्षाधीन"
            }
            Tuple4(Color(0xFFFEF3C7), Color(0xFFB45309), Icons.Default.HourglassTop, lbl)
        }
        "SUSPENDED" -> {
            Tuple4(UrgentRedContainer, UrgentRed, Icons.Default.Warning, "Suspended")
        }
        else -> {
            Tuple4(Color(0xFFF1F5F9), Color(0xFF64748B), Icons.Default.Warning, status)
        }
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("verification_badge_${status.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun UrgentBadge(lang: AppLanguage, modifier: Modifier = Modifier) {
    val label = when (lang) {
        AppLanguage.ENGLISH -> "URGENT NEED"
        AppLanguage.MARATHI -> "तातडीची गरज"
        AppLanguage.HINDI -> "अति आवश्यक"
    }
    Surface(
        color = UrgentRedContainer,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.testTag("urgent_badge")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(UrgentRed)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = UrgentRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

@Composable
fun CategoryChip(category: String, lang: AppLanguage, modifier: Modifier = Modifier) {
    val localizedName = AppStrings.getCategoryName(category, lang)
    Surface(
        color = PrimaryTeal.copy(alpha = 0.09f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = localizedName,
            color = PrimaryTeal,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun CampaignCard(
    campaign: CampaignEntity,
    lang: AppLanguage,
    onDonateClick: () -> Unit,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = when (lang) {
        AppLanguage.MARATHI -> campaign.titleMr.ifBlank { campaign.titleEn }
        AppLanguage.HINDI -> campaign.titleHi.ifBlank { campaign.titleEn }
        else -> campaign.titleEn
    }
    val shortDesc = when (lang) {
        AppLanguage.MARATHI -> campaign.shortDescMr.ifBlank { campaign.shortDescEn }
        AppLanguage.HINDI -> campaign.shortDescHi.ifBlank { campaign.shortDescEn }
        else -> campaign.shortDescEn
    }

    val progress = if (campaign.targetAmount > 0) {
        (campaign.amountRaised / campaign.targetAmount).coerceIn(0.0, 1.0).toFloat()
    } else 0f
    val percentInt = (progress * 100).toInt()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, SoftCardBorder, RoundedCornerShape(16.dp))
            .testTag("campaign_card_${campaign.id}")
            .clickable { onDetailsClick() }
    ) {
        Column {
            // Header Graphic Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = if (campaign.isEmergency) {
                                listOf(UrgentRed.copy(alpha = 0.85f), AccentSaffron.copy(alpha = 0.9f))
                            } else {
                                listOf(SecondaryNavy, PrimaryTeal)
                            }
                        )
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    CategoryChip(
                        category = campaign.category,
                        lang = lang,
                        modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp))
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (campaign.isEmergency) {
                            UrgentBadge(lang)
                        }
                        VerificationBadge(status = campaign.verificationStatus, lang = lang)
                    }
                }

                // Location tag at bottom of banner
                Text(
                    text = "📍 ${campaign.city}, ${campaign.district}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }

            // Body
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = shortDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (campaign.isEmergency) UrgentRed else PrimaryTeal,
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = formatInr(campaign.amountRaised),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = PrimaryTeal
                        )
                        Text(
                            text = "raised of " + formatInr(campaign.targetAmount),
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "$percentInt%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${campaign.daysRemaining}d left",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDetailsClick,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("details_button_${campaign.id}"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = AppStrings.viewDetails(lang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = onDonateClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (campaign.isEmergency) UrgentRed else AccentSaffron
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("donate_button_${campaign.id}"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = AppStrings.donateNow(lang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DonationReceiptDialog(
    donation: DonationEntity,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Date(donation.timestamp))

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                modifier = Modifier.testTag("close_receipt_button")
            ) {
                Text(
                    text = when (lang) {
                        AppLanguage.ENGLISH -> "Done / Close"
                        AppLanguage.MARATHI -> "पूर्ण / बंद करा"
                        AppLanguage.HINDI -> "संपन्न / बंद करें"
                    }
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { /* Share or download receipt receipt */ },
                modifier = Modifier.testTag("share_receipt_button")
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share / Save")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = TrustGreen,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (lang) {
                        AppLanguage.ENGLISH -> "Official Donation Receipt"
                        AppLanguage.MARATHI -> "अधिकृत देणगी पावती"
                        AppLanguage.HINDI -> "आधिकारिक दान रसीद"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "MadatSetu India Foundation",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryNavy
                    )
                    Text(
                        text = "80G Exempted",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TrustGreen
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ReceiptRow(label = "Receipt No", value = donation.receiptNumber)
                ReceiptRow(label = "Transaction ID", value = donation.transactionId)
                ReceiptRow(label = "Campaign", value = donation.campaignTitle)
                ReceiptRow(label = "Donor Name", value = if (donation.isAnonymous) "Anonymous Benefactor" else donation.donorName)
                ReceiptRow(label = "Amount Paid", value = formatInr(donation.amount), isHighlight = true)
                ReceiptRow(label = "Date & Time", value = dateStr)
                ReceiptRow(label = "Payment Engine", value = donation.paymentMethod)
                ReceiptRow(label = "Status", value = "SUCCESS (Escrow Verified)")

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "✓ 100% of this donation directly reaches the verified hospital/vendor escrow account. No raw card or bank secrets are retained.",
                        fontSize = 10.sp,
                        color = Color(0xFF475569),
                        lineHeight = 14.sp
                    )
                }
            }
        }
    )
}

@Composable
fun ReceiptRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = if (isHighlight) 14.sp else 11.sp,
            fontWeight = if (isHighlight) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (isHighlight) PrimaryTeal else Color(0xFF0F172A),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
    }
}
