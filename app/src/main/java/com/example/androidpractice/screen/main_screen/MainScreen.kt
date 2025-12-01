package com.example.androidpractice.screen.main_screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractice.R
import com.example.androidpractice.coroutines.CoroutineErrorType
import com.example.androidpractice.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    val coroutineCount = viewModel.coroutineCount
    val isSequential = viewModel.isSequential
    val startLazily = viewModel.startLazily
    val workInBackground = viewModel.workInBackground
    val isRunning = viewModel.isRunning
    val selectedDispatcher = viewModel.selectedDispatcher
    val dispatcherItems = viewModel.dispatcherItems
    val lastError = viewModel.lastError
    val cancelledCount = viewModel.cancelledCount


    LaunchedEffect(lastError) {
        when (lastError) {
            CoroutineErrorType.Toast -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }

            CoroutineErrorType.Snackbar -> {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.error_snackbar)
                )
            }

            CoroutineErrorType.Reset -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_reset),
                    Toast.LENGTH_SHORT
                ).show()
            }

            null -> Unit
        }
        viewModel.consumeError()
    }


    LaunchedEffect(cancelledCount) {
        cancelledCount?.let { count ->
            Toast.makeText(
                context,
                context.getString(R.string.coroutines_cancelled, count),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.clearCancelledCount()
        }
    }


    DisposableEffect(workInBackground, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    viewModel.onAppBackgrounded()
                }

                Lifecycle.Event.ON_START -> {
                    viewModel.onAppForegrounded()
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            MySlider(
                value = coroutineCount,
                onValueChange = { newValue ->
                    val stepped = (newValue / 5) * 5
                    viewModel.onCoroutineCountChange(
                        stepped.coerceIn(10, 100)
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            DropdownMenuBox(
                selected = selectedDispatcher,
                items = dispatcherItems,
                onItemSelected = { viewModel.onDispatcherSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))


            SwitchWithLabel(
                label = stringResource(R.string.sequentially_coroutine),
                checked = isSequential,
                onCheckedChange = { checked ->
                    viewModel.onSequentialChange(checked)
                }
            )


            SwitchWithLabel(
                label = stringResource(R.string.parallel_coroutine),
                checked = !isSequential,
                onCheckedChange = { checked ->
                    viewModel.onParallelChange(checked)
                }
            )

            SwitchWithLabel(
                label = stringResource(R.string.start_coroutine_lazily),
                checked = startLazily,
                onCheckedChange = { viewModel.onStartLazilyChange(it) }
            )

            SwitchWithLabel(
                label = stringResource(R.string.work_on_background),
                checked = workInBackground,
                onCheckedChange = { viewModel.onWorkInBackgroundChange(it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isRunning) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Button(
                    onClick = { viewModel.cancelCoroutines() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.cancel_coroutines))
                }
            } else {
                Button(
                    onClick = { viewModel.launchCoroutines() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.create_corutine))
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    selected: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SwitchWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
fun MySlider(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            steps = 17,
            valueRange = 10f..100f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                activeTickColor = MaterialTheme.colorScheme.secondary,
                inactiveTickColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )
        Text(text = value.toString())
    }
}
