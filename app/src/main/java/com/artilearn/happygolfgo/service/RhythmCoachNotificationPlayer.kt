package com.artilearn.happygolfgo.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import android.app.Notification
import android.app.NotificationChannel

import android.content.Intent
import android.os.Build
import android.util.Log
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.home.HomeActivity
import java.lang.Exception





open class RhythmCoachNotificationPlayer (service : RhythmCoachService?) {
    companion object {
         const val NOTIFICATION_PLAYER_ID = 0x342
    }
    var mNotificationManager: NotificationManager? = null
    private var mNotificationBuilder : NotificationManagerBuilder ? = null
    var mService : RhythmCoachService? = null
    var isForeground  = false

    open inner class NotificationManagerBuilder  {

//        private var mRemoteViews : RemoteViews? = null //노티 단순화 작업

        private var mNotificationBuilder : NotificationCompat.Builder? = null
        private var mMainPendingIntent:PendingIntent? = null
        private val rhythmRoachChannelId = "rhythm_coach_notification_channel"
        private var isChannelCreated = false

        private fun createNotificationChannel() {
            if (!isChannelCreated) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = mService?.getString(R.string.rhythm_coach_notification_channel_name)
                    val descriptionText =
                        mService?.getString(R.string.rhythm_coach_notification_channel_name)
                    val importance = NotificationManager.IMPORTANCE_LOW
                    val channel = NotificationChannel(
                        rhythmRoachChannelId,
                        name, importance
                    ).apply {
                        description = descriptionText
                    }
                    mNotificationManager?.createNotificationChannel(channel)
                }
                isChannelCreated = true
            }
        }
        private fun onPreExecute() {
            if (mService != null) {
                val homeActivityIntent = Intent(mService, HomeActivity::class.java)
                homeActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                mMainPendingIntent = PendingIntent.getActivity(mService, 0,
                    homeActivityIntent,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )

//                mRemoteViews = createRemoteView(R.layout.rhythm_coach_notification_player) //simplify notification
                createNotificationChannel()
                mNotificationBuilder = NotificationCompat.Builder(mService!!, rhythmRoachChannelId)
                mNotificationBuilder?.setSmallIcon(R.mipmap.ic_launcher)
                    ?.setOngoing(true)
                    ?.setContentIntent(mMainPendingIntent)
//                    ?.setContent(mRemoteViews)  //simplify notification

                val notification: Notification? = mNotificationBuilder?.build()
                notification?.contentIntent = mMainPendingIntent
                if (!isForeground) {
                    isForeground = true
                    mService?.startForeground(NOTIFICATION_PLAYER_ID, notification)
                }
            }
        }

        private fun doInBackground(): Notification? {
//            mNotificationBuilder?.setContent(mRemoteViews)
            mNotificationBuilder?.setContentIntent(mMainPendingIntent)
            return mNotificationBuilder?.build()
        }

        private fun onPostExecute(notification: Notification?)  {
            try {
                if (notification != null) {
                    mNotificationManager?.notify(NOTIFICATION_PLAYER_ID, notification)
                }
            } catch (e: Exception) {

            }
        }

        open fun execute() {
            onPreExecute()
            onPostExecute( doInBackground())
        }

        fun cancel( isCancel: Boolean) {
            Log.d("cancel", "$isCancel")
        }

//        private fun createRemoteView(layoutId: Int) :RemoteViews? {
//            try {
//                val remoteView = RemoteViews(
//                        mService?.packageName,
//                        layoutId
//                    )
//                val actionTogglePlay = Intent(RhythmCoachCommandActions.TOGGLE_PLAY)
//                val actionForward = Intent(RhythmCoachCommandActions.FORWARD)
//                val actionRewind = Intent(RhythmCoachCommandActions.REWIND)
//                val actionClose = Intent(RhythmCoachCommandActions.CLOSE)
//
//                val togglePlay: PendingIntent =
//                    PendingIntent.getService(mService, 0, actionTogglePlay, PendingIntent.FLAG_IMMUTABLE)
//                val forward: PendingIntent =
//                    PendingIntent.getService(mService, 0, actionForward, PendingIntent.FLAG_IMMUTABLE)
//                val rewind: PendingIntent = PendingIntent.getService(mService, 0, actionRewind, PendingIntent.FLAG_IMMUTABLE)
//                val close: PendingIntent = PendingIntent.getService(mService, 0, actionClose, PendingIntent.FLAG_IMMUTABLE)
//                remoteView.setOnClickPendingIntent(R.id.btn_play_pause, togglePlay)
//                remoteView.setOnClickPendingIntent(R.id.btn_rewind, rewind)
//                remoteView.setOnClickPendingIntent(R.id.btn_forward, forward)
//                remoteView.setOnClickPendingIntent(R.id.btn_close, close)
//                return remoteView
//            } catch ( e: Exception) {
//               return null
//            }
//        }

//        fun updateRemoteView( remoteViews: RemoteViews?, notification: Notification?) {
//            if (mService?.isPlaying() == true) {
//                remoteViews?.setImageViewResource(R.id.btn_play_pause, R.drawable.rhythm_coach_btn_bg_play)
//            } else {
//                remoteViews?.setImageViewResource(R.id.btn_play_pause, R.drawable.rhythm_coach_btn_bg_repeat)
//            }
//        }
    }
    init {
         mService = service
         mNotificationManager = service?.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
    }
    open fun updateNotificationPlayer() {
        cancel()
        mNotificationBuilder = NotificationManagerBuilder()
        mNotificationBuilder?.execute()
    }
    open fun removeNotificationPlayer() {
        cancel()
        mService?.stopForeground(true)
        isForeground = false
        mNotificationManager?.cancel(NOTIFICATION_PLAYER_ID)
    }
    open fun cancel() {
        mNotificationBuilder?.cancel(true)
        mNotificationBuilder = null
    }
}