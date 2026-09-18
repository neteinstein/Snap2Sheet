package org.neteinstein.snap2sheet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.snap2sheet.ui.theme.FaturaColors

@Composable
fun FaturaCard(
    modifier: Modifier = Modifier,
    containerColor: Color = FaturaColors.Surface,
    borderColor: Color = FaturaColors.Border,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(modifier = Modifier.padding(contentPadding), content = content)
    }
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    trailing: String? = null,
    onTrailingClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = FaturaColors.MutedStrong,
            letterSpacing = 0.6.sp,
        )
        if (trailing != null) {
            val trailingModifier = if (onTrailingClick != null) {
                Modifier.clickable(onClick = onTrailingClick)
            } else {
                Modifier
            }
            Text(
                text = trailing,
                style = MaterialTheme.typography.labelMedium,
                color = FaturaColors.Accent,
                modifier = trailingModifier,
            )
        }
    }
}

/** A rounded square badge holding a single centered icon composable (see [FaturaIcons]). */
@Composable
fun IconBadge(
    modifier: Modifier = Modifier,
    background: Color = FaturaColors.AccentSoft,
    size: Dp = 40.dp,
    cornerRadius: Dp = 12.dp,
    icon: @Composable () -> Unit,
) {
    Surface(modifier = modifier.size(size), shape = RoundedCornerShape(cornerRadius), color = background) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            icon()
        }
    }
}
