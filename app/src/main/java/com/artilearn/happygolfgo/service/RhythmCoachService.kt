package com.artilearn.happygolfgo.service

import android.app.Service
import android.content.*
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import java.lang.Exception

/*
    bindService 는 Activity 와 동일 main UI thread 를 사용한다.
    1) bindService 를 호출하여 시작됨.
    2) unbindService 를 호출하면 서비스를 풀어주고, 모든 연결이 풀어진 경우에는 서비스는 onDestroy 를 호출하게 된다.
 */
fun Int.getResourceUriForRhythmCoach(context: Context): String {
    return context.resources.let {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(it.getResourcePackageName(this))		// it : resources, this : ResId(Int)
            .appendPath(it.getResourceTypeName(this))		// it : resources, this : ResId(Int)
            .appendPath(it.getResourceEntryName(this))		// it : resources, this : ResId(Int)
            .build()
            .toString()
    }
}

open class RhythmCoachService : Service() , AudioManager.OnAudioFocusChangeListener {
    private val mBinder = RhythmCoachServiceBinder()
    private var mMediaPlayer : MediaPlayer? = null
    private var  isPrepared =  false
    private var mDuration  = 0
    private var mCurrentPosition = 0
    open   var mCurrentSpeedIndex = 1
    open   var mIsLoop = false
    private var mAudioManager : AudioManager? = null

    private var playbackDelayed = false
    private var playbackNowAuthorized = false
    private var resumeOnFocusGain =  false
    private var mAudioFocusRequest:AudioFocusRequest? = null

