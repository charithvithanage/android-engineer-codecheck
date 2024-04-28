package jp.co.yumemi.android.code_check.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Awaits and returns the value of a LiveData, blocking the current thread until the value is set or a timeout occurs.
 *
 * @param time The maximum time to wait for the LiveData value to be set, default is 2 seconds.
 * @param timeUnit The time unit for the timeout, default is seconds.
 * @param afterObserve A lambda function to be executed after observing the LiveData, default is an empty function.
 * @throws TimeoutException if the LiveData value is not set within the specified timeout.
 * @return The value of the LiveData once it's set.
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

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}