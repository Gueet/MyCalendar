package ru.university.taskcalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Получаем базу данных и репозиторий
        val db = AppDatabase.getDatabase(this)
        val repository = TaskRepository(db.taskDao())

        // 2. Находим RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Создаём адаптер с пустым списком (данные придут из базы)
        val adapter = TaskAdapter(emptyList()) { task ->
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("task_id", task.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // 4. Подписываемся на данные из базы — при изменениях список обновляется сам
        lifecycleScope.launch {
            repository.getAllTasks().collect { tasks ->
                adapter.updateTasks(tasks)
            }
        }

        // 5. Кнопка "+"
        val fab = findViewById<FloatingActionButton>(R.id.addTaskFab)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }
}