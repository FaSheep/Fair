package org.fasheep.fair.feature.vote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.DatePicker
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
internal fun VoteRoute(
    modifier: Modifier = Modifier,
    onNavHistory: (String) -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VoteScreen(
        modifier = modifier,
        uiState = uiState,
        onDismiss = viewModel::closeDialog,
        onCancel = viewModel::cancelCurrentJob,
        onCreateVoteClicked = { a, b -> viewModel.createVote(a, b, onNavHistory) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    modifier: Modifier = Modifier,
    uiState: VoteUiState,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onCreateVoteClicked: (Long, List<String>) -> Unit
) {
    // State variables
    var endTime by remember { mutableLongStateOf(0L) }
    val options = remember { mutableStateListOf("") }

    if (uiState is VoteUiState.Shown)
        AlertDialog(onDismissRequest = onDismiss,
            text = { Text(uiState.message) },
            confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } })
    if (uiState is VoteUiState.Loading)
        BasicAlertDialog(modifier = Modifier.fillMaxSize(), onDismissRequest = {
            onCancel()
            onDismiss()
        }) {
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                )
            }
        }
    // UI Components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextSwitch(
            isModeA = true,
            modeAText = "Create Vote",
            modeBText = "Cast Vote",
            activeBackgroundColor = MaterialTheme.colorScheme.primary,
            onModeChange = {}
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Date and Time Picker (Button)
        DateTimePicker(
            onDateTimeSelected = { timestamp ->
                endTime = timestamp
            },
            endTime
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display selected end time (Text)
        if (endTime > 0) {
            val formattedEndTime = remember(endTime) {
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(endTime))
            }
            Text(
                text = "End Time: $formattedEndTime"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }


        // Options Input
        OptionsInput(modifier = Modifier.weight(1f, false), options = options) { index, newText ->
            options[index] = newText
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add Option Button (with + icon)
        Button(onClick = { options.add("") }) {
            Icon(Icons.Default.Add, contentDescription = "Add Option")
            Text("Add Option")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Create Vote Button (enabled only when valid)
        Button(
            onClick = {
                val filteredOptions = options.filter { it.isNotBlank() }
                if (endTime > System.currentTimeMillis() && filteredOptions.size >= 2) {
                    onCreateVoteClicked(endTime, filteredOptions)
                }
            },
            enabled = endTime > System.currentTimeMillis() && options.filter { it.isNotBlank() }.size >= 2
        ) {
            Text("Create Vote")
        }
    }
}

@Composable
fun DateTimePicker(onDateTimeSelected: (Long) -> Unit, endTime: Long) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val timePickerDialog = TimePickerDialog(
                context,
                { _, selectedHour: Int, selectedMinute: Int ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                    onDateTimeSelected(selectedCalendar.timeInMillis)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        },
        year,
        month,
        day
    )
    //显示日期
    val showDate = remember {
        derivedStateOf {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = endTime
            if (endTime != 0L) {
                "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH)} ${
                    calendar.get(
                        Calendar.HOUR_OF_DAY
                    )
                }:${calendar.get(Calendar.MINUTE)}"
            } else {
                "Select End Date and Time"
            }
        }
    }

    Button(onClick = { datePickerDialog.show() }) {
        Text(showDate.value)
    }
}

@Composable
fun OptionsInput(
    modifier: Modifier = Modifier,
    options: MutableList<String>,
    onOptionTextChanged: (Int, String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(modifier = modifier) {
        itemsIndexed(options) { index, option ->
            OutlinedTextField(
                value = option,
                onValueChange = {
                    onOptionTextChanged(index, it)
                },
                label = { Text("Option ${index + 1}") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (index < options.size - 1) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                trailingIcon = {
                    if (options.size > 1) {
                        IconButton(onClick = { options.removeAt(index) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Remove Option")
                        }
                    }

                }

            )
        }

    }
}


@Composable
fun TextSwitch(
    isModeA: Boolean,
    onModeChange: (Boolean) -> Unit,
    modeAText: String = "Mode A",
    modeBText: String = "Mode B",
    activeBackgroundColor: Color = Color(0xFFB5E8B7), // 激活状态背景色
    inactiveBackgroundColor: Color = Color.LightGray, // 非激活状态背景色
    textColor: Color = Color.White // 文本颜色
) {
    // 使用动画平滑过渡背景颜色
    val backgroundColor by animateColorAsState(
        targetValue = if (isModeA) activeBackgroundColor else inactiveBackgroundColor,
        animationSpec = tween(durationMillis = 300)
    )
    val width = 220.dp
    val height = 50.dp
    // 使用动画平滑移动选择器
    val selectorOffset by animateDpAsState(
        targetValue = if (isModeA) 0.dp else width / 2, // 假设每个模式文本宽度为90dp
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .width(width) // 总宽度
            .height(height)
            .clip(RoundedCornerShape(height / 2)) // 圆角
            .background(backgroundColor)
            .clickable { onModeChange(!isModeA) } // 点击切换模式
    ) {
        // 移动的选择器
        Box(
            modifier = Modifier
                .offset(x = selectorOffset + 5.dp, y = 5.dp)
                .width(width / 2 - 10.dp)
                .height(height - 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White) //选择器颜色

        )


        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween, // 均匀分布
            verticalAlignment = Alignment.CenterVertically
        ) {

            ModeText(
                text = modeAText,
                isSelected = isModeA,
                textColor = if (isModeA) activeBackgroundColor else textColor, //根据选中状态改变文字颜色
                modifier = Modifier.weight(1f) // 平分宽度
            )

            ModeText(
                text = modeBText,
                isSelected = !isModeA,
                textColor = if (!isModeA) activeBackgroundColor else textColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ModeText(text: String, isSelected: Boolean, textColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxHeight(),// 填充父组件高度
        contentAlignment = Alignment.Center // 文字居中
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun PreviewVoteScreen() {
    Surface {
        VoteScreen(uiState = VoteUiState.Empty, onCancel = {}, onDismiss = {}) { _, _ -> }
    }
}

@Preview
@Composable
fun TextSwitchPreview() {
    var isModeA by remember { mutableStateOf(true) }
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            TextSwitch(
                isModeA = isModeA,
                onModeChange = { isModeA = it }
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextSwitch(
                isModeA = !isModeA,
                onModeChange = { isModeA = !it },
                activeBackgroundColor = Color.Blue,
                modeAText = "On",
                modeBText = "Off"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = if (isModeA) "Mode A" else "Mode B")
        }
    }
}
