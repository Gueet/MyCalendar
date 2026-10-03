package ru.university.taskcalendar

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    // Получить все задачи — Flow, обновляется автоматически
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    // Получить задачи на конкретную дату
    fun getTasksByDate(date: String): Flow<List<Task>> = taskDao.getTasksByDate(date)

    // Добавить задачу
    suspend fun insert(task: Task): Long = taskDao.insert(task)

    // Обновить задачу
    suspend fun update(task: Task) = taskDao.update(task)

    // Удалить задачу
    suspend fun delete(task: Task) = taskDao.delete(task)
}