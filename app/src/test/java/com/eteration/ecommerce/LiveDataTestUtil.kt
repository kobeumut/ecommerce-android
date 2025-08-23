package com.eteration.ecommerce

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Helper function to get the value from LiveData for testing purposes.
 * This function observes the LiveData and returns the value once it's set.
 *
 * For coroutines-based ViewModels, use this after calling test methods
 * and ensure you have MainDispatcherRule set up.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is never set.
        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value was never set.")
        }
    } catch (e: Exception) {
        this.removeObserver(observer)
        throw e
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

/**
 * Observes a LiveData until it receives a new value matching the predicate,
 * then returns that value. Useful for testing specific state changes.
 */
@VisibleForTesting
fun <T> LiveData<T>.getOrAwaitValueMatching(
    predicate: (T?) -> Boolean,
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            if (predicate(value)) {
                data = value
                latch.countDown()
                this@getOrAwaitValueMatching.removeObserver(this)
            }
        }
    }

    this.observeForever(observer)

    try {
        afterObserve.invoke()

        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value matching predicate was never set.")
        }
    } catch (e: Exception) {
        this.removeObserver(observer)
        throw e
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}