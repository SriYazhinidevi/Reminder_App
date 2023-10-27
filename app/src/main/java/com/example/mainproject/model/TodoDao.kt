package com.example.mainproject.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodo(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo")
    suspend fun getAllTodo(): List<Todo>

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    @Update
    suspend fun update(todo: Todo)
}