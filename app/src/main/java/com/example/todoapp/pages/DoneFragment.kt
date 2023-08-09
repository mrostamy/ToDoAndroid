package com.example.todoapp.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.recyclerview.adapterrs.CompleteTasksAdapter
import com.example.todoapp.recyclerview.adapterrs.OnClickListener
import com.example.todoapp.utils.SqliteHandler
import com.example.todoapp.utils.Utils
import com.example.todoapp.utils.room.RoomDB
import com.example.todoapp.utils.room.models.Task
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class DoneFragment : Fragment() {

    private lateinit var rvDone: RecyclerView
    private lateinit var rvDoneAdapter: CompleteTasksAdapter
    private lateinit var completedTasks: MutableList<Task>
    private lateinit var db: SqliteHandler

    private lateinit var sqliteHandler: SqliteHandler

    private val roomDatabase by lazy { RoomDB.getInstance(requireContext()).getTaskDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_done, container, false)

        observerTasks()


        rvDone = view.findViewById(R.id.rv_done)


        rvDoneAdapter = CompleteTasksAdapter()
        rvDone.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvDone.adapter = rvDoneAdapter

        rvDoneAdapter.setOnclickListener(object : OnClickListener {
            override fun onClick(position: Int) {


            }

            override fun onClickRemove(position: Int) {

                val tasks = rvDoneAdapter.currentList.toMutableList()
                val title = tasks[position].title
                val id = tasks[position].id

                tasks.removeAt(position)


                val task = Task(id, title)

                rvDoneAdapter.submitList(tasks)

                lifecycleScope.launch {
                    roomDatabase.deleteTask(task)
                }
            }


            override fun onCheckChange(position: Int, state: Boolean) {

                Utils.makeMessage(requireContext(), "Check State is:  $state", "$state")

                val tasks = rvDoneAdapter.currentList.toMutableList()

                tasks[position].isDone = state

                val id = tasks[position].id
                val title = tasks[position].title
                val description = tasks[position].description
                val color = tasks[position].color
                val image = tasks[position].image

                val taskTmp = Task(id, title, description, color, image, state)

                rvDoneAdapter.submitList(tasks)

                lifecycleScope.launch {
                    roomDatabase.updateTask(taskTmp)
                    rvDoneAdapter.submitList(tasks)
                }


            }

        })

        return view
    }

    private fun observerTasks() {
        println("observerTasks")
        lifecycleScope.launch {

            roomDatabase.getTasks().collect { it ->
                if (it.isNotEmpty()) {
                    completedTasks = it as MutableList<Task>
                    val tmp = completedTasks.filter {
                        it.isDone
                    }
                    rvDoneAdapter.submitList(tmp)
                }
            }
        }
    }

}
