package com.example.mainproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainproject.model.Todo
import com.example.mainproject.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository: TodoRepository): ViewModel() {
    var _todoList =
        mutableStateOf<List<Todo>>(listOf())
    var clickedTask = mutableStateOf<Todo?>(null)
    val allTemples: LiveData<List<Todo>> = todoRepository.allTaks
    var isSeachListEmpty = mutableStateOf<Boolean?>(null)
    var isListEmpty = mutableStateOf<Boolean?>(value = null)
    fun fetchTodos() {
        viewModelScope.launch {
            _todoList.value = todoRepository.getAllTasks()!!
            isListEmpty.value = _todoList.value.isEmpty()
        }
    }

    fun deleteTodo(todo: Todo) {
        todoRepository.deleteTodo(todo)
        _todoList.value = _todoList.value.minus(todo)
        isListEmpty.value = _todoList.value.isEmpty()
    }
    fun deleteAllRows() {
        todoRepository.deleteAllRow()
    }

    fun saveTask(todo: Todo) {
        viewModelScope.launch(Dispatchers.Default) {
            todoRepository.addTodo(todo)
            isListEmpty.value = false
        }
    }
    fun updateTask(todo: Todo) = viewModelScope.launch(Dispatchers.IO) { todoRepository.updateTask(todo) }
    fun setClickedItem(todo: Todo?) {
        clickedTask.value = todo
    }

    fun searchList(query: String) {
        //listToSearch = _taskList.value
        viewModelScope.launch(Dispatchers.Default) {
            println("inside searchlist() **************************")
            if (query.isEmpty()) {
                _todoList.value = todoRepository.getAllTasks()!!
                isSeachListEmpty.value = false
                return@launch
            }
            val filteredList = todoRepository.getAllTasks()?.filter {
                it.title!!.contains(query.trim(), ignoreCase = true) || it.description!!.contains(
                    query.trim(),
                    ignoreCase = true
                )
            }
            isSeachListEmpty.value = filteredList?.isEmpty()
            if (filteredList != null) {
                _todoList.value = filteredList
            }
            // _taskList.toMutableStateList()

        }
    }
}