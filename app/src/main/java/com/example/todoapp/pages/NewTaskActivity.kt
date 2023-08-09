package com.example.todoapp.pages

import android.app.AlarmManager
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.receivers.NotificationReceiver
import com.example.todoapp.utils.Utils
import com.example.todoapp.utils.room.RoomDB
import com.example.todoapp.utils.room.models.Task
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File

class NewTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnRed: ImageButton
    private lateinit var btnBlue: ImageButton
    private lateinit var btnGreen: ImageButton
    private lateinit var btnYellow: ImageButton

    private lateinit var btnCamera: ImageButton

    private lateinit var btnNotify: ImageButton

    private lateinit var btnCancel: Button

    private lateinit var inputTitle: EditText
    private lateinit var inputDescription: EditText
    private lateinit var chkDone: CheckBox

    private var id: Int? = null

    private var selectedColor = "red"

    private lateinit var btnSave: Button

    private lateinit var uri: Uri
    private lateinit var imgPic: ImageView
    private lateinit var btnImageDelete: ImageButton

    private var imageName: File? = null
    private lateinit var file: File
    private var colorsButton = ArrayList<ImageButton>()
    private var imagePath: String? = null


    private val roomDatabase by lazy { RoomDB.getInstance(this).getTaskDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        btnRed = findViewById(R.id.btn_red)
        btnBlue = findViewById(R.id.btn_blue)
        btnGreen = findViewById(R.id.btn_green)
        btnYellow = findViewById(R.id.btn_yellow)
        btnSave = findViewById(R.id.btnSave)
        btnNotify = findViewById(R.id.btn_notif)
        btnImageDelete = findViewById(R.id.btn_image_delete)
        inputTitle = findViewById(R.id.input_title)
        inputDescription = findViewById(R.id.input_description)
        chkDone = findViewById(R.id.chk_isDone)
        imgPic = findViewById(R.id.img_pic)
        btnCamera = findViewById(R.id.btn_camera)

        var intent = intent
        btnCamera.isEnabled = false

        inputTitle.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnCamera.isEnabled = p0?.length!! > 0
            }

            override fun onTextChanged(
                characterSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {


            }

            override fun afterTextChanged(p0: Editable?) {

                btnCamera.isEnabled = p0?.length!! > 0

            }
        })


        val rlImage = findViewById<RelativeLayout>(R.id.rl_image)

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        rlImage.visibility = View.GONE


        file = File(filesDir, getString(R.string.temp_images_dir))
        file.mkdir()

        //region camera result

        val cameraResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {

            if (it) {
                removeImages(id)
                rlImage.visibility = View.VISIBLE
                Picasso.get().load(uri).rotate(-90F).into(imgPic)
                imagePath = uri.toString()
                Toast.makeText(applicationContext, "picture captured!!!:$it", Toast.LENGTH_LONG)
                    .show()
                Log.i("$$ image path: ", imagePath!!)
            } else {
                rlImage.visibility = View.GONE
            }

        }
        //endregion

        /* start to capture image from camera */
        btnCamera.setOnClickListener {

            intent = getIntent()
            if (intent != null && intent.extras?.getInt("id") != null) {

                val id = intent.extras?.getInt("id")

                lifecycleScope.launch {

                    val tempUri = roomDatabase.getTaskById(id!!).image

                    if (tempUri.isNotBlank() or tempUri.isNotEmpty()) {

                        imgPic.setImageURI(tempUri.toUri())

                    } else {
                        Utils.makeMessage(
                            applicationContext,
                            "no image found",
                            "$$$$ no image found"
                        )
                    }
                }
            } else {
                val taskFolder = File(file, "$title")
                imageName = File(taskFolder, "${inputTitle.text}_${Math.random()}.jpg")
                uri = FileProvider.getUriForFile(this, getString(R.string.authorities), imageName!!)
                cameraResult.launch(uri)
            }
        }

        btnSave.setOnClickListener(this)

        colorsButton = arrayListOf(btnRed, btnBlue, btnGreen, btnYellow)

        btnRed.setOnClickListener(this)
        btnBlue.setOnClickListener(this)
        btnGreen.setOnClickListener(this)
        btnYellow.setOnClickListener(this)


        btnNotify.setOnClickListener {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_layout)
            dialog.setCancelable(true)

            val btnOk: Button = dialog.findViewById(R.id.btn_ok)

            btnCancel = dialog.findViewById(R.id.btn_cancel)

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            val inputMinutes: EditText = dialog.findViewById(R.id.input_minutes)

            btnOk.setOnClickListener {

                val seconds = inputMinutes.text.toString()
                val title = inputTitle.text.toString()
                val text = inputDescription.text.toString()

                scheduleNotification(applicationContext, seconds.toLong() * 1000 * 60, title, text)
                dialog.dismiss()
            }

            dialog.show()
        }



        if (intent != null && intent.extras?.getInt("id") != null) {

            Log.i("$$$ \$ID:", id.toString())

            id = intent.extras?.getInt("id")


            lifecycleScope.launch {

                val taskTmp = roomDatabase.getTaskById(id!!)
                inputTitle.setText(taskTmp.title)
                inputDescription.setText(taskTmp.description)
                chkDone.isChecked = taskTmp.isDone

                if (!(taskTmp.image.isBlank() or taskTmp.image.isEmpty())) {

                    imgPic.setImageURI(File(taskTmp.image).toUri())

                }
            }

        }


    }

    private fun removeImages(id: Int?) {

        var title: String

        lifecycleScope.launch {
            if (id != null) {
                val task = roomDatabase.getTaskById(id)
                title = task.title
                val targetFiles = File(file, title)
                if (targetFiles.exists()) {
                    for (f in targetFiles.listFiles()!!) {
                        f.delete()
                    }
                } else {

                    Utils.makeMessage(
                        applicationContext,
                        "file folder not found",
                        "folder not found"
                    )
                }

            }
        }

    }

    private fun scheduleNotification(context: Context, time: Long, title: String, text: String) {

        Toast.makeText(applicationContext, time.toString(), Toast.LENGTH_SHORT).show()

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("text", text)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            42,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + time,
                pendingIntent
            )
        }
        Toast.makeText(applicationContext, "Scheduled ", Toast.LENGTH_LONG).show()

    }

    fun cancelNotification(context: Context, time: Long, title: String, text: String) {

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("text", text)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            42,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }

        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel("channelId", "name", importance)
        channel.description = "description"
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_red -> {
                Toast.makeText(this, "red", Toast.LENGTH_LONG).show()
                clearImages()
                btnRed.setImageResource(R.drawable.baseline_check_24)
                selectedColor = "red"
            }

            R.id.btn_green -> {
                Toast.makeText(this, "green", Toast.LENGTH_LONG).show()
                clearImages()
                btnGreen.setImageResource(R.drawable.baseline_check_24)
                selectedColor = "green"
            }

            R.id.btn_blue -> {
                Toast.makeText(this, "blue", Toast.LENGTH_LONG).show()
                clearImages()
                btnBlue.setImageResource(R.drawable.baseline_check_24)
                selectedColor = "blue"
            }

            R.id.btn_yellow -> {
                Toast.makeText(this, "yellow", Toast.LENGTH_LONG).show()
                clearImages()
                btnYellow.setImageResource(R.drawable.baseline_check_24)
                selectedColor = "yellow"
            }

            R.id.btnSave -> {

                if (imagePath != null) {
                    Utils.makeMessage(
                        applicationContext,
                        "image path: " + imagePath.toString(),
                        imagePath.toString()
                    )
                }

                Utils.makeMessage(applicationContext, "OIPPPPPPP", "TTTTRRRRR")

                if (intent != null) {
                    if (intent.extras?.getInt("id") != null) {
                        Utils.makeMessage(applicationContext, "wwwwwwww", "rrrrrrrrrrrrrrr")

                        val task = Task()

                        task.title = inputTitle.text.toString()
                        task.description = inputDescription.text.toString()
                        task.color = selectedColor
                        task.isDone = chkDone.isChecked
                        if (imagePath != null) {
                            task.image = imagePath!!
                        }

                        lifecycleScope.launch {

                            roomDatabase.updateTask(task)
                        }
                    } else {

                        Utils.makeMessage(applicationContext, "kkkkkkkk", "O$$4")

                        val task = Task()

                        task.title = inputTitle.text.toString()
                        task.description = inputDescription.text.toString()
                        task.color = selectedColor
                        task.isDone = chkDone.isChecked
                        if (imagePath != null) {
                            task.image = imagePath!!
                        }

                        lifecycleScope.launch {

                            roomDatabase.insertTask(task)
                        }

                    }


                }

            }
        }

    }

    private fun clearImages() {

        for (btn in colorsButton) btn.setImageResource(0)
    }

    private fun getImage(id: Int): String {

        var uri: String = ""

        lifecycleScope.launch {
            val task = roomDatabase.getTaskById(id)
            uri = task.image
        }
        return uri
    }
}