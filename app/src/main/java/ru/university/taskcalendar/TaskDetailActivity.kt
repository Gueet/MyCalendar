package ru.university.taskcalendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val taskId = intent.getIntExtra("task_id", -1)
        val textView = findViewById<TextView>(R.id.detailText)
        textView.text = "Открыта задача с id = $taskId"
    }
}