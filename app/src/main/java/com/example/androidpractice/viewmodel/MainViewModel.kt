package com.example.androidpractice.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.coroutines.CoroutineErrorType
import com.example.androidpractice.coroutines.runHeavyOperation
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    var coroutineCount by mutableStateOf(10)
        private set

    val dispatcherItems = listOf(
        "Dispatchers.Default",
        "Dispatchers.IO",
        "Dispatchers.Main",
        "Dispatchers.Unconfined"
    )

    var selectedDispatcher by mutableStateOf(dispatcherItems.first())
        private set

    var isSequential by mutableStateOf(true)
        private set

    var startLazily by mutableStateOf(false)
        private set

    var workInBackground by mutableStateOf(true)
        private set

    var isRunning by mutableStateOf(false)
        private set

    // Сколько корутин планировали запустить последний раз
    private var lastRequestedCount = 0

    // Родительский Job для отмены
    private var parentJob: Job? = null

    // Событие "ошибка корутины" (для Toast/Snackbar/сообщения о сбросе)
    var lastError: CoroutineErrorType? by mutableStateOf(null)
        private set

    // Событие "отменено корутин"
    var cancelledCount: Int? by mutableStateOf(null)
        private set

    fun onCoroutineCountChange(newValue: Int) {
        coroutineCount = newValue
    }

    fun onDispatcherSelected(newValue: String) {
        selectedDispatcher = newValue
    }

    fun onSequentialChange(checked: Boolean) {
        isSequential = checked
    }

    fun onParallelChange(checked: Boolean) {
        isSequential = !checked
    }

    fun onStartLazilyChange(checked: Boolean) {
        startLazily = checked
    }

    fun onWorkInBackgroundChange(checked: Boolean) {
        workInBackground = checked
    }

    fun consumeError() {
        lastError = null
    }

    fun clearCancelledCount() {
        cancelledCount = null
    }

    private fun mapDispatcher(label: String): CoroutineDispatcher =
        when (label) {
            "Dispatchers.IO" -> Dispatchers.IO
            "Dispatchers.Main" -> Dispatchers.Main
            "Dispatchers.Unconfined" -> Dispatchers.Unconfined
            else -> Dispatchers.Default
        }

    fun resetSettingsToDefault() {
        coroutineCount = 10
        selectedDispatcher = dispatcherItems.first()
        isSequential = true
        startLazily = false
        workInBackground = true
    }

    private suspend fun runSingleTask(dispatcher: CoroutineDispatcher) {
        withContext(dispatcher) {
            val error = runHeavyOperation()
            when (error) {
                CoroutineErrorType.Toast -> {
                    lastError = CoroutineErrorType.Toast
                }

                CoroutineErrorType.Snackbar -> {
                    lastError = CoroutineErrorType.Snackbar
                }

                CoroutineErrorType.Reset -> {
                    resetSettingsToDefault()
                    lastError = CoroutineErrorType.Reset
                }

                null -> Unit
            }
        }
    }


    fun launchCoroutines() {
        if (isRunning) return

        val dispatcher = mapDispatcher(selectedDispatcher)
        isRunning = true
        lastRequestedCount = coroutineCount

        parentJob = viewModelScope.launch(
            start = if (startLazily && isSequential) {
                CoroutineStart.LAZY
            } else {
                CoroutineStart.DEFAULT
            }
        ) {
            try {
                if (isSequential) {
                    repeat(coroutineCount) {
                        runSingleTask(dispatcher)
                    }
                } else {
                    coroutineScope {
                        val jobs = List(coroutineCount) {
                            launch(
                                start = if (startLazily) {
                                    CoroutineStart.LAZY
                                } else {
                                    CoroutineStart.DEFAULT
                                }
                            ) {
                                runSingleTask(dispatcher)
                            }
                        }
                        if (startLazily) {
                            jobs.forEach { it.start() }
                        }
                        jobs.joinAll()
                    }
                }
            } finally {
                isRunning = false
                parentJob = null
            }
        }

        if (isSequential && startLazily) {
            parentJob?.start()
        }
    }


    fun cancelCoroutines() {
        val job = parentJob
        if (job == null || !job.isActive) return

        job.cancel()
        cancelledCount = lastRequestedCount
        isRunning = false
        parentJob = null
    }

    fun onAppBackgrounded() {
        if (!workInBackground) {
            parentJob?.cancel()
        }
    }

    fun onAppForegrounded() {
        if (!workInBackground && parentJob == null && !isRunning && lastRequestedCount > 0) {
            launchCoroutines()
        }
    }
}
