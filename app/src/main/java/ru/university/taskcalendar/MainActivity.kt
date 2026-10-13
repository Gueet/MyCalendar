package ru.university.taskcalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper

class MainActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(AppDatabase.getDatabase(applicationContext).taskDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = TaskAdapter(emptyList()) { task ->
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("task_id", task.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allTasks.collect { tasks ->
                adapter.updateTasks(tasks)
            }
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, // не поддерживаем перетаскивание
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // свайп в обе стороны
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskToDelete = adapter.getTaskAt(position)

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Удалить задачу?")
                    .setMessage("«${taskToDelete.title}» будет удалена")
                    .setPositiveButton("Удалить") { _, _ ->
                        viewModel.deleteTask(taskToDelete)
                        Toast.makeText(
                            this@MainActivity,
                            "Задача удалена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Отмена") { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab = findViewById<FloatingActionButton>(R.id.addTaskFab)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }
}