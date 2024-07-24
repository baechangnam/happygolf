package com.artilearn.happygolfgo.ui.home.record

import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.SWING_ROUND_DETAIL_ID
import com.artilearn.happygolfgo.databinding.ActivityTrainingRecordRoundDetailBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.util.convertTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingRecordRoundDetailActivity :
    BaseActivity<ActivityTrainingRecordRoundDetailBinding>(R.layout.activity_training_record_round_detail)
{
    private val viewModel: TrainingRecordRoundDetailViewModel by viewModel()
    private val gmID by lazy { intent.getIntExtra(SWING_ROUND_DETAIL_ID, 0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
        viewModel.requestRoundDetail(gmID)
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            roundDetail.observe(this@TrainingRecordRoundDetailActivity, Observer { roundDetail->
                binding.roundDetail = roundDetail;
            })

            serverError.observe(this@TrainingRecordRoundDetailActivity, Observer { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            })
            networkFail.observe(this@TrainingRecordRoundDetailActivity, Observer {
                showNetworkFail()
            })
            appRefresh.observe(this@TrainingRecordRoundDetailActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun initView() {
        initToolbar(binding.detailToolbar.toolbar, getString(R.string.training_record_score_card_toolbar_title), true)
    }

    private fun convertLessonLogTime(logCreateTime: String): String {
        val calendar = convertTime(logCreateTime).trim().split(":")

        return "${calendar[0]}.${calendar[1]}.${calendar[2]}"
    }

//    override fun onStart() {
//        super.onStart()
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//    }
}