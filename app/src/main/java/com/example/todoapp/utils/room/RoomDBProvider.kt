package com.example.todoapp.utils.room

import android.content.Context

object RoomDBProvider {

    private var roomDB: RoomDB? = null

    fun getInstance(context: Context): RoomDB {
        if (roomDB == null) {
            roomDB = com.example.todoapp.utils.room.RoomDB.getInstance(context)
        }

        return roomDB!!
    }


}