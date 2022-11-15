package vn.hoanguyen.compose.stopwatch

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import vn.hoanguyen.compose.stopwatch.service.ServiceHelper
import vn.hoanguyen.compose.stopwatch.service.StopwatchService
import vn.hoanguyen.compose.stopwatch.service.StopwatchState
import vn.hoanguyen.compose.stopwatch.ui.theme.Light
import vn.hoanguyen.compose.stopwatch.ui.theme.Red
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_CANCEL
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_START
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_STOP

/**
 * Created by Hoa Nguyen on Oct 05 2022.
 */

@ExperimentalAnimationApi
@Composable
fun MainScreen(stopwatchService: StopwatchService) {
    val context = LocalContext.current
    val hours by stopwatchService.hours
    val minutes by stopwatchService.minutes
    val seconds by stopwatchService.seconds
    val currentState by stopwatchService.currentState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = Modifier
                .weight(weight = 9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = hours, transitionSpec = { addAnimation() }) {
                Text(
                    text = hours,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (hours == "00") Color.White else Blue
                    )
                )
            }

            AnimatedContent(targetState = minutes, transitionSpec = { addAnimation() }) {
                Text(
                    text = minutes,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (minutes == "00") Color.White else Blue
                    )
                )
            }

            AnimatedContent(targetState = seconds, transitionSpec = { addAnimation() }) {
                Text(
                    text = seconds,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h1.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (seconds == "00") Color.White else Blue
                    )
                )
            }
        }

        Row(Modifier.weight(weight = 1f)) {
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                        else ACTION_SERVICE_START
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = if (currentState == StopwatchState.Started) Red else Blue
                )
            ) {
                Text(
                    text = when (currentState) {
                        StopwatchState.Started -> "Stop"
                        StopwatchState.Stopped -> "Resume"
                        else -> "Start"
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_CANCEL
                    )
                },
                enabled = seconds != "00" && currentState != StopwatchState.Started,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Light,
                    contentColor = if (seconds != "00" && currentState != StopwatchState.Started) White else Gray
                )
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun EmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading"
        )
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

