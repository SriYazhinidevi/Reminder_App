package com.example.mainproject.ui.presentation

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mainproject.R
import com.example.mainproject.model.Todo
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.getUniqueId
import com.example.mainproject.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContent {
            MainProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    app(context = this, window)
                }
            }
        }
    }
}

@Composable
fun app(context: Context, window: Window) {
    val navController = rememberNavController()
    val todoListViewModel: TodoViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "Splash_screen"
    ) {
        composable("Add_Todo") {
            AddTodo(navController = navController,todoListViewModel)
        }
        composable("List_Todo") {
            ListTodo(navController = navController,todoListViewModel)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        composable("Splash_screen") {
            AnimatedSplashScreen(navController = navController) {
                navController.navigate("Add_Todo")
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    val todoListViewModel: TodoViewModel = hiltViewModel() // by viewModals() will also work
//
//    LaunchedEffect(key1 = true, block = {
//        todoListViewModel.fetchTodos()
//    })
//    Column(modifier = Modifier.height(500.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally) {
//        Button(
//            onClick = {
//
//                val todoValue = Todo(
//                    getUniqueId(),
//                    "Fourth Todo Title",
//                    "Fourth todo description",
//                    "12/10/2000",
//                    "19.20",
//                    false,
//                    "High"
//                )
//                todoListViewModel.saveTask(todoValue)
////            todoListViewModel.deleteAllRows()
//            },
//            modifier = Modifier.height(50.dp)
//        ) {
//            Text("Submit")
//        }
//        val itemsList by todoListViewModel.allTemples.observeAsState(emptyList())
//
//        Column(Modifier.verticalScroll(rememberScrollState())) {
//            itemsList.forEach { temple ->
//                Text(text = "${temple.title} - ${temple.description} ")
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainProjectTheme {
//        Greeting("Android")
    }
}