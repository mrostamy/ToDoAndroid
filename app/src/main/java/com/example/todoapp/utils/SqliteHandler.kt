package com.example.todoapp.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqliteHandler(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(context, "DB_ToDo", factory, 1) {

    private var tbl = "tasks"
    private val sqlCreateQuery: String =
        "create table if not exists $tbl (id INTEGER primary key not null " +
                ",title text default \" \"  " +
                ",description text,color text default \" \" " +
                ",imagePath text default \" \" " +
                ",isDone INTEGER)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(sqlCreateQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun insert(
        title: String = "",
        description: String = "",
        isDone: String = "",
        imagePath: String? = "",
        color: String = "red"
    ): Long {


        val cv = ContentValues()

        cv.put("title", title)
        cv.put("description", description)
        cv.put("isDone", isDone)
        cv.put("color", color)
        cv.put("imagePath", imagePath)

        val db = this.writableDatabase
        return db.insert(tbl, null, cv)

    }

    fun select(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from $tbl", null)
    }

    fun update(
        title: String,
        description: String,
        isDone: String,
        color: String,
        imageName: String?,
        id: Int?
    ): Int {

        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put("title", title)
        cv.put("description", description)
        cv.put("isDone", isDone)
        cv.put("color", color)
        cv.put("imagePath", imageName)

        return db.update(tbl, cv, "id=?", arrayOf(id.toString()))

    }


    fun selectById(id: Int): String {

        val db = this.readableDatabase

        var tmpImage = ""

        val cursor = db.rawQuery("select * from $tbl where id=$id", null)

        Log.i("$$$ select count: ", cursor.count.toString())

        if (cursor.count == 1) {
            cursor.moveToFirst()

            tmpImage = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))

        }

        cursor.close()
        return tmpImage


    }
}