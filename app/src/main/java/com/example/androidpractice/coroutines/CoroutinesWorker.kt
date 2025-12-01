package com.example.androidpractice.coroutines

import kotlinx.coroutines.delay
import kotlin.random.Random

enum class CoroutineErrorType {
    Toast,
    Snackbar,
    Reset
}


suspend fun runHeavyOperation(): CoroutineErrorType? {
    val delayTime = Random.nextLong(1_000L, 10_001L) // 1–10 секунд
    delay(delayTime)

    if (delayTime >= 7_000L && Random.nextInt(100) < 30) {
        return when (Random.nextInt(3)) {
            0 -> CoroutineErrorType.Toast
            1 -> CoroutineErrorType.Snackbar
            else -> CoroutineErrorType.Reset
        }
    }
    return null
}
