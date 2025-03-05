package org.fasheep.fair.feature.assignment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun AssignmentRoute(
    modifier: Modifier = Modifier,
    callback: (String) -> Unit,
    viewModel: AssignmentViewModel = hiltViewModel()
) {
    AssignmentScreen(
        modifier = modifier,
        roleList = viewModel.roles,
        nameList = viewModel.names,
        onAssign = viewModel::assign,
        addName = viewModel::addName,
        addRole = viewModel::addRole,
        deleteName = viewModel::deleteName,
        deleteRole = viewModel::deleteRole,
        callback = callback
    )
}

@Composable
internal fun AssignmentScreen(
    modifier: Modifier = Modifier,
    roleList: List<RoleVM>,
    nameList: List<String>,
    onAssign: ((String) -> Unit) -> Unit,
    addName: (String) -> Boolean,
    addRole: (RoleVM) -> Boolean,
    deleteName: (String) -> Unit,
    deleteRole: (String) -> Unit,
    callback: (String) -> Unit
) {
    val sum = roleList.sumOf { it.num }
    var showAddNameDialog by remember { mutableStateOf(false) }
    var showAddRoleDialog by remember { mutableStateOf(false) }
    if (showAddRoleDialog) AddRoleDialog(onConfirm = { name, num ->
        if (num.isDigitsOnly()) {
            addRole(RoleVM(name, num.toInt()))
        } else {
            false
        }
    }, onDismiss = { showAddRoleDialog = false })
    if (showAddNameDialog) AddNameDialog(onConfirm = {
        addName(it)
    }, onDismiss = { showAddNameDialog = false })

    Box {
        Surface {
            Row {
                LazyColumn(Modifier.weight(0.4f), contentPadding = PaddingValues(5.dp)) {
                    items(nameList) { name ->
                        NameCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            name = name,
                            onDeleteClick = { deleteName(name) }
                        )
                    }
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                            onClick = { showAddNameDialog = true }) { Text("Add a people") }
                    }
                }
                Spacer(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(color = Color.Black)
                )
                LazyColumn(Modifier.weight(0.6f), contentPadding = PaddingValues(5.dp)) {
                    items(roleList) { role ->
                        RoleCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            name = role.name,
                            number = role.num,
                            percentage = role.num.toFloat() / sum,
                            onDeleteClick = { deleteRole(role.name) }
                        )
                    }
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                            onClick = { showAddRoleDialog = true }) { Text("Add a role") }
                    }
                }
            }
            // TODO FAB Menu & Animation
            FloatingActionButton(
                onClick = { onAssign(callback) }, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "")
            }
        }
    }
}

@Composable
fun AddRoleDialog(
    onConfirm: (roleName: String, num: String) -> Boolean,
    onDismiss: () -> Unit
) {
    var roleName by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("1") }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 5.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Add role"
                )
                TextField(
                    value = roleName,
                    onValueChange = { roleName = it },
                    label = { Text("Role Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
                TextField(
                    value = num,
                    onValueChange = { if (it.isDigitsOnly()) num = it },
                    label = { Text("Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
                Row(modifier = Modifier.align(alignment = Alignment.End)) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (onConfirm(roleName, num)) onDismiss()
                    }) {
                        Text("OK")
                    }
                }

            }
        }
    }
}


@Composable
fun AddNameDialog(
    onConfirm: (roleName: String) -> Boolean,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 5.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Add name"
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
                Row(modifier = Modifier.align(alignment = Alignment.End)) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (onConfirm(name)) onDismiss()
                    }) {
                        Text("OK")
                    }
                }

            }
        }
    }
}

@Composable
private fun NameCard(modifier: Modifier = Modifier, name: String, onDeleteClick: () -> Unit) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
private fun RoleCard(
    modifier: Modifier = Modifier,
    name: String,
    number: Int,
    percentage: Float,
    onDeleteClick: () -> Unit
) {
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(text = name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "$number")
                }
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth(),
                    trackColor = Color.LightGray,
                    gapSize = (-1).dp,
                    drawStopIndicator = {}
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
fun Preview() {
    AssignmentScreen(
        modifier = Modifier,
        roleList = listOf(RoleVM("A", 1), RoleVM("C", 2), RoleVM("B", 1)),
        nameList = listOf("111", "2222", "3333", "444"),
        onAssign = {},
        addName = { false },
        addRole = { false },
        deleteName = {},
        deleteRole = {},
        callback = {}
    )
}

@Preview(apiLevel = 34)
@Composable
fun AddRoleDialogPreview() {
    AddRoleDialog(onDismiss = {}, onConfirm = { _, _ -> false })
}

@Preview(apiLevel = 34)
@Composable
fun AddNameDialogPreview() {
    AddNameDialog(onDismiss = {}, onConfirm = { _ -> false })
}

@Preview(apiLevel = 34)
@Composable
fun RoleCardPreview() {
    Surface {
        RoleCard(modifier = Modifier.width(300.dp), name = "AAA", number = 3, percentage = 0.3f, onDeleteClick = {})
    }
}

@Preview(apiLevel = 34)
@Composable
fun NameCardPreview() {
    Surface {
        NameCard(name = "Ben", onDeleteClick = {})
    }
}
