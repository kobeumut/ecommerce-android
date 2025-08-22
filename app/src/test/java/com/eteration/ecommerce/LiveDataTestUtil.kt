package com.eteration.ecommerce

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.mockito.exceptions.base.MockitoException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Helper function to get the value from LiveData for testing purposes.
 * This function observes the LiveData and returns the value once it's set.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(2)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the latch will never be counted down
    if (!latch.await(time, timeUnit)) {
        throw MockitoException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}