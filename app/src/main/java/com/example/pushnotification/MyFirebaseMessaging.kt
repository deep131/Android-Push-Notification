package com.example.pushnotification

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessaging: FirebaseMessagingService() {



        // Override onMessageReceived() method to extract the
        // title and
        // body from the message passed in FCM

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            super.onMessageReceived(remoteMessage)
            handleMessage(remoteMessage)

            if (remoteMessage.notification != null) {
                // Since the notification is received directly from
                // FCM, the title and the body can be fetched
                // directly as below.
                showNotification(
                    remoteMessage.notification!!.title,
                    remoteMessage.notification!!.body
                )
            }
        }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        //1
        val handler = Handler(Looper.getMainLooper())
        Log.e("Logs Success", remoteMessage.toString())

        //2
        handler.post(Runnable {
            Toast.makeText(baseContext, getString(1),
                Toast.LENGTH_LONG).show()
        }
        )
    }

        // Method to get the custom Design for the display of
        // notification.
       /* private fun getCustomDesign(
            title: String?,
            message: String?
        ): RemoteViews? {

            remoteViews.setTextViewText(R.id.title, title)
            remoteViews.setTextViewText(R.id.message, message)

            return remoteViews
        }
*/
        // Method to display the notifications
        fun showNotification(
            title: String?,
            message: String?
        ) {
            // Pass the intent to switch to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Assign channel ID
            val channel_id = "notification_channel"
            // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
            // the activities present in the activity stack,
            // on the top of the Activity that is to be launched
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Pass the intent to PendingIntent to start the
            // next Activity
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            // Create a Builder object using NotificationCompat
            // class. This will allow control over all the flags
            var builder: NotificationCompat.Builder = NotificationCompat.Builder(
                applicationContext,
                channel_id
            )

                .setAutoCancel(true)
                .setVibrate(
                    longArrayOf(
                        1000, 1000, 1000,
                        1000, 1000
                    )
                )
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

            // A customized design for the notification can be
            // set only for Android versions 4.1 and above. Thus
            // condition for the same is checked here.
            if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN
            ) {
              /*  builder = builder.setContent(
                    getCustomDesign(title, message)
                )*/
            } // If Android Version is lower than Jelly Beans,
            else {
                builder = builder.setContentTitle(title)

            }
            // Create an object of NotificationManager class to
            // notify the
            // user of events that happen in the background.
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            // Check if the Android Version is greater than Oreo
            if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O
            ) {
                val notificationChannel = NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(
                    notificationChannel
                )
            }
            notificationManager.notify(0, builder.build())
        }
        }