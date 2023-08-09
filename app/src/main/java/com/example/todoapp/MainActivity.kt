package com.example.todoapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.todoapp.pages.DoneFragment
import com.example.todoapp.pages.TasksFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    lateinit var bottom_nav: BottomNavigationView

    var tasksFragment: TasksFragment = TasksFragment()
    var doneFragment: DoneFragment = DoneFragment()

    lateinit var toolbarMain: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_nav = findViewById(R.id.bottom_nav)
        toolbarMain = findViewById(R.id.toolbar_main)

        toolbarMain.textAlignment = Toolbar.TEXT_ALIGNMENT_CENTER
        toolbarMain.title = "Tasks"
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        setSupportActionBar(toolbarMain)

        setPage(tasksFragment)

        bottom_nav.setOnItemSelectedListener(BottomNav_Listener())

        val value = intent.extras?.getInt("value")
        if (value != null) {
            AlertDialog.Builder(this)
                .setTitle("success")
                .setMessage("task saved successfully")
                .create().show()
        }


    }

    inner class BottomNav_Listener : NavigationBarView.OnItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {

            when (item.itemId) {
                R.id.mnu_todo -> {
                    toolbarMain.title = "Tasks"
                    setPage(tasksFragment)
                }

                R.id.mnu_done -> {
                    toolbarMain.title = "done tasks"
                    setPage(doneFragment)
                }
            }

            return true
        }


    }

    private fun setPage(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, fragment)
            .addToBackStack(fragment.tag).commit()

    }
}


