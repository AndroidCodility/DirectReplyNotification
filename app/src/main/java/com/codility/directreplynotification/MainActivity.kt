package com.codility.directreplynotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.RemoteInput
import android.support.v7.app.AppCompatActivity
import android.view.View


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * So, from Android Nougat, creating a notification channel is compulsory for displaying notifications.
        * Inside onCreate(), we will check if the device version is Android N or greater we will create a notification channel.
        * */
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(ConstantResource.CHANNEL_ID, ConstantResource.CHANNEL_NAME, importance)
            mChannel.description = ConstantResource.NOTIFICATION_DESCRIPTION
            mChannel.enableLights(true)
            mChannel.lightColor = Color.MAGENTA
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }

    fun showNotificationView(view: View) {
        displayNotification()
    }

    private fun displayNotification() {
        //Pending intent for a notification button named More
        val morePendingIntent = PendingIntent.getBroadcast(this, ConstantResource.REQUEST_CODE_MORE, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_MORE, ConstantResource.REQUEST_CODE_MORE), PendingIntent.FLAG_UPDATE_CURRENT)

        //Pending intent for a notification button help
        val helpPendingIntent = PendingIntent.getBroadcast(this, ConstantResource.REQUEST_CODE_HELP, Intent(this, MyNotificationReceiver::class.java)
                .putExtra(ConstantResource.KEY_HELP, ConstantResource.REQUEST_CODE_HELP), PendingIntent.FLAG_UPDATE_CURRENT)

        //We need this object for getting direct input from notification
        val remoteInput = RemoteInput.Builder(ConstantResource.NOTIFICATION_REPLY)
                .setLabel("Please enter your message")
                .build()

        //For the remote input we need this action object
        val action = NotificationCompat.Action.Builder(android.R.drawable.ic_delete, "Reply Now...", helpPendingIntent)
                .addRemoteInput(remoteInput)
                .build()

        //Creating the notifiction builder object
        val mBuilder = NotificationCompat.Builder(this, ConstantResource.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Hey this is Android Kotlin Coding...")
                .setContentText("Please share your message with us")
                .setAutoCancel(true)
                .setContentIntent(helpPendingIntent)
                .addAction(action)
                .addAction(android.R.drawable.ic_menu_compass, "More", morePendingIntent)
                .addAction(android.R.drawable.ic_menu_directions, "Help", helpPendingIntent)

        //finally displaying the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ConstantResource.NOTIFICATION_ID, mBuilder.build())
    }
}