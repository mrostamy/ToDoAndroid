package com.example.todoapp.recyclerview.adapterrs


interface OnClickListener {
    fun onClick(position: Int)
    fun onClickRemove(position: Int)
    fun onCheckChange(position: Int,state:Boolean)
}