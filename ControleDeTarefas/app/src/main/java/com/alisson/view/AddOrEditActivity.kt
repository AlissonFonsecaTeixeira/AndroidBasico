package com.alisson.view

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.content.ContentValues
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alisson.R
import com.alisson.db.DatabaseHandler
import com.alisson.models.Tasks
import kotlinx.android.synthetic.main.activity_add_edit.*
import java.util.*

class AddOrEditActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var isEditMode = false
    private var image_uri : Uri? = null
    private var mCurrentPhotoPath: String = ""
    private var notificationManager: NotificationManager? = null
    private val notificationChannelID = "com.alisson"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        iniciarDB()
        iniciarOperacoes()
    }

    companion object {
        // image pick code
        private val REQUEST_IMAGE_GARELLY = 1000
        private val REQUEST_IMAGE_CAPTURE = 2000
    }

    private fun iniciarDB() {

        dbHandler = DatabaseHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val tasks: Tasks = dbHandler!!.getTask(intent.getIntExtra("Id",0))
            input_name.setText(tasks.name)
            input_desc.setText(tasks.desc)
            swt_completed.isChecked = tasks.completed == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun iniciarOperacoes() {
        btn_save.setOnClickListener({
            var success: Boolean = false
            if (!isEditMode) {
                val tasks: Tasks = Tasks()
                tasks.name = input_name.text.toString()
                tasks.desc = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.addTask(tasks) as Boolean
                showNotification("Projeto Final","Você possui uma nova tarefa", android.R.drawable.ic_dialog_info)
            } else {
                val tasks: Tasks = Tasks()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = input_name.text.toString()
                tasks.desc = input_desc.text.toString()
                if (swt_completed.isChecked)
                    tasks.completed = "Y"
                else
                    tasks.completed = "N"
                success = dbHandler?.updateTask(tasks) as Boolean
                showNotification("Projeto Final - Alerta","Uma de suas tarefas foi alterada !", android.R.drawable.ic_dialog_info)
            }

            if (success)
                finish()
        })

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        deleteNotificationChannel()
        createNotificationChannel()

        btnAddFoto?.setOnClickListener {
            btnAddFoto.setOnCreateContextMenuListener { menu, v, menuInfo ->
                menu.add(Menu.NONE, 1, Menu.NONE, "Escolher foto")
                menu.add(Menu.NONE, 2, Menu.NONE, "Tirar foto")
            }
        }


        btn_delete.setOnClickListener({
            val dialog = AlertDialog.Builder(this).setTitle("Atenção").setMessage("Clique em SIM para deleta-lá'")
                    .setPositiveButton("SIM", { dialog, i ->
                        val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                        if (success)
                            finish()
                        dialog.dismiss()
                    })
                    .setNegativeButton("NÃO", { dialog, i ->
                        dialog.dismiss()
                    })
            dialog.show()
        })
    }

    private fun showNotification(title: String, message: String, icon: Int) {
        var random = Random()
        val notification = Notification.Builder(this@AddOrEditActivity, notificationChannelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon)
                .setChannelId(notificationChannelID)
                .build()

        notificationManager?.notify(random.nextInt(10000000 + 1), notification)
    }

    private fun deleteNotificationChannel() {
        notificationManager?.deleteNotificationChannel(notificationChannelID)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(notificationChannelID, "Projeto Final - Notificação", importance)

            channel.description = "Essa notificação não tem um propósito. Esteja avisado."
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_IMAGE_GARELLY -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) pickImageFromGallery()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
            REQUEST_IMAGE_CAPTURE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture()
                else Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            1 -> getPermissionImageFromGallery()
            2 -> getPermissionTakePicture()
        }
        return super.onContextItemSelected(item)
    }

    private fun getPermissionImageFromGallery(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_GARELLY)
            } else {
                // permission granted
                pickImageFromGallery()
            }
        }
        else{
            // system < M
            pickImageFromGallery()
        }
    }

    private fun takePicture() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "nova imagem")
        values.put(MediaStore.Images.Media.DESCRIPTION, "imagem da camera")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

        image_uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = image_uri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        imageView.visibility = View.VISIBLE
    }

    private fun getPermissionTakePicture(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                // permission denied
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, REQUEST_IMAGE_CAPTURE)
            } else {
                // permission granted
                takePicture()
            }
        }
        else{
            // system < M
//            takePicture()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GARELLY)
        imageView.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GARELLY) imageView.setImageURI(data?.data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) imageView.setImageURI(image_uri)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
