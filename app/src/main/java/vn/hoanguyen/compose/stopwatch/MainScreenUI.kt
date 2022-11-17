package vn.hoanguyen.compose.stopwatch

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import vn.hoanguyen.compose.stopwatch.service.ServiceHelper
import vn.hoanguyen.compose.stopwatch.service.StopwatchState
import vn.hoanguyen.compose.stopwatch.ui.theme.ColorBackground
import vn.hoanguyen.compose.stopwatch.ui.theme.ColorButtonAction
import vn.hoanguyen.compose.stopwatch.ui.theme.Light
import vn.hoanguyen.compose.stopwatch.ui.theme.Red
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_CANCEL
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_START
import vn.hoanguyen.compose.stopwatch.util.Constants.ACTION_SERVICE_STOP
import vn.hoanguyen.compose.stopwatch.viewmodel.StopwatchStateManagement

/**
 * Created by Hoa Nguyen on Oct 05 2022.
 */

@OptIn(ExperimentalUnitApi::class)
@ExperimentalAnimationApi
@Composable
@Preview
fun MainScreenUI(stopwatchStateManagement: StopwatchStateManagement = StopwatchStateManagement()) {
    val context = LocalContext.current
    val hours by stopwatchStateManagement.hours
    val minutes by stopwatchStateManagement.minutes
    val seconds by stopwatchStateManagement.seconds
    val currentState by stopwatchStateManagement.currentState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 32.dp,
                bottom = 56.dp,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Column(
            modifier = Modifier
                .weight(weight = 9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ItemNumber(
                modifier = Modifier.weight(1f),
                value = hours,
                title = " HR",
                borderColor = Color(0xFFC999B3)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ItemNumber(
                modifier = Modifier.weight(1f),
                value = minutes,
                title = "MIN",
                borderColor = Color(0xFF73A3A6)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ItemNumber(
                modifier = Modifier.weight(1f),
                value = seconds,
                title = "SEC",
                borderColor = Color(0xFF9AAAAF)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            Modifier.weight(weight = 2f),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Button(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight(0.5f),
                    onClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                            else ACTION_SERVICE_START
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        backgroundColor = if (currentState == StopwatchState.Started) Red else ColorButtonAction
                    ),
                ) {
                    Text(
                        text = when (currentState) {
                            StopwatchState.Started -> "Stop"
                            StopwatchState.Stopped -> "Resume"
                            else -> "Start"
                        },
                        style = TextStyle(color = White),
                        fontSize = TextUnit(value = 32f, TextUnitType.Sp),
                        fontFamily = FontFamily(Font(R.font.panton_black_caps))
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val isActive = seconds != "00" && currentState != StopwatchState.Started
                Button(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(weight = 1f)
                        .fillMaxHeight(0.5f),
                    onClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context, action = ACTION_SERVICE_CANCEL
                        )
                    },
                    enabled = seconds != "00" && currentState != StopwatchState.Started,
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = Light,
                    )
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(color = if (isActive) White else DarkGray),
                        fontSize = TextUnit(value = 32f, TextUnitType.Sp),
                        fontFamily = FontFamily(Font(R.font.panton_black_caps))
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ItemNumber(modifier: Modifier, value: String, title: String, borderColor: Color) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title:",
            style = TextStyle(
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                color = White,
                fontFamily = FontFamily(Font(R.font.panton_black_caps))
            ),
            modifier = Modifier
                .padding(16.dp),
        )
        Card(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f, matchHeightConstraintsFirst = true),

            shape = RoundedCornerShape(corner = CornerSize(size = 16.dp)),
            border = BorderStroke(color = borderColor, width = 2.dp),
            backgroundColor = ColorBackground,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(targetState = value, transitionSpec = { addAnimation2() }) {
                    Text(
                        text = value,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.h1.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = if (value == "00") Color.LightGray else White,
                            fontFamily = FontFamily(Font(R.font.panton_black_caps))
                        ),
                        modifier = Modifier
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation2(duration: Int = 300): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

