package com.example.mainproject.ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mainproject.R
import com.example.mainproject.viewmodel.TodoViewModel

@Composable
fun FloatingActionButtons(navController: NavController, viewModel: TodoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(14.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = {
                //mContext.startActivity(Intent(mContext, DateTimePicker::class.java))
                viewModel.setClickedItem(null)
                navController.navigate("Add_Todo")
            },
            shape = CircleShape,
            containerColor = Color(0xFF123456),
            contentColor = Color.White
        ) {
            Icon(
                painterResource(R.drawable.baseline_timer_24),
                modifier = Modifier.size(24.dp),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}