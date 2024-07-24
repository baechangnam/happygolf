package com.artilearn.happygolfgo.ui.home.lecture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.data.Lecture
import com.artilearn.happygolfgo.data.lecture.LectureDetailResponse
import com.artilearn.happygolfgo.databinding.ActivityLectureDetailBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.common.ErrorActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.exo_playback_control_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class LectureDetailActivity :
    BaseActivity<ActivityLectureDetailBinding>(R.layout.activity_lecture_detail) {

    private val viewModel: LectureDetailViewModel by viewModel()
    private val lectNo by lazy { intent.getStringExtra("lectNo") }
    private val lectUrl by lazy { intent.getStringExtra("lectUrl") }

    private var playReady = false
    private var player: ExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L

    private var isFavor : Boolean = false;
    private var isLike : Boolean = false;

    private lateinit var adapter: LectureDetailAdapter
    private lateinit var adapterSameLecture : LectureDetailAdapter
    private lateinit var recommLectureList: List<Lecture>
    private lateinit var sameLectureList: List<Lecture>
    private lateinit var lectTitle: String

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                playbackPosition = it.data!!.getLongExtra("playbackPos",0L)

            }
        }

        binding.exoplayer.exo_fullscreen_icon.setOnClickListener{
            val detailActivity = Intent(baseContext, FullScreenPlayerActivity::class.java)
                .apply {
                    putExtra("lectNo", lectNo)
                    putExtra("lectUrl", lectUrl)
                    putExtra("playbackPosition", player!!.currentPosition)
                    putExtra("lectTitile", lectTitle)
                }

            activityResultLauncher.launch(detailActivity)
        }


        lectNo?.let { viewModel.getLectureDetail(it) }
    }

    private fun initBinding() {
        binding.vm = viewModel

        binding.exoplayer.exo_rew.setImageResource(R.drawable.btn_player_prev)
        binding.exoplayer.exo_ffwd.setImageResource(R.drawable.btn_player_next)
        binding.exoplayer.exo_play.setImageResource(R.drawable.btn_player_play)
        binding.exoplayer.exo_pause.setImageResource(R.drawable.btn_player_pause)

    }


    private fun initViewModelObserve() {
        with(viewModel) {
            _lecture.observe(this@LectureDetailActivity, Observer {
                if (it!!.rsc_url != null) {

                    lectTitle = it.lect_title
                    setDataView(it)
                    setRecommendLecture(it)
                    setSameSectLecture(it)
                }

            })

            sectLecture.observe(this@LectureDetailActivity, Observer {

            })

            networkFail.observe(this@LectureDetailActivity, Observer {
                showNetworkFail()
            })
            serverError.observe(this@LectureDetailActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@LectureDetailActivity, Observer {
                goRefresh()
            })

            addBookMark.observe(this@LectureDetailActivity, Observer {
                if(it){

                }
            })
        }
    }

    private fun setDataView(data: LectureDetailResponse) {
        binding.lectTitle.text = data.lect_title
        binding.close.setOnClickListener{
            finish()
        }

        if (data.comments.isNotEmpty()) {
            binding.commentTitle.text = data.comments[0].cmmt_text
            binding.commentCount.text = data.comments.size.toString()
        }

        binding.llComment.setOnClickListener {
            val bottomSheet = CommentListDialog(this , data.lect_no)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        //담기여부
        if(data.like_yn=="Y"){
            isFavor=true
            binding.llFavor.setBackgroundResource(R.drawable.bookmark_bg_on)
            binding.llFavorIcon.setBackgroundResource(R.drawable.favor_ons)
            binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_reservation_plate_text_day))

        }else{
            isFavor=false
            binding.llFavor.setBackgroundResource(R.drawable.home_training_like_bg)
            binding.llFavorIcon.setBackgroundResource(R.drawable.favor)
            binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
        }


        //좋아요 여부
        if(data.favr_yn=="Y"){
            isLike=true
            binding.llLike.setBackgroundResource(R.drawable.home_training_like_bg)
            binding.llLikeIcon.setBackgroundResource(R.drawable.btn_like_on)
            binding.llLikeTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))

        }else{
            isLike=false
            binding.llLike.setBackgroundResource(R.drawable.home_training_like_bg)
            binding.llLikeIcon.setBackgroundResource(R.drawable.heart_off)
            binding.llLikeTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
        }


        binding.llLike.setOnClickListener {
            isLike = !isLike

            if(isLike){
                binding.llLike.setBackgroundResource(R.drawable.home_training_like_bg)
                binding.llLikeIcon.setBackgroundResource(R.drawable.btn_like_on)
                binding.llLikeTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
                viewModel.addLike(data.lect_no)
            }else{
                binding.llLike.setBackgroundResource(R.drawable.home_training_like_bg)
                binding.llLikeIcon.setBackgroundResource(R.drawable.heart_off)
                binding.llLikeTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
                viewModel.cancelLike(data.lect_no)
            }

        }

        binding.llFavor.setOnClickListener {
            isFavor = !isFavor

            if(isFavor){
                binding.llFavor.setBackgroundResource(R.drawable.bookmark_bg_on)
                binding.llFavorIcon.setBackgroundResource(R.drawable.favor_ons)
                binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_reservation_plate_text_day))
                viewModel.addPick(data.lect_no)
            }else{
                binding.llFavor.setBackgroundResource(R.drawable.home_training_like_bg)
                binding.llFavorIcon.setBackgroundResource(R.drawable.favor)
                binding.llFavorTxt.setTextColor(ContextCompat.getColor(binding.llFavorTxt.context, R.color.color_black))
                viewModel.cancelPick(data.lect_no)
            }

        }

