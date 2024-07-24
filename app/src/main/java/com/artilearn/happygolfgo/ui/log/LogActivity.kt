package com.artilearn.happygolfgo.ui.log

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.COACH_NAME
import com.artilearn.happygolfgo.constants.LESSON_DATE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.data.LogDetail
import com.artilearn.happygolfgo.databinding.ActivityLogBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.comment.CommentActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.setImage
import com.artilearn.happygolfgo.util.setLessonLogItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.android.synthetic.main.common_default_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogActivity : BaseActivity<ActivityLogBinding>(R.layout.activity_log) {

    private val viewModel: LogViewModel by viewModel()
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }

    private var playReady = false
    private var player: ExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var log: LogDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
        viewModel.requestLog(lessonId)
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            logDetail.observe(this@LogActivity, Observer {
                binding.log = it
                setLessonLogItem(it)
                setLessonLogText(it)

                log = it
                log?.videoURL?.let { path ->
                    initializePlayer(path)
                }
            })
            videoUrl.observe(this@LogActivity, Observer { url ->
                showVideoView()
            })
            serverError.observe(this@LogActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            })
            networkFail.observe(this@LogActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@LogActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun showVideoView() {
        playReady = true

        playbackPosition = player!!.currentPosition
        currentWindow = player!!.currentWindowIndex
        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)

        with(binding) {
            ivThumbnail.visibility = View.GONE
            ivVideoStart.visibility = View.GONE
            exoplayer.visibility = View.VISIBLE
        }
    }

    private fun videoStateReady() {
        player?.playWhenReady = playReady
    }

    private fun initializePlayer(path: String) {
        player = ExoPlayer.Builder(this).build()
        binding.exoplayer.player = player

        val uri = path.toUri()
        val dataSourceFactory = DefaultDataSourceFactory(this, "happygolf_pro")
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)


        val exoEventListener = object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> videoStateReady()
                    Player.STATE_READY -> {}
                    Player.STATE_ENDED -> videoStateEnd()
                    Player.STATE_IDLE -> showToast(
                        ToastType.ERROR,
                        getString(R.string.log_video_start_fail)
                    )
                }

                super.onPlayerStateChanged(playWhenReady, playbackState)
            }
        }
        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)
        player?.addListener(exoEventListener)
    }

    private fun videoStateEnd() {
        playReady = false
        currentWindow = 0
        playbackPosition = 0L

        player?.playWhenReady = playReady
        player?.seekTo(currentWindow, playbackPosition)

        with(binding) {
            ivThumbnail.visibility = View.VISIBLE
            ivVideoStart.visibility = View.VISIBLE
            exoplayer.visibility = View.INVISIBLE
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

    private fun initView() {
        initToolbar(binding.logToolbar.toolbar, getString(R.string.log_toolbar_title), true)
    }

    private fun setLessonLogText(log: LogDetail) {
        with(binding) {
            logName.tv_common.text = log.employee.name
            logDate.tv_common.text = convertLessonLogTime(log.createdAt)
        }
    }

    private fun convertLessonLogTime(logCreateTime: String): String {
        val calendar = convertTime(logCreateTime).trim().split(":")

        return "${calendar[0]}.${calendar[1]}.${calendar[2]}"
    }

    private fun setLessonLogItem(log: LogDetail) {
        with(log) {
            loadThumbNail(log.thumbnailURL, log.body)

            if (isReviewed) {
                setMyCommet(log.comment)
            } else {
                if (isReviewable) {
                    underIsReviewable()
                } else {
                    overIsReviewable()
                }
            }
        }
    }

    private fun loadThumbNail(thumbNail: String?, body: String) {
        if (thumbNail.isNullOrEmpty()) {
            binding.videoLayout.visibility = View.GONE
        } else {
            with(binding.ivThumbnail) {
                setImage(this, thumbNail)
            }
        }
        binding.tvBody.text = body
    }

    private fun setMyCommet(commet: String?) {
        commet?.let {
            with(binding) {
                logCommentTitle.tv_common.text = getString(R.string.log_comment_tile)
                tvComment.text = commet
                logBottomView.visibility = View.GONE
            }
        }
    }

    private fun overIsReviewable() {
        with(binding) {
            tvComment.visibility = View.GONE
            logBottomView.visibility = View.GONE
            logCommentTitle.tv_common.apply {
                setTextColor(ContextCompat.getColor(this.context, R.color.color_sub_gray))
                text = getString(R.string.log_over_comment)
            }
        }
    }

    private fun underIsReviewable() {
        with(binding) {
            logBottomView.visibility = View.VISIBLE
            logCommentTitle.visibility = View.GONE
            tvComment.visibility = View.GONE
            initBottomView(
                logBottomView,
                getString(R.string.log_bottom_view_title),
                BottimViewType.DEFAULT_TYPE
            )
        }
    }

    private fun goComment(log: LogDetail) {
        val commentIntent = Intent(this, CommentActivity::class.java)
            .apply {
                putExtra(LESSON_ID, log.id)
                putExtra(COACH_NAME, log.employee.name)
                putExtra(LESSON_DATE, log.createdAt)
            }
        startActivity(commentIntent)
    }

    override fun bottomAction() {
        super.bottomAction()
        log?.let {
            goComment(it)
        }
    }

    override fun onStart() {
        super.onStart()
        log?.videoURL?.let {
            initializePlayer(it)
        }
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}