package com.artilearn.happygolfgo.ui.home.lecture

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity

import com.artilearn.happygolfgo.databinding.ActivityLectureFullscreenViewBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.exo_playback_control_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class FullScreenPlayerActivity :
    BaseActivity<ActivityLectureFullscreenViewBinding>(R.layout.activity_lecture_fullscreen_view) {


    private val lectUrl by lazy { intent.getStringExtra("lectUrl") }
    private val lectNo by lazy { intent.getStringExtra("lectNo") }
    private val playbackPositions by lazy { intent.getLongExtra("playbackPosition",0L) }
    private val lectTitle by lazy { intent.getStringExtra("lectTitle") }


    private val viewModel: LectureDetailViewModel by viewModel()

    private var playReady = true
    private var isFirsLoad = true
    private var player: ExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.close.setOnClickListener{
            intent.putExtra("playbackPos" ,  player!!.currentPosition)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.exoplayer.exo_rew.setImageResource(R.drawable.btn_player_prev)
        binding.exoplayer.exo_ffwd.setImageResource(R.drawable.btn_player_next)
        binding.exoplayer.exo_play.setImageResource(R.drawable.btn_player_play)
        binding.exoplayer.exo_pause.setImageResource(R.drawable.btn_player_pause)

        binding.exoplayer.exo_fullscreen_icon.setBackgroundResource(R.drawable.btn_scale_down)
        binding.exoplayer.exo_fullscreen_icon.setOnClickListener{

            val data = Intent()
            data.putExtra("playbackPos" ,  player!!.currentPosition)
            setResult(RESULT_OK, data)
            finish()
        }

        binding.exoplayer.exo_text.text = lectTitle

    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("playbackPos" ,  player!!.currentPosition)
        setResult(RESULT_OK, data)
        finish()
    }

    private fun showVideoView() {
        playReady = true

        if(isFirsLoad){
            playbackPosition = playbackPositions
            isFirsLoad=false
        }else{
            playbackPosition = player!!.currentPosition
        }

        currentWindow = player!!.currentWindowIndex
        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)

    }

    private fun videoStateReady() {
        player?.playWhenReady = playReady
    }

    private fun hidePlaybackController() {
        with(binding) {
            exoplayer.useController = false;
        }
    }

    private fun showPlaybackController() {
        with(binding) {
            exoplayer.useController = true;
        }
    }

    private fun initializePlayer(path: String) {
        player = ExoPlayer.Builder(this).build() //ExoPlayerFactory.newSimpleInstance(this)
        binding.exoplayer.player = player

        val uri = path.toUri()
        val dataSourceFactory = DefaultDataSourceFactory(this, "happygolf_pro")
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        val exoEventListener = object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> videoStateReady()
                    Player.STATE_READY -> {}
                    Player.STATE_ENDED -> videoStateEnd()
                    //  Player.STATE_IDLE -> showToast( ToastType.ERROR, getString(R.string.log_video_start_fail))
                }
                super.onPlayerStateChanged(playWhenReady, playbackState)
            }
        }
        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)
        player?.addListener(exoEventListener)

        binding.exoplayer.exo_rew.setBackgroundResource(R.drawable.btn_player_prev)
        setAudioFocus()


    }

    private fun stringForTime(timeMs: Int): String? {
        val mFormatBuilder = StringBuilder()
        val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        //  videoDurationInSeconds = totalSeconds % 60;
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private fun videoStateEnd() {
        playReady = false
        currentWindow = 0
        playbackPosition = 0L

        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)


        val map: HashMap<String, String> = HashMap()
        map["playTime"] = "12345678911"
        val duration = player?.duration

        Log.d("myLog", "stringForTime " + stringForTime(duration!!.toInt()))

        lectNo?.let { viewModel.playEnd(it,map) }

    }

    private fun releasePlayer() {
        if (player != null) {
            playReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentMediaItemIndex
            player!!.release()
            player = null
        }
    }

    private fun setAudioFocus() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val afChangeListener: AudioManager.OnAudioFocusChangeListener =
            AudioManager.OnAudioFocusChangeListener {
                when (it) {
                    AudioManager.AUDIOFOCUS_LOSS -> {
                        player?.playWhenReady = false
                    }

                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        player?.playWhenReady = false
                    }

                    AudioManager.AUDIOFOCUS_GAIN -> {
                        player?.playWhenReady = true

                    }
                }
            }
        val result: Int = audioManager.requestAudioFocus(
            afChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        player?.playWhenReady = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }


    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT < 24 || player== null ) {
            initializePlayer(lectUrl!!)
            showVideoView()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < 24) {
            releasePlayer()
        }
    }


}