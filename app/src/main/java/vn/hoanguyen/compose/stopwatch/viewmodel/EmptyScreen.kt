package vn.hoanguyen.compose.stopwatch.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import vn.hoanguyen.compose.stopwatch.ui.theme.ColorBackground

/**
 * Created by Hoa Nguyen on Nov 17 2022.
 */
@ExperimentalAnimationApi
@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}