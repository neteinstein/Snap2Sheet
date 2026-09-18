package org.neteinstein.snap2sheet.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.snap2sheet.ui.components.FaturaCard
import org.neteinstein.snap2sheet.ui.components.FaturaIcons
import org.neteinstein.snap2sheet.ui.components.FaturaTopBar
import org.neteinstein.snap2sheet.ui.components.IconBadge
import org.neteinstein.snap2sheet.ui.components.SectionTitle
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onChangeDefaultSpreadsheet: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(FaturaColors.Surface).navigationBarsPadding()) {
        FaturaTopBar(title = "Settings", onBack = onBack)

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                SettingsSection(title = "Account") {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(FaturaColors.AccentSoft),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(state.account?.initials ?: "?", style = MaterialTheme.typography.labelMedium, color = FaturaColors.Accent)
                            }
                            Column {
                                Text(
                                    state.account?.email ?: "Not signed in",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FaturaColors.Ink,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    if (state.account != null) "Connected" else "Disconnected",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (state.account != null) FaturaColors.Success else FaturaColors.Muted,
                                )
                            }
                        }
                        Text(
                            "Sign out",
                            style = MaterialTheme.typography.labelMedium,
                            color = FaturaColors.Danger,
                            modifier = Modifier.clickable(onClick = viewModel::signOut),
                        )
                    }
                }
            }

            item {
                SettingsSection(title = "Default Spreadsheet") {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable(onClick = onChangeDefaultSpreadsheet),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            IconBadge(size = 36.dp) { FaturaIcons.Document(tint = FaturaColors.Accent, size = 18.dp) }
                            Text(
                                state.defaultSpreadsheetName ?: "None selected",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = FaturaColors.Ink,
                            )
                        }
                        Text("Change", style = MaterialTheme.typography.labelMedium, color = FaturaColors.Accent)
                    }
                }
            }

            item {
                SettingsSection(title = "Append Rules") {
                    SettingsSwitchRow(
                        title = "Match columns by header",
                        subtitle = "Fills each column by its title, not position",
                        checked = state.appendRules.matchColumnsByHeader,
                        onCheckedChange = viewModel::setMatchColumnsByHeader,
                    )
                    SettingsSwitchRow(
                        title = "Skip duplicate invoices",
                        subtitle = "Matched by ATCUD code",
                        checked = state.appendRules.skipDuplicateInvoices,
                        onCheckedChange = viewModel::setSkipDuplicates,
                    )
                    SettingsSwitchRow(
                        title = "New sheet tab each month",
                        subtitle = "Otherwise all rows append to one tab",
                        checked = state.appendRules.newSheetTabEachMonth,
                        onCheckedChange = viewModel::setNewSheetTabEachMonth,
                    )
                }
            }

            item {
                SettingsSection(title = "Permissions") {
                    PermissionRow(label = "Camera") { FaturaIcons.Camera(tint = FaturaColors.Muted, size = 17.dp) }
                    PermissionRow(label = "Google Sheets access") { FaturaIcons.Document(tint = FaturaColors.Muted, size = 17.dp) }
                }
            }

            item {
                SettingsSection(title = "Notifications") {
                    SettingsSwitchRow(
                        title = "Notify me if a scan fails",
                        subtitle = "Push notification when a save doesn't go through",
                        checked = state.notifyOnScanFailure,
                        onCheckedChange = viewModel::setNotifyOnScanFailure,
                    )
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 26.dp), contentAlignment = Alignment.Center) {
                    Text("Fatura v1.0.0", style = MaterialTheme.typography.bodySmall, color = FaturaColors.Muted)
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        SectionTitle(text = title)
    }
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)) {
        FaturaCard(modifier = Modifier.fillMaxWidth(), contentPadding = 6.dp) {
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = FaturaColors.Muted)
        }
        Spacer(Modifier.width(12.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedTrackColor = FaturaColors.Accent))
    }
}

@Composable
private fun PermissionRow(label: String, icon: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            icon()
            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
        }
        Text("Allowed", style = MaterialTheme.typography.labelSmall, color = FaturaColors.Success)
    }
}
