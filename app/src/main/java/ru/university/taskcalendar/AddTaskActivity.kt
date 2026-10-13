package ru.university.taskcalendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(AppDatabase.getDatabase(applicationContext).taskDao())
        )
    }

    private var editingTaskId: Int = -1
    private var existingTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val formTitle = findViewById<android.widget.TextView>(R.id.formTitle)
        val titleLayout = findViewById<TextInputLayout>(R.id.titleLayout)
        val titleInput = findViewById<TextInputEditText>(R.id.titleInput)
        val descriptionInput = findViewById<TextInputEditText>(R.id.descriptionInput)
        val dateInput = findViewById<TextInputEditText>(R.id.dateInput)
        val timeInput = findViewById<TextInputEditText>(R.id.timeInput)
        val saveButton = findViewById<MaterialButton>(R.id.saveButton)

        editingTaskId = intent.getIntExtra("task_id", -1)

        if (editingTaskId != -1) {
            formTitle.text = "Редактирование задачи"

            val repository = TaskRepository(AppDatabase.getDatabase(applicationContext).taskDao())
            lifecycleScope.launch {
                existingTask = repository.getTaskById(editingTaskId)

                existingTask?.let { task ->
                    titleInput.setText(task.title)
                    descriptionInput.setText(task.description)
                    dateInput.setText(task.date)
                    timeInput.setText(task.time)
                }
            }
        } else {
            formTitle.text = "Новая задача"
        }

        dateInput.setOnClickListener { showDatePicker(dateInput) }
        timeInput.setOnClickListener { showTimePicker(timeInput) }

        saveButton.setOnClickListener {
            val title = titleInput.text?.toString()?.trim() ?: ""
            val description = descriptionInput.text?.toString()?.trim() ?: ""
            val date = dateInput.text?.toString()?.trim() ?: ""
            val time = timeInput.text?.toString()?.trim() ?: ""

            if (title.isEmpty()) {
                titleLayout.error = "Введите название задачи"
                return@setOnClickListener
            }
            titleLayout.error = null // Убираем ошибку, если есть

            if (date.isEmpty()) {
                Toast.makeText(this, "Выберите дату", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                id = if (editingTaskId != -1) editingTaskId else 0,
                title = title,
                description = description,
                date = date,
                time = time,
                isDone = existingTask?.isDone ?: false
            )

            if (editingTaskId != -1) {
                viewModel.updateTask(task)
                Toast.makeText(this, "Задача обновлена", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addTask(task)
                Toast.makeText(this, "Задача сохранена", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    private fun showDatePicker(dateInput: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d.%02d.%04d",
                    selectedDay, selectedMonth + 1, selectedYear
                )
                dateInput.setText(formattedDate)
            },
            year, month, day
        ).show()
    }

    private fun showTimePicker(timeInput: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(formattedTime)
            },
            hour, minute,
            true
        ).show()
    }
}