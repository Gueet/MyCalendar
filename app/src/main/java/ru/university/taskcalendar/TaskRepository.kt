package ru.university.taskcalendar

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksByDate(date: String): Flow<List<Task>> = taskDao.getTasksByDate(date)

    suspend fun insert(task: Task): Long = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id)
}