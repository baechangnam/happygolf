package com.artilearn.happygolfgo.ui.comment

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.COACH_NAME
import com.artilearn.happygolfgo.constants.LESSON_DATE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.databinding.ActivityCommetBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.util.convertTime
import com.artilearn.happygolfgo.util.snackbar
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentActivity : BaseActivity<ActivityCommetBinding>(R.layout.activity_commet) {

    private val viewModel: CommentViewModel by viewModel()
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val coachName by lazy { intent.getStringExtra(COACH_NAME) }
    private val lessonDate by lazy { intent.getStringExtra(LESSON_DATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
        initTextListener()
        initRatingbarListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
        viewModel.lessonIdOnNext(lessonId)
    }

    private fun initViewModelObserve() {
        with(viewModel) {
            success.observe(this@CommentActivity) {
                goMyPage()
            }
            networkFail.observe(this@CommentActivity){
                showNetworkFail()
            }
            serverError.observe(this@CommentActivity) { errorMsg ->
                showToast(ToastType.ERROR, errorMsg)
            }
            commentType.observe(this@CommentActivity) {
                when (commentType.value) {
                    CommentViewModel.CommentEmptyType.EMPTY_COMMENT -> showEmptyComment()
                    CommentViewModel.CommentEmptyType.EMPTY_RATING -> showEmptyRating()
                    else -> showEmptyComment() //when expression must be exhaustive 2023/02/13
                }
            }
            appRefresh.observe(this@CommentActivity){
                goRefresh()
            }
        }
    }

    private fun initView() {
        initToolbar(binding.commentToolbar.toolbar, getString(R.string.log_toolbar_title), true)
        initBottomView(binding.commentBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
        //feature/20211222/migration
        //kotling version 1.6.0 now, Typemismatch error comes out
        setCoachNameLessonTime(coachName, lessonDate)
    }

    private fun initTextListener() {
        binding.etComment.addTextChangedListener { viewModel.commentOnNext(it.toString()) }
    }

    private fun initRatingbarListener() {
        binding.ratingbar.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.ratingOnNext(rating)
        }
    }
    //featue/20211222/migration
    private fun setCoachNameLessonTime(name: String?, date: String?) {
        if (name !=  null && date != null) {
            val time = convertTime(date).trim().split(":")
            val text = "${time[0]}.${time[1]}.${time[2]}"
            binding.tvCoachInfo.text = text
        } else {
            binding.tvCoachInfo.text = "ER00  name and date null"
        }
    }

    private fun showEmptyRating() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_rating))
    }

    private fun showEmptyComment() {
        binding.commentLayout.snackbar(getString(R.string.comment_empty_comment))
    }

    private fun goMyPage() {
        showToast(ToastType.SUCCESS, getString(R.string.comment_upload_success))
        val myPageIntent = Intent(this, HomeActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        startActivity(myPageIntent)
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.bottomOnNext()
    }

}