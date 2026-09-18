package org.neteinstein.snap2sheet.ui.screens.destination

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.neteinstein.snap2sheet.domain.model.SpreadsheetDestination
import org.neteinstein.snap2sheet.ui.components.FaturaCard
import org.neteinstein.snap2sheet.ui.components.FaturaDashedButton
import org.neteinstein.snap2sheet.ui.components.FaturaIcons
import org.neteinstein.snap2sheet.ui.components.FaturaPrimaryButton
import org.neteinstein.snap2sheet.ui.components.FaturaTopBar
import org.neteinstein.snap2sheet.ui.components.IconBadge
import org.neteinstein.snap2sheet.ui.components.SectionTitle
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun DestinationScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: DestinationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(FaturaColors.Surface)) {
        FaturaTopBar(title = "Choose Spreadsheet", onBack = onBack)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(FaturaColors.Background, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FaturaIcons.Search(tint = FaturaColors.MutedStrong, size = 16.dp)
            Box(modifier = Modifier.weight(1f)) {
                if (state.query.isEmpty()) {
                    Text("Search your Google Sheets", style = MaterialTheme.typography.bodyMedium, color = FaturaColors.Subtle)
                }
                BasicTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(color = FaturaColors.Ink, fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp)) {
                    SectionTitle(text = "Your spreadsheets")
                }
            }

            items(state.filtered) { destination ->
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 5.dp)) {
                    SpreadsheetOptionRow(
                        destination = destination,
                        selected = destination.id == state.selectedId,
                        onClick = { viewModel.selectDestination(destination.id) },
                    )
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)) {
                    FaturaDashedButton(text = "Create new spreadsheet", onClick = { viewModel.createDestination("New Spreadsheet") }) {
                        FaturaIcons.Plus(tint = FaturaColors.Muted)
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    FaturaCard(containerColor = FaturaColors.SurfaceMuted, modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("Always save here automatically", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
                            Switch(
                                checked = state.alwaysSaveAutomatically,
                                onCheckedChange = viewModel::onAlwaysSaveChange,
                                colors = SwitchDefaults.colors(checkedTrackColor = FaturaColors.Accent),
                            )
                        }
                        Text(
                            "Skip this screen next time. You can change this anytime in Settings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = FaturaColors.Muted,
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {
                    FaturaCard(modifier = Modifier.fillMaxWidth()) {
                        Text("APPEND RULES", style = MaterialTheme.typography.labelSmall, color = FaturaColors.MutedStrong)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "New row per invoice · Columns matched by header · Skip duplicate ATCUD",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = FaturaColors.Ink,
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars).padding(20.dp)) {
            FaturaPrimaryButton(text = "Save & Continue", onClick = {
                viewModel.saveDraftToSelectedDestination()
                onSaved()
            })
        }
    }
}

@Composable
private fun SpreadsheetOptionRow(destination: SpreadsheetDestination, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) FaturaColors.AccentSoft else FaturaColors.Surface)
            .border(
                width = 1.5.dp,
                color = if (selected) FaturaColors.Accent else FaturaColors.Border,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconBadge(size = 36.dp, background = FaturaColors.Surface) {
            FaturaIcons.Document(tint = if (selected) FaturaColors.Accent else FaturaColors.Muted, size = 18.dp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(destination.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = FaturaColors.Ink)
            Text(destination.lastEditedLabel, style = MaterialTheme.typography.bodySmall, color = FaturaColors.MutedStrong)
        }
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, if (selected) FaturaColors.Accent else FaturaColors.BorderStrong, CircleShape)
                .background(FaturaColors.Surface, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(FaturaColors.Accent))
            }
        }
    }
}
