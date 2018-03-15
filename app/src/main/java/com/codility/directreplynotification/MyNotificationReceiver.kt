package com.codility.directreplynotification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.support.v4.app.RemoteInput
import android.widget.Toast


/**
 * Created by Govind on 3/15/2018.
 */
class MyNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //getting the remote input bundle from intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        //if there is some input
        if (remoteInput != null) {
            //getting the input value
            val msg = remoteInput.getCharSequence(ConstantResource.NOTIFICATION_REPLY)
            //updating the notification with the input value
            val mBuilder = NotificationCompat.Builder(context!!, ConstantResource.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_info_details)
                    .setContentTitle("Hey Thanks For Reply, ".plus(msg))
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(ConstantResource.NOTIFICATION_ID, mBuilder.build())
            //Methods For Vibrate
            shakeItVibrator(context)
        }

        //if help button is clicked
        if (intent!!.getIntExtra(ConstantResource.KEY_HELP, -1) == ConstantResource.REQUEST_CODE_HELP)
            Toast.makeText(context, "You Clicked Help", Toast.LENGTH_SHORT).show()

        //if more button is clicked
        if (intent.getIntExtra(ConstantResource.KEY_MORE, -1) == ConstantResource.REQUEST_CODE_MORE)
            Toast.makeText(context, "You Clicked More", Toast.LENGTH_SHORT).show()
    }

    // Vibrate for 150 milliseconds
    private fun shakeItVibrator(context: Context?) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context!!.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (context!!.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }
}