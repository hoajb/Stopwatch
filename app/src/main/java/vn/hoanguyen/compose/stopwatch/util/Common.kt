package vn.hoanguyen.compose.stopwatch.util

/**
 * Created by Hoa Nguyen on Oct 28 2022.
 */

fun formatTime(seconds: String, minutes: String, hours: String): String {
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}