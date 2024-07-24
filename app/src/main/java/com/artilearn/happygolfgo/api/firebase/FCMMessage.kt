package com.artilearn.happygolfgo.api.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMMessage : FirebaseMessagingService(), LifecycleObserver {

    private var eventType: String? = null
    private var eventValue: String? = null
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data)
    }

    private fun sendNotification(body: MutableMap<String, String>) {
        createNotificationChannel()

        eventType = body["eventType"]
        eventValue = body["eventValue"]
        createNotification(body, eventType)
    }

    private fun createNotification(body: MutableMap<String, String>, type: String?) {
        val intent: Intent

        when (type) {
            "3", "7" -> {
                if (isRunning) {
                    intent = Intent(this, ReserveConfirmActivity::class.java)
                        .apply {
                            putExtra(RESERVATION_TYPE, reservationPlate)
                            putExtra(PLATE_ID, eventValue?.toInt())
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                } else {
                    intent = Intent(this, HomeActivity::class.java)
                        .apply {
                            putExtra(RESERVATION_TYPE, reservationPlate)
                            putExtra(PLATE_ID, eventValue?.toInt())
                            putExtra(FCM_EVENT_TYPE, eventType)
                        }
                }
            }
            "4" -> {
                if (isRunning) {
                    intent = Intent(this, ReserveConfirmActivity::class.java)
                        .apply {
                            putExtra(RESERVATION_TYPE, reservationLesson)
                            putExtra(LESSON_ID, eventValue?.toInt())
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                } else {
                    intent = Intent(this, HomeActivity::class.java)
                        .apply {
                            putExtra(RESERVATION_TYPE, reservationLesson)
                            putExtra(LESSON_ID, eventValue?.toInt())
                            putExtra(FCM_EVENT_TYPE, eventType)
                        }
                }
            }
            "6" -> {
                if (isRunning) {
                    intent = Intent(this, LogActivity::class.java)
                        .apply {
                            putExtra(LESSON_ID, eventValue?.toInt())
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                } else {
                    intent = Intent(this, HomeActivity::class.java)
                        .apply {
                            putExtra(LESSON_ID, eventValue?.toInt())
                            putExtra(FCM_EVENT_TYPE, eventType)
                        }
                }
            }
            else -> {
                eventType = ""
                if (isRunning) {
                    intent = Intent(this, HomeActivity::class.java)
                        .apply {
                            putExtra(FCM_EVENT_TYPE, eventType)
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                } else {
                    intent = Intent(this, HomeActivity::class.java)
                        .apply {
                            putExtra(FCM_EVENT_TYPE, eventType)
                        }
                }
            }
        }

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //comments: Target SDK 31, FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                    PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_appnotifi)
            .setContentTitle("${body["title"]}")
            .setContentText("${body["body"]}")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .setSound(sound)
            .setAutoCancel(true)
        //TODO: Error android 13 policy
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.default_notification_channel_id)
            val descriptionText = getString(R.string.default_notification_text)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public fun appCreate() {
        isRunning = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public fun appResume() {
        isRunning = true
    }

    companion object {
        const val reservationPlate = "ReservationPlateFromAlram"
        const val reservationLesson = "ReservationLessonFromAlram"
    }

}