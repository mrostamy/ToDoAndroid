package com.example.todoapp.pages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.recyclerview.adapterrs.NewTasksAdapter
import com.example.todoapp.recyclerview.adapterrs.OnClickListener
import com.example.todoapp.utils.SqliteHandler
import com.example.todoapp.utils.Utils
import com.example.todoapp.utils.room.RoomDB
import com.example.todoapp.utils.room.models.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class TasksFragment : Fragment() {

    private lateinit var rvTasks: RecyclerView
    private lateinit var rvTasksAdapter: NewTasksAdapter
    private lateinit var tasks: MutableList<Task>
    private lateinit var db: SqliteHandler
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var sqliteHandler: SqliteHandler

    private val roomDatabase by lazy { RoomDB.getInstance(requireContext()).getTaskDao() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        observerTasks()

        print()


        fab = view.findViewById(R.id.fab_add)

        fab.setOnClickListener {
            val intent = Intent(requireContext(), NewTaskActivity::class.java)
            startActivity(intent)
        }

        rvTasks = view.findViewById(R.id.rv_tasks)

        rvTasksAdapter = NewTasksAdapter()

        rvTasks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        rvTasksAdapter.setOnclickListener(object : OnClickListener {
            override fun onClick(position: Int) {

                Utils.makeMessage(requireContext(), "position click item: $position", "$position")

                val i = Intent(requireContext(), NewTaskActivity::class.java)
                i.putExtra("id", rvTasksAdapter.currentList.toMutableList()[position].id)
//                i.putExtra("title", rvTasksAdapter.currentList.toMutableList()[position].title)
//                i.putExtra("description", rvTasksAdapter.currentList.toMutableList()[position].id)
//                i.putExtra("id", rvTasksAdapter.currentList.toMutableList()[position].id)
//                i.putExtra("id", rvTasksAdapter.currentList.toMutableList()[position].id)
//                i.putExtra("id", rvTasksAdapter.currentList.toMutableList()[position].id)
                startActivity(i)


            }

            override fun onClickRemove(position: Int) {

                Utils.makeMessage(requireContext(), "position delete item: $position", "$position")

                val taskList = rvTasksAdapter.currentList.toMutableList()

                val id = taskList[position].id
                val title = taskList[position].title

                taskList.removeAt(position)

                val removeNote = Task(id, title)

                rvTasksAdapter.submitList(taskList)

                lifecycleScope.launch {
                    roomDatabase.deleteTask(removeNote)
                }

            }

            override fun onCheckChange(position: Int, state: Boolean) {

                Utils.makeMessage(requireContext(), "Check State is:  $state", "$state")

                val tasks = rvTasksAdapter.currentList.toMutableList()

                tasks[position].isDone = state

                val id = tasks[position].id
                val title = tasks[position].title
                val description = tasks[position].description
                val color = tasks[position].color
                val image = tasks[position].image

                val taskTmp = Task(id, title, description, color, image, state)

                rvTasksAdapter.submitList(tasks)

                lifecycleScope.launch {
                    roomDatabase.updateTask(taskTmp)
                    rvTasksAdapter.submitList(tasks)
                }

            }

        })

        rvTasks.adapter = rvTasksAdapter


        return view
    }

    private fun observerTasks() {
        println("observerTasks")
        lifecycleScope.launch {

            roomDatabase.getTasks().collect { it ->
                if (it.isNotEmpty()) {
                    tasks = it as MutableList<Task>
                    val tmp = tasks.filter {
                        !it.isDone
                    }
                    rvTasksAdapter.submitList(tmp)
                }
            }
        }
    }

    private fun print() {
        lifecycleScope.launch {
            roomDatabase.getTasks().collect {
                if (it.isNotEmpty()) {
                    for (task in it) {

                        println("ID: ${task.id}")
                        println("title: ${task.title}")
                        println("description: ${task.description}")
                        println("isDone: ${task.isDone}")
                        println("color: ${task.color}")
                        println("image: ${task.image}")
                        println("****************************")


                    }
                }
            }
        }
    }
}