package com.example.androidpractice.ui.memes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidpractice.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeSortBottomSheet(
    current: MemeSortOption,
    onSelect: (MemeSortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            SortRow(
                text = stringResource(R.string.sort_newest),
                selected = current == MemeSortOption.NEWEST
            ) { onSelect(MemeSortOption.NEWEST) }

            SortRow(
                text = stringResource(R.string.sort_title),
                selected = current == MemeSortOption.TITLE
            ) { onSelect(MemeSortOption.TITLE) }

            SortRow(
                text = stringResource(R.string.sort_favorites),
                selected = current == MemeSortOption.FAVORITES_FIRST
            ) { onSelect(MemeSortOption.FAVORITES_FIRST) }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SortRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text)
        if (selected) {
            Text("✓")
        }
    }
}
