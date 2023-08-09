package com.example.todoapp.utils.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.utils.room.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("select * from task")
    fun getTasks(): Flow<List<Task>>

    @Delete
    suspend fun deleteTask(removeNote: Task)

    @Query("select  * from task where id=:id")
    suspend fun getTaskById(id: Int): Task

    @Query("update task set image=:image where id=:id")
    suspend fun updateImage(image: String,id:Int)


}