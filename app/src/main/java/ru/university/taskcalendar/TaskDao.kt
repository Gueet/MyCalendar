package ru.university.taskcalendar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Добавить новую задачу. Room сам вернёт id вставленной строки
    @Insert
    suspend fun insert(task: Task): Long

    // Обновить существующую задачу (по id)
    @Update
    suspend fun update(task: Task)

    // Удалить задачу
    @Delete
    suspend fun delete(task: Task)

    // Получить все задачи, отсортированные по дате
    @Query("SELECT * FROM tasks ORDER BY date ASC, time ASC")
    fun getAllTasks(): Flow<List<Task>>

    // Получить задачи на конкретную дату
    @Query("SELECT * FROM tasks WHERE date = :selectedDate ORDER BY time ASC")
    fun getTasksByDate(selectedDate: String): Flow<List<Task>>
}