    private var  mNoisyReceiver =  object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
             when( intent?.action) {
                 //이어폰을 뺐을때, 소리커지는거 방지하기 위함
                 AudioManager.ACTION_AUDIO_BECOMING_NOISY ->  handlerAfChangeListener.postDelayed(delayedStopRunnable, 200)
                 else -> Log.d("else", "111")
             }
        }
    }

    private val handlerAfChangeListener  = Handler( Looper.getMainLooper())
    private val delayedStopRunnable: Runnable = Runnable { stopPlay() }

    private val focusLock = Any()

    private var mUpdateHandler = Handler( Looper.getMainLooper())
    private var mAudioFocusHandler = Handler( Looper.getMainLooper())

    private val mUpdater: Runnable = object : Runnable {
        override fun run() {
            broadcastProgress()
            mUpdateHandler.postDelayed(this, 200)
        }
    }

    private fun startMediaObserving() {
        stopMediaObserving()
        mUpdateHandler.postDelayed(mUpdater, 200)
    }

    private fun stopMediaObserving() {
        mUpdateHandler.removeCallbacks(mUpdater)
    }


    private val mAfChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                handlerAfChangeListener.postDelayed(delayedStopRunnable, 200)
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                handlerAfChangeListener.postDelayed(delayedStopRunnable, 200)
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                handlerAfChangeListener.postDelayed(delayedStopRunnable, 200)
            }

            AudioManager.AUDIOFOCUS_GAIN -> {

            }
        }
    }

    @Suppress("DEPRECATION")
    private fun abandonAudioFocusListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mAudioFocusRequest != null) {
                mAudioManager?.abandonAudioFocusRequest( mAudioFocusRequest!!)
            }
        }  else {
           mAudioManager?.abandonAudioFocus(mAfChangeListener)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN ->
                if (playbackDelayed || resumeOnFocusGain) {
                    synchronized(focusLock) {
                        playbackDelayed = false
                        resumeOnFocusGain = false
                    }
                    play()
                }
            AudioManager.AUDIOFOCUS_LOSS -> {
                synchronized(focusLock) {
                    resumeOnFocusGain = false
                    playbackDelayed = false
                }
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                synchronized(focusLock) {
                    resumeOnFocusGain = true
                    playbackDelayed = false
                }
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                pause()
            }
        }
    }

    private var  mRhythmCoachNotificationPlayer:RhythmCoachNotificationPlayer? = null

    inner class RhythmCoachServiceBinder : Binder() {
        fun getService() : RhythmCoachService  {
            return this@RhythmCoachService
        }
    }

    fun broadcastProgress() {
        try {
            if (isPrepared) {
                val intent = Intent(RhythmCoachBroadcastAction.STATE_PROGRESS)
                val currentPosition = mMediaPlayer?.currentPosition
                val duration = mMediaPlayer?.duration
                intent.putExtra(RhythmCoachBroadcastAction.STATE_DURATION, duration)
                intent.putExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, currentPosition)
                sendBroadcast(intent)
            }
        } catch (e: Exception){
            Log.d("error", e.toString())
        }
    }

    @Suppress("DEPRECATION")
    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val res = mAudioManager?.requestAudioFocus(mAudioFocusRequest!!)
            synchronized(focusLock) {
                playbackNowAuthorized = when (res) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> false
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                        //playbackNow()
                        true
                    }
                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                        playbackDelayed = true
                        false
                    }
                    else -> false
                }
            }
        } else {
            val result: Int? = mAudioManager?.requestAudioFocus(
                mAfChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN
            )
            playbackNowAuthorized = (if (result != null) {
                result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            } else {
                false
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager =
                applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setUsage(AudioAttributes.USAGE_GAME)
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                setAcceptsDelayedFocusGain(true)
                setOnAudioFocusChangeListener(this@RhythmCoachService, mAudioFocusHandler)
                build()
            }
            if (mAudioFocusRequest != null) {
                requestAudioFocus()
            }
        } else {
            mAudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }

        val filter = IntentFilter()
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY )
        registerReceiver(mNoisyReceiver, filter)

        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mMediaPlayer?.setOnPreparedListener {
              isPrepared = true
              mDuration = it.duration
              mCurrentPosition = it.currentPosition
              it.isLooping = mIsLoop
              broadcastCurrentState()
              startMediaObserving()
              it.start()
        }

        mMediaPlayer?.setOnCompletionListener {
              stopMediaObserving()
              isPrepared = false
              broadcastCurrentState( false)
              abandonAudioFocusListener()
        }

        mMediaPlayer?.setOnErrorListener { mp: MediaPlayer, _, _ ->
            stopMediaObserving()
            abandonAudioFocusListener()
            isPrepared = false
            mp.reset() //When Error, media player must be reset
            broadcastCurrentState( false)
            false
        }

        mMediaPlayer?.setOnSeekCompleteListener {

        }

        mRhythmCoachNotificationPlayer = RhythmCoachNotificationPlayer(this)
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action == RhythmCoachCommandActions.TOGGLE_PLAY) {
                 if (isPlaying()) {
                     pause()
                 } else {
                     play()
                 }
            } else if (action == RhythmCoachCommandActions.REWIND) {
                 Log.d("SERVICE", "REWIND")
            } else if (action == RhythmCoachCommandActions.FORWARD) {
                Log.d("SERVICE", "FORWARD")
            } else if (action == RhythmCoachCommandActions.CLOSE) {
                Log.d("SERVICE", "CLOSE")
                if (isPlaying()) {
                    pause()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanUpRhythmService()
        unregisterReceiver(mNoisyReceiver)
    }

    open fun cleanUpRhythmService() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer?.stop()
                mMediaPlayer?.release()
                mMediaPlayer = null
            }
            mRhythmCoachNotificationPlayer?.removeNotificationPlayer()
            mRhythmCoachNotificationPlayer = null
        } catch ( e: Exception) {
            Log.d("cleanUpRhythmService",  "cleanUpRhythmService")
        }
    }

    private fun prepare() {
        try {
            requestAudioFocus()
            /* 2022.02.15 리듬 코치 배포 보류 중
            when(mCurrentSpeedIndex) {
                //TODO: 41(2.2.5)  remove rh_00_000.mp3 and remove comments below
//                1 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_01_140.getResourceUriForRhythmCoach(applicationContext)))
//                2 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_01_145.getResourceUriForRhythmCoach(applicationContext)))
//                3 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_01_150.getResourceUriForRhythmCoach(applicationContext)))
//                4 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_01_155.getResourceUriForRhythmCoach(applicationContext)))
//                5 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_01_160.getResourceUriForRhythmCoach(applicationContext)))

                1 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
                2 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
                3 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
                4 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
                5 -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse( R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
                else -> mMediaPlayer?.setDataSource( applicationContext, Uri.parse(R.raw.rh_00_000.getResourceUriForRhythmCoach(applicationContext)))
            }
            */

            mMediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) .build())
            mMediaPlayer?.prepareAsync()
        } catch (e : Exception) {
                Log.d("error", "Error")
        }
    }

    open  fun stopPlay( ) {
        try {
            mMediaPlayer?.stop()
            mMediaPlayer?.reset()
            stopMediaObserving()
            sendBroadcast( Intent(RhythmCoachBroadcastAction.PAUSE))
        } catch (e: Exception) {
            Log.d("error", "Error")
        }
    }

    open fun play() {
        try {
            stopPlay()
            prepare()
            updateNotificationPlayer()
        } catch (e : Exception) {

        }
    }

    open fun isPlaying(): Boolean {
        return try {
            mMediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            false
        }
    }

    open fun pause() {
         if (isPrepared) {
             mMediaPlayer?.pause()
             queryState()
         }
    }

    open fun broadcastCurrentState( isPlaying : Boolean= true) {
        val intent = Intent(RhythmCoachBroadcastAction.CURRENT_PLAY_STATE)
        intent.putExtra( RhythmCoachBroadcastAction.STATE_IS_LOOPING, mIsLoop)
        intent.putExtra( RhythmCoachBroadcastAction.STATE_IS_PLAYING, isPlaying)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_DURATION, mDuration)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, mCurrentPosition)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_SPEED_INDEX, mCurrentSpeedIndex)
        sendBroadcast(intent)
    }

    open fun upSpeed() {
        if (mCurrentSpeedIndex in 1..4) {
            mCurrentSpeedIndex += 1
            if (isPlaying()) {
                 play()
            }
        } else {
            mCurrentSpeedIndex = 5
        }
        queryState()
    }

    open fun downSpeed() {
        if (mCurrentSpeedIndex in 2..5) {
            mCurrentSpeedIndex -= 1
            if (isPlaying()) {
                play()
            }
        } else {
            mCurrentSpeedIndex = 1
        }
        queryState()
    }

    open fun repeat() {
        val isPlaying : Boolean = isPlaying()
        if (isPlaying) {
            try {
                mMediaPlayer?.isLooping = true
                mIsLoop = true
            } catch (e:Exception) {

            }
        } else {
            mIsLoop = true
        }
        queryState()
    }

    open fun playOnce() {
        val isPlaying = isPlaying()
        if (isPlaying) {
            try {
                mMediaPlayer?.isLooping = false
                mIsLoop = false
            } catch (e:Exception) {

            }
        } else {
            mIsLoop = false
        }
        queryState()
    }

    open fun updateNotificationPlayer() {
        mRhythmCoachNotificationPlayer?.updateNotificationPlayer()
    }
    //TODO:  remove notification player some time later
//    open fun removeNotificationPlayer() {
//        mRhythmCoachNotificationPlayer?.removeNotificationPlayer()
//    }

    open fun queryState() {
        val isPlaying = isPlaying()
        val intent = Intent(RhythmCoachBroadcastAction.CURRENT_PLAY_STATE)
        intent.putExtra( RhythmCoachBroadcastAction.STATE_IS_LOOPING, mIsLoop)
        intent.putExtra( RhythmCoachBroadcastAction.STATE_IS_PLAYING, isPlaying)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_DURATION, mDuration)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_CURRENT_POSITION, mCurrentPosition)
        intent.putExtra(RhythmCoachBroadcastAction.STATE_SPEED_INDEX, mCurrentSpeedIndex)
        sendBroadcast(intent)
    }

}