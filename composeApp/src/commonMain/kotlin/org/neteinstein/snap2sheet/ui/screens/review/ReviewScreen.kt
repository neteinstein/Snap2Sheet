package org.neteinstein.snap2sheet.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.snap2sheet.ui.components.FaturaCard
import org.neteinstein.snap2sheet.ui.components.FaturaIcons
import org.neteinstein.snap2sheet.ui.components.FaturaPrimaryButton
import org.neteinstein.snap2sheet.ui.components.FaturaTopBar
import org.neteinstein.snap2sheet.ui.components.IconBadge
import org.neteinstein.snap2sheet.ui.components.SectionTitle
import org.neteinstein.snap2sheet.ui.components.formatAmount
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun ReviewScreen(
    onBack: () -> Unit,
    onChooseDestination: () -> Unit,
    onSave: () -> Unit,
    viewModel: ReviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val invoice = state.invoice ?: return

    Column(modifier = Modifier.fillMaxSize().background(FaturaColors.Surface)) {
        FaturaTopBar(title = "Review Invoice", onBack = onBack)

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(FaturaColors.SuccessSoft, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .fillMaxWidth(),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("✓", color = FaturaColors.Success, fontWeight = FontWeight.Bold)
                    Text("QR code scanned successfully", style = MaterialTheme.typography.labelMedium, color = FaturaColors.Success)
                }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)) {
                    FaturaCard(containerColor = FaturaColors.SurfaceMuted, modifier = Modifier.fillMaxWidth()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            IconBadge(size = 34.dp, cornerRadius = 9.dp, background = FaturaColors.Surface) {
                                FaturaIcons.Document(tint = FaturaColors.Muted, size = 18.dp)
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(invoice.merchantName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
                            Text(
                                "${invoice.documentNumber} · ${invoice.date}",
                                style = MaterialTheme.typography.bodySmall,
                                color = FaturaColors.Muted,
                            )
                            Spacer(Modifier.height(10.dp))
                            Text("€${formatAmount(invoice.total)}", style = MaterialTheme.typography.headlineMedium, color = FaturaColors.Ink)
                        }
                    }
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)) {
                    SectionTitle(text = "Invoice details")
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    FaturaCard(modifier = Modifier.fillMaxWidth(), contentPadding = 6.dp) {
                        FieldRow("NIF Emitente", invoice.nifEmitente) { viewModel.updateNifEmitente(it) }
                        FieldRow("NIF Adquirente", invoice.nifAdquirente) { viewModel.updateNifAdquirente(it) }
                        FieldRow("Tipo de Documento", invoice.documentType) { viewModel.updateDocumentType(it) }
                        FieldRow("N.º Documento", invoice.documentNumber) { viewModel.updateDocumentNumber(it) }
                        FieldRow("Data", invoice.date) { viewModel.updateDate(it) }
                        FieldRow("ATCUD", invoice.atcud) { viewModel.updateAtcud(it) }
                        FieldRow("Base Tributável", "€${formatAmount(invoice.taxBase)}") { viewModel.updateTaxBase(it) }
                        FieldRow("IVA (23%)", "€${formatAmount(invoice.vat)}") { viewModel.updateVat(it) }
                        FieldRow("Total", "€${formatAmount(invoice.total)}", bold = true, showDivider = false) { viewModel.updateTotal(it) }
                    }
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)) {
                    SectionTitle(text = "Destination")
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {
                    FaturaCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onChooseDestination)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconBadge(size = 36.dp) { FaturaIcons.Document(tint = FaturaColors.Accent, size = 18.dp) }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("WILL BE SAVED TO", style = MaterialTheme.typography.labelSmall, color = FaturaColors.MutedStrong)
                                Text(
                                    state.selectedSpreadsheetName ?: "Choose a spreadsheet",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FaturaColors.Ink,
                                )
                            }
                            FaturaIcons.ChevronRight(tint = FaturaColors.Subtle)
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars).padding(20.dp)) {
            FaturaPrimaryButton(text = "Save Invoice", onClick = onSave)
        }
    }
}

@Composable
private fun FieldRow(
    label: String,
    value: String,
    bold: Boolean = false,
    showDivider: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = FaturaColors.Muted,
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = FaturaColors.Ink,
                    fontWeight = if (bold) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = if (bold) 14.5.sp else 13.5.sp,
                    textAlign = TextAlign.End,
                ),
                modifier = Modifier.weight(1f, fill = true),
            )
        }
        if (showDivider) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FaturaColors.Divider))
        }
    }
}
