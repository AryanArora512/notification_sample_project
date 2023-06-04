package com.example.sampleproject

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    private lateinit var requestLauncher:ActivityResultLauncher<String>



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name=findViewById<EditText>(R.id.editname)
        val tf2=findViewById<TextView>(R.id.textView2)
        val submit=findViewById<Button>(R.id.button)
        val nameinp=name.text


        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

                createNotifChannel(nameinp)

            }
            else {
                //show error message
                Toast.makeText(this,"permisson not granted",Toast.LENGTH_SHORT).show()
            }
        }







        submit.setOnClickListener {


            tf2.setText("hello $nameinp").toString()


            when{
                ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)==
                        PackageManager.PERMISSION_GRANTED->{
                            createNotifChannel(nameinp)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)->{

                }else->{
                    requestLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
            }
        }
        }

    }

    private fun createNotifChannel(nameinp: Editable){
        val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val Channel=NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor= Color.BLUE
                enableLights(true)
            }
            manager.createNotificationChannel(Channel)
        }
        manager.cancelAll()

        val notify=NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Hello $nameinp")
            .setContentText("hello $nameinp")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(0,notify)
    }


}