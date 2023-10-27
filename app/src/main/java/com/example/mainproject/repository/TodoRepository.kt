package com.example.mainproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mainproject.model.Todo
import com.example.mainproject.model.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoRepository @Inject constructor(private val db: TodoDatabase) {
//    val allTasks = MutableLiveData<List<Todo>>()
    val allTaks: LiveData<List<Todo>> = db.todoDao().getAllTodos();
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun addTodo(item: Todo) {
        coroutineScope.launch(Dispatchers.IO) {
            db.todoDao().insertTodo(item)
        }
    }
    fun deleteTodo(todo: Todo) = coroutineScope.launch(Dispatchers.IO) {
        db.todoDao().deleteTodo(todo = todo)
    }

    fun updateTask(todo: Todo) = coroutineScope.launch(Dispatchers.IO) { db.todoDao().update(todo) }

    fun deleteAllRow() = coroutineScope.launch(Dispatchers.IO) {
        db.todoDao().deleteAll()
    }
    suspend fun getAllTasks() : List<Todo>?{
        return db.todoDao().getAllTodo()
    }
}