//        binding.exoplayer.exo_fullscreen_icon.setOnClickListener{
//            if (fullscreen) {
//
//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//                if (supportActionBar != null) {
//                    supportActionBar!!.show()
//                }
//                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                val params = binding.exoplayer.getLayoutParams()
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT
//                params.height = (235 * applicationContext.resources.displayMetrics.density).toInt()
//                binding.exoplayer.layoutParams = params
//                fullscreen = false
//            } else {
//                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//                if (supportActionBar != null) {
//                    supportActionBar!!.hide()
//                }
//                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                val params = binding.exoplayer.layoutParams
//                params.width = ViewGroup.LayoutParams.MATCH_PARENT
//                params.height = ViewGroup.LayoutParams.MATCH_PARENT
//                binding.exoplayer.layoutParams = params
//                fullscreen = true
//            }
//        }

    }


    private fun setRecommendLecture(lectureDetailResponse: LectureDetailResponse) {
        recommLectureList = lectureDetailResponse.recommendLectures

        adapter = LectureDetailAdapter(recommLectureList,LectureItemClick(),"recomm")
        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.listRecommentLect.layoutManager = layoutManager


        with(binding) {
            binding.listRecommentLect.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            binding.listRecommentLect.adapter = adapter
        }
    }

    private fun setSameSectLecture(lectureDetailResponse: LectureDetailResponse) {
        sameLectureList = lectureDetailResponse.sectLectures

        adapterSameLecture = LectureDetailAdapter(sameLectureList,LectureItemClick(),"same")
        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.listSameLect.layoutManager = layoutManager


        with(binding) {
            binding.listSameLect.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            // swipeToDelete(recyclerview)
            binding.listSameLect.adapter = adapterSameLecture
        }
    }

    inner class LectureItemClick() {
        fun itemClick(pos: Int, tag: String, type: String) {
            when (tag) {
                "bookmark" -> {
                    if(type == "recomm"){
                        val lectNo =  recommLectureList[pos].lect_no
                        val favor_yn =  recommLectureList[pos].like_yn
                        if(favor_yn=="Y"){
                            binding.vm?.cancelLike(lectNo)
                            recommLectureList[pos].like_yn= "N"
                        }else{
                            binding.vm?.addLike(lectNo)
                            recommLectureList[pos].like_yn= "Y"
                        }
                        adapter.notifyDataSetChanged()
                    }else{
                        val lectNo =  sameLectureList[pos].lect_no
                        val favor_yn =  sameLectureList[pos].like_yn
                        if(favor_yn=="Y"){
                            binding.vm?.cancelLike(lectNo)
                            sameLectureList[pos].like_yn= "N"
                        }else{
                            binding.vm?.addLike(lectNo)
                            sameLectureList[pos].like_yn= "Y"
                        }
                        adapterSameLecture.notifyDataSetChanged()
                    }


                }
                else -> {
                    if(type == "recomm"){
                        val lectNo =  recommLectureList[pos].lect_no
                        val lectUrl =  recommLectureList[pos].rsc_url

                        val detailActivity = Intent(baseContext, LectureDetailActivity::class.java)
                            .apply {
                                putExtra("lectNo", lectNo)
                                putExtra("lectUrl", lectUrl)
                            }
                        startActivity(detailActivity)
                    }else{
                        val lectNo =  sameLectureList[pos].lect_no
                        val lectUrl =  sameLectureList[pos].rsc_url

                        val detailActivity = Intent(baseContext, LectureDetailActivity::class.java)
                            .apply {
                                putExtra("lectNo", lectNo)
                                putExtra("lectUrl", lectUrl)
                            }
                        startActivity(detailActivity)
                    }

                }
            }
        }
    }


    private fun showVideoView() {
        playReady = true

        playbackPosition = player!!.currentPosition
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

        with(binding) {
            //    ivThumbnail.visibility = View.VISIBLE
            // ivVideoStart.visibility = View.VISIBLE
         //   exoplayer.visibility = View.INVISIBLE
           // hidePlaybackController();
        }
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