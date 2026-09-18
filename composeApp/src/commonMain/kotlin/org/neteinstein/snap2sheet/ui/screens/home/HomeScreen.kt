package org.neteinstein.snap2sheet.ui.screens.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.snap2sheet.ui.components.BottomNavBar
import org.neteinstein.snap2sheet.ui.components.BottomNavTab
import org.neteinstein.snap2sheet.ui.components.FaturaCard
import org.neteinstein.snap2sheet.ui.components.FaturaIcons
import org.neteinstein.snap2sheet.ui.components.IconBadge
import org.neteinstein.snap2sheet.ui.components.InvoiceRow
import org.neteinstein.snap2sheet.ui.components.SectionTitle
import org.neteinstein.snap2sheet.ui.components.formatAmount
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun HomeScreen(
    onScan: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(FaturaColors.Surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Fatura", style = MaterialTheme.typography.titleLarge, color = FaturaColors.Ink)
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(FaturaColors.AccentSoft).clickable(onClick = onSettings),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.account?.initials ?: "?",
                    style = MaterialTheme.typography.labelMedium,
                    color = FaturaColors.Accent,
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    FaturaCard(containerColor = FaturaColors.SurfaceMuted, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "THIS MONTH",
                            style = MaterialTheme.typography.labelMedium,
                            color = FaturaColors.Muted,
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("${state.invoicesThisMonth}", style = MaterialTheme.typography.headlineSmall, color = FaturaColors.Ink)
                            Text("invoices ·", style = MaterialTheme.typography.bodyMedium, color = FaturaColors.Muted)
                            Text("€${formatAmount(state.totalThisMonth)}", style = MaterialTheme.typography.titleSmall, color = FaturaColors.Ink)
                        }
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            FaturaIcons.Document(tint = FaturaColors.Accent, size = 15.dp)
                            Text(
                                state.primarySpreadsheetName ?: "No spreadsheet yet",
                                style = MaterialTheme.typography.labelMedium,
                                color = FaturaColors.Accent,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    FaturaCard(
                        containerColor = FaturaColors.AccentSoft,
                        borderColor = FaturaColors.Accent,
                        modifier = Modifier.fillMaxWidth().clickable(onClick = onScan),
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            IconBadge(background = FaturaColors.Accent, size = 44.dp) {
                                FaturaIcons.Camera(tint = androidx.compose.ui.graphics.Color.White, size = 22.dp)
                            }
                            Column {
                                Text("Scan a new invoice", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
                                Text("Point your camera at the QR code", style = MaterialTheme.typography.bodySmall, color = FaturaColors.Muted)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
                    SectionTitle(text = "Recent scans", trailing = "See all", onTrailingClick = onHistory)
                }
            }

            items(state.recentInvoices) { invoice ->
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    InvoiceRow(invoice = invoice)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        BottomNavBar(
            selected = BottomNavTab.HOME,
            onHome = {},
            onHistory = onHistory,
            onSettings = onSettings,
            onScan = onScan,
        )
    }
}
