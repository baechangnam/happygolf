package com.artilearn.happygolfgo.service

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder

import android.content.Intent



open class RhythmCoachServiceInterface( val context: Context) {
    private var mServiceConnection : ServiceConnection? =  null

    private var mService : RhythmCoachService? = null

    init {
        mServiceConnection =  object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val b = service as RhythmCoachService.RhythmCoachServiceBinder
                mService = b.getService()
            }
            override fun onServiceDisconnected(p0: ComponentName?) {
                mService = null
            }
        }
        if (mServiceConnection != null) {
            context.bindService(
                Intent(context, RhythmCoachService::class.java)
                    .setPackage(context.packageName), mServiceConnection!!, Context.BIND_AUTO_CREATE
            )
        }
    }

    open fun unbindRhythmCoachService() {
         if (mServiceConnection != null) {
             context.unbindService(mServiceConnection!!)
             mServiceConnection = null
             mService = null
         }
    }

    open fun play() {
         mService?.play()
    }

    open fun pause() {
         mService?.pause()
    }

    open fun upSpeed() {
        mService?.upSpeed()
    }

    open fun downSpeed() {
         mService?.downSpeed()
    }

    open fun repeat() {
        mService?.repeat()
    }

    open fun playOnce() {
        mService?.playOnce()
    }

    open fun stopPlay() {
         mService?.stopPlay()
    }

    open fun queryState() {
        mService?.queryState()
    }

//    open fun cleanUpRhythmService() {
//        mService?.cleanUpRhythmService()
//    }
}