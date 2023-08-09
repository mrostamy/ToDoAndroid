package com.example.todoapp.utils.room.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id", "title"], unique = true)])
class Task {
    constructor(id: Int, title: String) {
        this.id = id
        this.title = title
    }

    constructor(id: Int, isDone: Boolean) {
        this.id = id
        this.isDone = isDone
    }

    constructor()

    constructor(
        id: Int, title: String, description: String, color: String, image: String, isDone: Boolean
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.color = color
        this.image = image
        this.isDone = isDone
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var title: String = ""
    var description: String = ""
    var color: String = ""
    var image: String = ""
    var isDone: Boolean = false
}


