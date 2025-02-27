package org.fasheep.fair.feature.vote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Text("Create a New Vote")
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

@Preview
@Composable
private fun PreviewVoteScreen() {
    Surface {
        VoteScreen(uiState = VoteUiState.Empty, onCancel = {}, onDismiss = {}) { _, _ -> }
    }
}
