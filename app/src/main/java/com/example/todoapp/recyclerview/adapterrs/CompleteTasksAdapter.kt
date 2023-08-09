package com.example.todoapp.recyclerview.adapterrs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.utils.room.models.Task

class CompleteTasksAdapter : ListAdapter<Task, CompleteTasksAdapter.VH>(MyDiffUtil) {

    private lateinit var onClickListener: OnClickListener

    fun setOnclickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }


    companion object MyDiffUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_layout_tasks,
            parent,
            false
        )

        return VH(view)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {

        val task = getItem(position)

        holder.txtTitle.text = task.title
        holder.txtDescription.text = task.description
        holder.chkIsDone.isChecked = task.isDone

        val color = when (task.color) {
            "red" -> android.graphics.Color.RED
            "green" -> android.graphics.Color.GREEN
            "blue" -> android.graphics.Color.BLUE
            "yellow" -> android.graphics.Color.YELLOW

            else ->  android.graphics.Color.WHITE
        }

        holder.taskColor.setBackgroundColor(color)

        holder.btnDelete.setOnClickListener {
            onClickListener.onClickRemove(holder.adapterPosition)
        }

        holder.chkIsDone.setOnCheckedChangeListener { _, status ->
            onClickListener.onCheckChange(holder.adapterPosition, status)
        }

        holder.itemView.setOnClickListener { onClickListener.onClick(holder.adapterPosition) }

    }


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var txtDescription: TextView
        var chkIsDone: CheckBox
        var btnDelete: ImageButton
        var taskColor: View

        init {
            this.chkIsDone = itemView.findViewById(R.id.chk_done)
            this.btnDelete = itemView.findViewById(R.id.btn_delete)
            this.txtTitle = itemView.findViewById(R.id.txt_title)
            this.txtDescription = itemView.findViewById(R.id.txt_description)
            this.taskColor = itemView.findViewById(R.id.task_color)

        }

    }
}
