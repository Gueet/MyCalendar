package ru.university.taskcalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Временный список задач (пока в коде)
        val taskList = listOf(
            Task(1, "Купить учебник по Kotlin", "Найти хорошую книгу", "01.10.2026"),
            Task(2, "Пойти в спортзал", "Тренировка ног", "02.10.2026"),
            Task(3, "Сделать домашку по Android", "Этап 2", "03.10.2026", isDone = true),
            Task(4, "Заказать обед", "Что-нибудь вкусное", "04.10.2026"),
            Task(5, "Позвонить маме", "Узнать как дела", "05.10.2026")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = TaskAdapter(taskList) { task ->
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("task_id", task.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.addTaskFab)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }
}