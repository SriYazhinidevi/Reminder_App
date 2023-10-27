package com.example.mainproject.ui.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.mainproject.R
import com.example.mainproject.model.Todo
import com.example.mainproject.ui.theme.Pink40
import com.example.mainproject.ui.theme.PriorityHighTextColor
import com.example.mainproject.ui.theme.PriorityLowTextColor
import com.example.mainproject.ui.theme.PriorityMediumTextColor
import com.example.mainproject.utils.Util.Companion.CHANNEL_ID
import com.example.mainproject.utils.Util.Companion.CHANNEL_NAME
import com.example.mainproject.utils.capitalized
import com.example.mainproject.utils.createChannel
import com.example.mainproject.utils.getUniqueId
import com.example.mainproject.viewmodel.TodoViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import okhttp3.internal.concurrent.Task

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodo(navController: NavController, todoViewModel: TodoViewModel) {
    val clickedTask = remember {todoViewModel.clickedTask}
    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }
    var isTextFieldEmpty by remember { mutableStateOf(false) }
    val rememberMe : MutableState<Boolean> = if(clickedTask.value == null || clickedTask.value?.date == "") remember { mutableStateOf(false) } else remember {  mutableStateOf(true)}
    val priority :MutableState<String?> =  if(clickedTask.value == null) remember { mutableStateOf("NONE") } else remember {  mutableStateOf(clickedTask.value?.priority)}
    val selectedDate :MutableState<String?> =  if(clickedTask.value == null)remember { mutableStateOf("")} else remember { mutableStateOf(clickedTask.value?.date)}
    val selectedTime :MutableState<String?> = if(clickedTask.value == null) remember { mutableStateOf("") } else remember {  mutableStateOf(clickedTask.value?.time)}

    //val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.", Locale.ENGLISH)
    val descriptionTextField :MutableState<String?> =
        if(clickedTask.value == null) remember { mutableStateOf("")} else remember {  mutableStateOf(clickedTask.value?.description) }
    val titleTextField :MutableState<String?> =
        if(clickedTask.value == null) remember { mutableStateOf("")} else remember {mutableStateOf(clickedTask.value?.title)}//mutableStateOf(TextFieldValue())
    val calendarState = rememberSheetState()
    CalendarDialog(state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date { date ->
            selectedDate.value = date.toString()
            println("DATE:  $date")
            println(selectedDate)
        })
    val clockState = rememberSheetState()
    ClockDialog(state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            println("TIME IN HOURS AND MINUTES: , $hours : $minutes")
            selectedTime.value = "$hours:$minutes"
            println(selectedTime)
        }
    )
    val title = if(clickedTask.value == null) "Add Todo" else "Update Todo"
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFD3E0DC), Color(0xFFE1F1DD)),
        startY = 0f,
        endY = 500f
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF123456),
                    titleContentColor = Color.White,
                ),
            )
        }, content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                titleTextField.value?.let { it1 ->
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp),
                        value = it1,
                        label = { Text(text = "Title") },
                        onValueChange = {
                            titleTextField.value = it
                            if (isTextFieldEmpty) {
                                isTextFieldEmpty = false
                            }
                        },
                        isError = isTextFieldEmpty,
                        placeholder = { Text("Enter the title") },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White, textColor = Color(0xFF123456))


                        /*           label = "Description",
                                               placeholder = "Not compulsory"*/
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                descriptionTextField.value?.let { it1 ->
                    SpecialOutlinedTextField(it1) {
                        descriptionTextField.value = it
                    }
                }
                DropDownMenu(priority)

                Spacer(modifier = Modifier.height(16.dp))



                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.Start).padding(10.dp, 0.dp)
                ) {
                    AddDateTimeCheckBox(
                        checked = rememberMe.value,
                        onCheckedChange = { rememberMe.value = it }
                    )
                }
                Column {
                    AnimatedVisibility(rememberMe.value) {
                        DateTimeTextField(
                            selectedDate = selectedDate,
                            selectedTime = selectedTime,
                            onDateClick = { calendarState.show() },
                            onTimeClick = { clockState.show() }
                        )
//                        Row(
//                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//                            Button(
//                                onClick = {
//                                    calendarState.show()
//                                }, modifier = Modifier,
//                                shape = RoundedCornerShape(5.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = if (selectedDate.value != "") Pink40 else Color.Gray,
//                                    contentColor = Color.White,
//                                )
//                            ) {
//                                Column {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.vc_time),
//                                        contentDescription = "null",
//                                        tint = Color.White,
//                                        modifier = Modifier.padding(0.dp, 12.dp)
//                                            .align(Alignment.CenterHorizontally)
//                                    )
//                                    Text(text = "Due Date")
//                                }
//
//                            }
//                            Button(
//                                onClick = {
//                                    clockState.show()
//                                },
//                                modifier = Modifier,
//                                shape = RoundedCornerShape(5.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = if (selectedTime.value != "") Pink40 else Color.Gray,
//                                    contentColor = Color.White,
//                                )
//                            ) {
//                                Column {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.vc_date),
//                                        contentDescription = "null",
//                                        tint = Color.White,
//                                        modifier = Modifier.padding(0.dp, 12.dp)
//                                            .align(Alignment.CenterHorizontally)
//                                    )
//                                    Text(text = "Due Time")
//                                }
//                            }
//                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val capitalize = descriptionTextField.value?.capitalized()
                            val capitalizedTitle = titleTextField.value?.capitalized()
                            //var date = LocalDate.parse(selectedDate) // "dd-MM-yyyy"
                            //val id = UUID.randomUUID().toString() unique id

                            if (titleTextField.value?.isEmpty() == true) {
                                isTextFieldEmpty = true
                                return@Button
                            } else {
                                isTextFieldEmpty = false
                            }
                            if (rememberMe.value) {
                                if (selectedDate.value != "" && selectedTime.value != "") {
                                    if(clickedTask.value != null){
                                        val aTask = Todo(
                                            clickedTask.value!!.id,
                                            capitalizedTitle?.trim(),
                                            capitalize?.trim(),
                                            selectedDate.value,
                                            selectedTime.value,
                                            false,
                                            priority.value,
                                            clickedTask.value!!.alarmId
                                        )
                                        todoViewModel.updateTask(todo = aTask)
                                        navController.navigate("List_Todo")
                                        return@Button
                                    }
                                    val aTask = Todo(
                                        getUniqueId(),
                                        capitalizedTitle?.trim(),
                                        capitalize?.trim(),
                                        selectedDate.value,
                                        selectedTime.value,
                                        false,
                                        priority.value
                                    )

                                    todoViewModel.saveTask(aTask)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // API 26
                                        val channelId = CHANNEL_ID
                                        val channelName = CHANNEL_NAME
                                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                                        val channel =
                                            NotificationChannel(
                                                channelId.toString(),
                                                channelName,
                                                importance
                                            )
                                        val notificationManager =
                                            context.getSystemService(NotificationManager::class.java)
                                        notificationManager.createNotificationChannel(channel)
                                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // min API level is 31
                                        }*/
                                        createChannel(channelId.toString(), context, aTask)
                                    }
                                    navController.navigate("List_Todo")
                                } else {
                                    //ToastMessage("Please Enter Date and Time or Uncheck.")
                                    Toast.makeText(
                                        context,
                                        "Please, consider to add a due date and time or uncheck!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    //showDialog.value = true
                                }
                            } else {
                                //save without due date
                                /*if(clickedTask.value?.alarmId){
                                    // if there is an alarm is set before and updated at cancel , cancel it
                                }*/

                                if(clickedTask.value != null){
                                    val task = Todo(
                                        clickedTask.value!!.id,
                                        capitalizedTitle?.trim(),
                                        capitalize?.trim(),
                                        "",
                                        "",
                                        false,
                                        priority.value,
                                        0
                                    )
                                    todoViewModel.updateTask(todo = task)
                                    navController.navigate("List_Todo")
                                    return@Button
                                }
                                val aTask = Todo(
                                    0,
                                    capitalizedTitle?.trim(),
                                    capitalize?.trim(),
                                    "",
                                    "",
                                    false,
                                    priority.value
                                )
                                todoViewModel.saveTask(aTask)
                                navController.navigate("List_Todo")
                            }
                        },
                        modifier = Modifier.height(60.dp).fillMaxWidth().padding(20.dp, 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF123456),
                            contentColor = Color(0xFF123456),
                        ),
                        shape = RoundedCornerShape(5.dp),
                        border = if (isTextFieldEmpty) BorderStroke(
                            2.dp,
                            Color.Red
                        ) else BorderStroke(0.dp, Color.Black)
                    ) {

//                        Icon(
//                            painter = painterResource(id = R.drawable.vc_done),
//                            contentDescription = "null",
//                            tint = Color.White,
//                        )
                        Text(
                            text = "SAVE",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .offset(x = (-12).dp) //default icon width = 24.dp
                        )
                    }
                }

            }
        })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialOutlinedTextField(string: String, function: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .padding(20.dp, 0.dp)
            .verticalScroll(rememberScrollState()),
        value = string,
        label = { Text(text = "Description") },
        onValueChange = function,
        placeholder = { Text("Enter the description") },
        maxLines = 5,
        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White, textColor = Color(0xFF123456))
        /*           label = "Description",
                   placeholder = "Not compulsory"*/
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(priority: MutableState<String?>) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("HIGH", "LOW", "MEDIUM", "NONE")

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    val priorityColor = when(priority.value){
        "HIGH" -> PriorityHighTextColor
        "MEDIUM" -> PriorityMediumTextColor
        "LOW" -> PriorityLowTextColor
        else -> Color.Black
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp)) {
        priority.value?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { priority.value = it },
                modifier = Modifier
                    .clickable {
                        Toast
                            .makeText(context, "lkdsajf", Toast.LENGTH_LONG)
                            .show()
                    }
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = "Back button",
                        tint = priorityColor
                    )
                },
                textStyle = TextStyle(color = priorityColor),
                readOnly = true,
                label = { Text("Priority") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Gray
                ),
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                },
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
                .background(Color.White)
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem({ Text(text = label, color = Color.Black) },
                    onClick = { priority.value = label
                  expanded = false })
            }
        }
    }
}
@Composable
fun AddDateTimeCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Checkbox(

            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 2.dp),
        )
        Text(
            text = "Remember me",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeTextField(
    selectedDate: MutableState<String?>,
    selectedTime: MutableState<String?>,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp),
    ) {
        OutlinedTextField(
            value = selectedDate.value ?: "",
            onValueChange = { selectedDate.value = it },
            label = { Text("Due Date") },
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .clickable { onDateClick() },
            textStyle = TextStyle(color = Color(0xFF123456)),
            trailingIcon = {
                IconButton(
                    onClick = { onDateClick() },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vc_time),
                        contentDescription = "Date Icon",
                        tint = Color(0xFF123456)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedTextField(
            value = selectedTime.value ?: "",
            onValueChange = { selectedTime.value = it },
            label = { Text("Due Time") },
            textStyle = TextStyle(color = Color(0xFF123456)),
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .clickable { onTimeClick() },
            trailingIcon = {
                IconButton(
                    onClick = { onTimeClick() },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vc_date),
                        contentDescription = "Time Icon",
                        tint = Color(0xFF123456)
                    )
                }
            }
        )
    }
}
