package com.example.mainproject.utils


import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mainproject.model.Todo
import com.example.mainproject.ui.presentation.MainActivity
import com.example.mainproject.utils.Util.Companion.NOTIFICATION
import com.example.mainproject.utils.Util.Companion.NOTIFICATION_LIST
import okhttp3.internal.concurrent.Task


/*
Android13: We have full control when we want to ask the user for permission
Android 12L or lower: The system will show the permission dialog when the app creates its first notification channel*/

// the notification and channel can be created here

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
//        val appIntent = Intent(context, MainActivity::class.java) // Replace with your main activity
//
//        // Create a PendingIntent for the appIntent
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            appIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        // Set this pendingIntent as the contentIntent for your notification

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33
            intent.getParcelableExtra(NOTIFICATION, Notification::class.java)
        } else {
            intent.getParcelableExtra<Notification>(NOTIFICATION)
        }
        val notificationList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33
            intent.getParcelableArrayListExtra<Todo>(NOTIFICATION_LIST, Todo::class.java) // need to get list of class
        } else {
            intent.getParcelableArrayListExtra<Todo>(NOTIFICATION_LIST)
        }
//        notification?.contentIntent = pendingIntent
        if(notificationList?.isNotEmpty() == true){
            notificationList.forEach{
                if(it.date != null && it.time != null){
                    //val notification =  createNotification(it,context)
                    createChannel(getUniqueId().toString(),context,it)
                }
            }
        }
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        // Show the notification
        if (notification != null) {
            notificationManager.notify(getUniqueId().toInt(), notification) // notification id
        }
    }
}

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendReminderNotification(
    applicationContext: Context,
    channelId: String,
) {}
