package org.neteinstein.snap2sheet.ui.screens.scan

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.neteinstein.snap2sheet.ui.components.FaturaIcons

private val ScanBackgroundTop = Color(0xFF2B2F38)
private val ScanBackgroundMid = Color(0xFF15171C)
private val ScanBackgroundBottom = Color(0xFF0C0D10)
private val ScanAccent = Color(0xFF5FA0FF)

@Composable
fun ScanScreen(onBack: () -> Unit, onCaptured: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(ScanBackgroundTop, ScanBackgroundMid, ScanBackgroundBottom),
                )
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundIconButton(onClick = onBack) { FaturaIcons.Back(tint = Color.White) }
            Text("Scan Invoice", style = MaterialTheme.typography.titleSmall, color = Color.White)
            RoundIconButton(onClick = {}) { FaturaIcons.Document(tint = Color.White, size = 18.dp) }
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.size(250.dp).clickable(onClick = onCaptured),
                contentAlignment = Alignment.Center,
            ) {
                FaturaIcons.ScanCorners(tint = ScanAccent, modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(ScanAccent.copy(alpha = 0.85f)))
            }
            Spacer(Modifier.height(26.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF4ADE80)))
                Text("Looking for a QR code…", style = MaterialTheme.typography.bodySmall, color = Color(0xFFEDEFF2))
            }
            Spacer(Modifier.height(26.dp))
            Text(
                "Align the invoice's QR code inside the frame. It fills in automatically.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC6CAD2),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(260.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("or tap to capture", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9BA1AB), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = onCaptured),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.size(54.dp).clip(CircleShape).background(Color.White))
            }
        }
    }
}

@Composable
private fun RoundIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.14f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
