package com.artilearn.happygolfgo.ui.home.record

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.databinding.ActivityTrainingRecordSwingVideoPlayerBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.util.setImage
import com.artilearn.happygolfgo.util.setSwingPreviewImage
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.common_default_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

//activity_training_record_swing_video_player
class TrainingRecordSwingVideoPlayerActivity
    : BaseActivity<ActivityTrainingRecordSwingVideoPlayerBinding>(R.layout.activity_training_record_swing_video_player)
{

    private val viewModel: TrainingRecordSwingVideoPlayerViewModel by viewModel()
    private val swingVideoUrl by lazy { intent.getStringExtra(SWING_VIDEO_URL) }
    private val swingVideoThumbNailUrl by lazy { intent.getStringExtra(SWING_VIDEO_THUMBNAIL_URL) }
    private val swingRecordModel by lazy { intent.getParcelableExtra<TRSummarySwingVideoRecordModel>(SWING_VIDEO_DETAIL) }

    private var playReady = false
    private var player: ExoPlayer? = null
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var  clickableDelete = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModelObserve()
        initView()
        initOnClickListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
        binding.videoRecord = swingRecordModel
        viewModel.requestVideoUrl("")
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            playVideo.observe(this@TrainingRecordSwingVideoPlayerActivity, Observer { isReady ->
                showVideoView()
            })

            deleteVideoUrl.observe(this@TrainingRecordSwingVideoPlayerActivity,Observer { deleteVideo ->
                if( deleteVideo == true) {
                    closeSwingVideoDetail()
                } else {
                     clickableDelete = true;
                }
            })

            networkFail.observe(this@TrainingRecordSwingVideoPlayerActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@TrainingRecordSwingVideoPlayerActivity, Observer {
                showToast(ToastType.ERROR, it)
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
            showPlaybackController();
        }
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
            hidePlaybackController();
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
//        initToolbar(binding.swingVideoToolbar.toolbar, getString(R.string.swing_video_player_toolbar_title), true)
        initToolbar(binding.swingVideoToolbar.toolbar, getString(R.string.swing_video_player_toolbar_title), true)
    }

    private fun initOnClickListener() {
        with(binding)  {
            btnSwingVideoDetailDelete.setOnClickListener {
                  onClickDeleteVideo()
            }
            btnSwingVideoDetailDownLoad.setOnClickListener {
                 onClickDownload()
            }
        }
    }

    private fun initVideoPlayer() {
        swingVideoUrl?.let {
            if (it.isNotEmpty()) {
                initializePlayer(it)
            }
       }
    }

//    private fun setLessonLogText(log: LogDetail) {
//        with(binding) {
//            logName.tv_common.text = log.employee.name
//            logDate.tv_common.text = convertLessonLogTime(log.createdAt)
//        }
//    }

//    private fun convertLessonLogTime(logCreateTime: String): String {
//        val calendar = convertTime(logCreateTime).trim().split(":")
//
//        return "${calendar[0]}.${calendar[1]}.${calendar[2]}"
//    }

//    private fun setLessonLogItem(log: LogDetail) {
//        with(log) {
//            loadThumbNail(log.thumbnailURL, log.body)
//
//            if (isReviewed) {
//                setMyCommet(log.comment)
//            } else {
//                if (isReviewable) {
//                    underIsReviewable()
//                } else {
//                    overIsReviewable()
//                }
//            }
//        }
//    }

    private fun loadThumbNail(thumbNail: String?) {
        if (thumbNail.isNullOrEmpty()) {
            binding.videoLayout.visibility = View.GONE
        } else {
            with(binding.ivThumbnail) {
//                setImage(this, thumbNail)
                setSwingPreviewImage(this, thumbNail)
            }
        }
    }

    private fun showMessagePopupDelete() {
        val dialog = AlertDialog.Builder(this@TrainingRecordSwingVideoPlayerActivity)
        dialog.setTitle("영상 삭제")
            .setMessage("영상을 삭제 하시겠습니까 ?")
            .setPositiveButton("삭제하기") { _, _ ->
                requestUpdateSwingVideoType()
            }
            .setNegativeButton("취소") { _, _ ->  clickableDelete = true  }
        dialog.create()
        dialog.show()
    }

    private fun onClickDeleteVideo() {
        try {
            if (clickableDelete) {
                showMessagePopupDelete();
            }
        } catch (e : Exception) {
            showToast(ToastType.ERROR, "예외 상황이 발생하였습니다. ")
        }
    }

    private fun requestUpdateSwingVideoType()  {
        clickableDelete = false
        if (swingRecordModel != null ) {
            val id = swingRecordModel!!.id;
            val position = swingRecordModel!!.videoPosition;
            val type = if (position == "정면") {
                1
            } else {
                2
            }
            binding.vm?.requestUpdateSwingVideoType(id, type);
        } else {
            clickableDelete = true;
            showToast(ToastType.ERROR, "스윙 데이타 상세 점보에 오류가 있습니다.")
        }
    }

    private fun closeSwingVideoDetail() {
        clickableDelete = false
        val intent = Intent(this, TrainingRecordSwingVideoListActivity::class.java)
            .apply {
                putExtra(SWING_VIDEO_RECORD_DELETE, SWING_VIDEO_RECORD_DELETE)
            }
        setResult(RESULT_OK, intent);
        if(!isFinishing) {
            finish();
        }
    }

    private fun onClickDownload() {
        showMessagePopupDownload()
    }

    private fun startDownloadingVideo() {
        // After API 23 (Marshmallow) and lower Android 10 you need to ask for permission first before save an image
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        } else {
            downloadImage(swingVideoUrl)
        }
    }

    private fun showMessagePopupDownload() {
        val dialog = AlertDialog.Builder(this@TrainingRecordSwingVideoPlayerActivity)
        dialog.setTitle("영상 저장")
            .setMessage("동영상을 저장하시겠습니까 ?")
            .setPositiveButton("저장하기") { _, _ ->
                startDownloadingVideo();
            }
            .setNegativeButton("취소") { _, _ ->  clickableDelete = true  }
        dialog.create()
        dialog.show()
    }

    /*
    private fun downloadImageWithCoroutine(url: String?) {
        if (url == null) {
            showToast(ToastType.ERROR, "동영상 경로를 찾을 수 없습니다.")
            return ;
        }
        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("abc")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    url.substring(url.lastIndexOf("/") + 1)
                )

        }
        //use when just to download the file with getting status
        //downloadManager.enqueue(request)
        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        lifecycleScope.launchWhenStarted {
            var lastMsg: String = ""
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val msg: String? = statusMessage(url, File(Environment.DIRECTORY_DOWNLOADS), status)
                Log.e("DownloadManager", " Status is :$msg")
                if (msg != lastMsg) {
                    withContext(Dispatchers.Main) {
                        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        text_view.text = msg
                        //Log.e("DownloadManager", "Status is :$msg")
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }
    }
   */

    @SuppressLint("Range")
    private fun downloadImage(url: String?) {
        if (url == null) {
            showToast(ToastType.ERROR, "동영상 경로를 찾을 수 없습니다.")
            return
        }
        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("abc")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    url.substring(url.lastIndexOf("/") + 1)
                )

        }
        //use when just to download the file with getting status
        //downloadManager.enqueue(request)
        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)

        lifecycleScope.launchWhenStarted {
            var lastMsg: String = ""
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val msg: String? = statusMessage(url, File(Environment.DIRECTORY_DOWNLOADS), status)
                Log.e("DownloadManager", " Status is :$msg")
                if (msg != lastMsg) {
                    withContext(Dispatchers.Main) {
                        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        if (msg == "SUCCESS") {
                            showToast(ToastType.SUCCESS, "다운로드가 완료되었습니다");
                        } else if ( msg == "ERROR") {
                        showToast(ToastType.ERROR, "다운로드에 실패하였습니다.");
                    }
//                        text_view.text = msg
                        //Log.e("DownloadManager", "Status is :$msg")
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "ERROR" //"Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "PAUSED" //"Paused"
            DownloadManager.STATUS_PENDING -> "PENDING"//"Pending"
            DownloadManager.STATUS_RUNNING -> "RUNNING"//"Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "SUCCESS"
            //"PDF downloaded successfully in $directory" + File.separator + url.substring(url.lastIndexOf("/") + 1)
            else -> "ERROR"//"There's nothing to download"
        }
        return msg
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(swingVideoUrl)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage(swingVideoUrl)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadThumbNail(swingVideoThumbNailUrl)
        initVideoPlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}