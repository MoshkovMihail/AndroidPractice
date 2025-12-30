package com.example.androidpractice.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidpractice.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletedAccountScreen(
    isLoading: Boolean,
    onRestore: () -> Unit,
    onDeleteForever: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.account_deleted_title)) },
                navigationIcon = {
                    TextButton(onClick = onBackToLogin) {
                        Text(stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Text(stringResource(R.string.account_deleted_message))

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onRestore,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.restore_account))
            }

            OutlinedButton(
                onClick = onDeleteForever,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete_forever))
            }
        }
    }
}
