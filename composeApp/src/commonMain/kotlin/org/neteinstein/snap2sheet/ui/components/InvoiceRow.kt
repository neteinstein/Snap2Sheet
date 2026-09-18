package org.neteinstein.snap2sheet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.neteinstein.snap2sheet.domain.model.Invoice
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun InvoiceRow(invoice: Invoice, modifier: Modifier = Modifier, subtitle: String = invoice.scannedAtLabel) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconBadge(background = FaturaColors.Background, size = 38.dp, cornerRadius = 10.dp) {
            FaturaIcons.Document(tint = FaturaColors.Muted, size = 18.dp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(invoice.merchantName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = FaturaColors.MutedStrong)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "€" + formatAmount(invoice.total),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = FaturaColors.Ink,
            )
            StatusChip(status = invoice.status, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

fun formatAmount(value: Double): String {
    val cents = kotlin.math.round(value * 100).toLong()
    val whole = cents / 100
    val frac = (cents % 100).let { if (it < 0) -it else it }
    return "$whole." + frac.toString().padStart(2, '0')
}
