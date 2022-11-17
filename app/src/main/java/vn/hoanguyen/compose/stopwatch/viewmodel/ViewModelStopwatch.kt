package vn.hoanguyen.compose.stopwatch.viewmodel

import androidx.compose.runtime.mutableStateOf
import vn.hoanguyen.compose.stopwatch.service.StopwatchState

/**
 * Created by Hoa Nguyen on Nov 15 2022.
 */
class StopwatchStateManagement {
    val seconds = mutableStateOf("00")
    val minutes = mutableStateOf("00")
    val hours = mutableStateOf("00")

    val currentState = mutableStateOf(StopwatchState.Idle)
}