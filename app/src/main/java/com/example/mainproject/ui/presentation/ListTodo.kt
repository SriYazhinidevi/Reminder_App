package com.example.mainproject.ui.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mainproject.R
import com.example.mainproject.model.Todo
import com.example.mainproject.ui.theme.CustomGray
import com.example.mainproject.ui.theme.DarkGreen
import com.example.mainproject.ui.theme.PriorityHigh
import com.example.mainproject.ui.theme.PriorityHighTextColor
import com.example.mainproject.ui.theme.PriorityLow
import com.example.mainproject.ui.theme.PriorityLowTextColor
import com.example.mainproject.ui.theme.PriorityMedium
import com.example.mainproject.ui.theme.PriorityMediumTextColor
import com.example.mainproject.utils.cancelAlarm
import com.example.mainproject.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ListTodo(navController: NavController, viewModel: TodoViewModel) {
    val itemsList by viewModel.allTemples.observeAsState(emptyList())
    LaunchedEffect(key1 = true) {
        viewModel.fetchTodos()
    }
    var isSearchResultEmpty by remember { viewModel.isSeachListEmpty }
    val isListEmpty by remember { viewModel.isListEmpty }
//    Column(Modifier.verticalScroll(rememberScrollState())) {
//        itemsList.forEach { temple ->
//            Text(text = "${temple.title} - ${temple.description} ")
//        }
//    }
    ListItems(data = itemsList, viewModel = viewModel, navController = navController, isListEmpty = isListEmpty, isSearchResultEmpty = isSearchResultEmpty)
    FloatingActionButtons(navController,viewModel)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListItems(
    modifier: Modifier = Modifier,
    data: List<Todo>,
    viewModel: TodoViewModel,
    navController: NavController,
    isListEmpty: Boolean?,
    isSearchResultEmpty: Boolean?
) {
    val context = LocalContext.current
    val itemsToDelete = remember { mutableStateOf<Set<Todo>>(emptySet()) }
    Column(
        modifier = Modifier
            .fillMaxSize()  // This makes the background fill the entire screen
            .background(Color.White),  // Set the background color to white
    ) {
        if (isListEmpty != null && isListEmpty == false) {
            LazyColumn(
                modifier = modifier
                    .padding(vertical = 4.dp)
                    .testTag("list_items")
                    .background(Color.White)
            ) {
                itemsIndexed(items = data.toMutableList()) { index, n ->
                    n.title?.let {
                        n.description?.let { it1 ->
                            n.date?.let { it2 ->
                                n.time?.let { it3 ->
                                    n.priority?.let { it4 ->
                                        ComposeCard(
                                            name = it,
                                            type = it1,
                                            description = it2,
                                            date = n.date,
                                            time = it3,
                                            priority = it4,
                                            modifier = Modifier.testTag("composeCard_$index")
                                                .clickable {
                                                    viewModel.setClickedItem(n)
                                                    navController.navigate("Add_Todo")
                                                },
                                            navController = navController,
                                            data = n,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        AnimatedVisibility(isListEmpty != null && isListEmpty == true) {
            ShowEmptyListMessage()
        }
    }
}
@Composable
fun ShowEmptyListMessage() {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFD3E0DC), Color(0xFFE1F1DD)),
        startY = 0f,
        endY = 500f
    )
    Column(
        modifier = Modifier.fillMaxSize().background(brush = gradient),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(
                width = 1.dp,
                color = CustomGray
            ),

            ) {
            Icon(
                painterResource(id = R.drawable.vc_empty_list),
                "empty list icon",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                tint = Color(0xFF123456)
            )
            Text(
                "Oops! No Todo's Found. Please try to add new one.",
                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                fontSize = 24.sp,
                style = TextStyle.Default,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                color = Color(0xFF123456)
            )
        }
        println("inside list is empty")

    }
}

@Composable
fun ComposeCard(
    name: String,
    type: String,
    description: String,
    date: String,
    time: String,
    modifier: Modifier,
    priority: String,
    navController: NavController,
    data: Todo,
    viewModel: TodoViewModel
) {
    val dialogState = remember { mutableStateOf(false) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFD3E0DC), Color(0xFFE1F1DD)),
        startY = 0f,
        endY = 500f
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDCECE9)
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                dialogState.value = true
                navController.navigate("Add_Todo")
                viewModel.setClickedItem(data)
            },
    ) {
        CardContent(name, type, description,date,time, priority, viewModel,data)
    }

//    if (dialogState.value) {
//        ReminderDialog(name = name, onDismiss = { dialogState.value = false })
//    }
}

@Composable
fun CardContent(name: String, type: String, description: String, date: String, time: String, priority: String, viewModel: TodoViewModel, data: Todo) {
    val expanded = remember { mutableStateOf(false) }
    var year = ""
    var month = ""
    var day = ""
    var abbreviatedMonth = ""
    var hours = ""
    var min = ""
    val calendar = Calendar.getInstance()
    if (!date.isNullOrBlank() && !time.isNullOrBlank()) {
        val dateList = date.split("-").map {// (year, month, day)
            it.toInt()
        }
        year = dateList[0].toString()
        month = dateList[1].toString()
        day = dateList[2].toString()
        calendar.set(Calendar.MONTH, month.toInt() - 1)
        abbreviatedMonth = SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.time)
        println("abbbbbbbbbbbbbbbbbbbbbbbbriviared time is :  $abbreviatedMonth")
        calendar.set(Calendar.MONTH, month.toInt())
        println("the calendar is +++++++++++++++01           :   $calendar")
        val timeList = time.split(":").map {// (year, month, day)
            it.toInt()
        }
        if (timeList.isNotEmpty()) {
            hours = timeList[0].toString()
            min = timeList[1].toString()
            calendar.set(year.toInt(), month.toInt() - 1, day.toInt(), hours.toInt(), min.toInt())
        }
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = type, color = Color(0xFF123456))
            Text(text = "$name.",
                color = Color(0xFF123456),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )

            if (expanded.value) {
                // Some random text here.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (date != "") {
                        Icon(
                            modifier = Modifier.size(24.dp).padding(0.dp),
                            painter = painterResource(id = com.example.mainproject.R.drawable.vc_alarm),
                            contentDescription = "alarm icon",
                            tint = Color.Black
                        )
                    }
                    time?.let {
                        Text(
                            modifier = Modifier.padding(6.dp, 0.dp, 2.dp, 0.dp)
                                .align(Alignment.CenterVertically),
                            textAlign = TextAlign.End,
                            text = if (day != "") "$it | $abbreviatedMonth $day, $year" else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                            if (calendar.timeInMillis >= System.currentTimeMillis()) DarkGreen else Color.Red,
                            maxLines = 1,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        priority?.let {
                            if (priority != "NONE") {
                                // "Medium" or "High" tag
                                Button(
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.wrapContentWidth().height(20.dp)
                                        .defaultMinSize(
                                            minWidth = ButtonDefaults.MinWidth,
                                            minHeight = 1.dp
                                        ),
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (priority == "HIGH") PriorityHigh else if (priority == "LOW") PriorityLow else if (priority == "MEDIUM") PriorityMedium else Color.White,
                                        contentColor = Color.Black,
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                ) {
                                    Text(
                                        it.toLowerCase(),
                                        style = TextStyle(fontSize = 14.sp),
                                        modifier = Modifier.padding(0.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = when (priority) {
                                            "HIGH" -> PriorityHighTextColor
                                            "MEDIUM" -> PriorityMediumTextColor
                                            "LOW" -> PriorityLowTextColor
                                            else -> Color.White
                                        },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Delete icon
                        IconButton(onClick = {viewModel.deleteTodo(data)}) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                tint = Color.Red,
                                contentDescription = "Delete"
                            )
                        }
                }
                }
            }
        }

        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                painter = if (expanded.value) painterResource(id = com.example.mainproject.R.drawable.baseline_expand_less_24) else painterResource(id = com.example.mainproject.R.drawable.baseline_expand_more_24),
                contentDescription = if (expanded.value) {
                    "Show Less"
                } else {
                    "Show More"
                },
                tint = Color(0xFF123456)
            )
        }
    }
}