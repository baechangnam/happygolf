package com.artilearn.happygolfgo.ui.home.record

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.*
import com.artilearn.happygolfgo.data.LessonLog
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.record.model.TRSummarySwingVideoRecordModel
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListAdapter
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListViewModel
import com.artilearn.happygolfgo.ui.log.LogActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TrainingRecordSwingVideoListActivity :
    BaseActivity<com.artilearn.happygolfgo.databinding.ActivityTrainingRecordSwingVideoListBinding>
        (R.layout.activity_training_record_swing_video_list)
{
    private val viewModel: TrainingRecordSwingVideoListViewModel by viewModel()
    private lateinit var adapter: SwingVideoListAdapter
    var clickable = true;
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initAdapter()
        initActivityLauncher()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            networkFail.observe(this@TrainingRecordSwingVideoListActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@TrainingRecordSwingVideoListActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            //refresh access token
            appRefresh.observe(this@TrainingRecordSwingVideoListActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.trainingRecordSwingVideoListToolbar.toolbar, getString(R.string.training_record_swing_list_toolbar_title), true)
    }

    private fun initAdapter() {
        adapter = SwingVideoListAdapter {   trSummarySwingVideoRecordModel ->
            goSwingVideoPlayer(trSummarySwingVideoRecordModel)
        }
        binding.recyclerview.adapter = adapter
    }

    private fun initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val address = it.data?.getStringExtra(SWING_VIDEO_RECORD_DELETE) ?: ""
                if (address == SWING_VIDEO_RECORD_DELETE) {
                    refreshApp();
                }
                //tvMain.text = address
            }
        }
    }

    private fun refreshApp() {
        showToast(ToastType.SUCCESS,"새로고침")
        with(viewModel) {
             refreshSwingList();
        }
    }

    private fun goSwingVideoPlayer(videoRecord : TRSummarySwingVideoRecordModel ) {
        if (clickable) {
            clickable = false
            val swingVideoIntent = Intent(this, TrainingRecordSwingVideoPlayerActivity::class.java)
                .apply {
                    putExtra(SWING_VIDEO_DETAIL, videoRecord)
                    putExtra(SWING_VIDEO_URL, videoRecord.svrUrl1 ?: "")
                    putExtra(SWING_VIDEO_THUMBNAIL_URL, videoRecord.svrUrlThumbNail1 ?: "")
                }
            //startActivity(swingVideoIntent)
            //startActivityForResult is deprecated
            activityResultLauncher.launch(swingVideoIntent);
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    clickable = true;
                }
            }, 1000)
        }
    }
}