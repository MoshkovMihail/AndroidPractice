package com.example.androidpractice.ui.memes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidpractice.R
import com.example.androidpractice.data.local.entity.MemeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeListScreen(
    ui: MemeListUiState,
    items: List<MemeEntity>,
    onOpenSort: (MemeSortOption) -> Unit,
    onToggleFavorite: (memeId: Long, current: Boolean) -> Unit
) {
    var showSort by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.memes)) },
                actions = {
                    IconButton(onClick = { showSort = true }, modifier = Modifier.size(100.dp)) {
                        Text(stringResource(R.string.sort))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (ui.isSeeding) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { meme ->
                    MemeCard(
                        meme = meme,
                        onToggleFavorite = { onToggleFavorite(meme.id, meme.isFavorite) }
                    )
                }
            }
        }
    }

    if (showSort) {
        MemeSortBottomSheet(
            current = ui.sort,
            onSelect = {
                onOpenSort(it)
                showSort = false
            },
            onDismiss = { showSort = false }
        )
    }
}

@Composable
private fun MemeCard(
    meme: MemeEntity,
    onToggleFavorite: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(meme.imageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(meme.title, style = MaterialTheme.typography.titleMedium)

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (meme.isFavorite)
                            Icons.Default.Star
                        else
                            Icons.Default.StarBorder,
                        contentDescription = null
                    )
                }
            }

            meme.notes?.takeIf { it.isNotBlank() }?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

