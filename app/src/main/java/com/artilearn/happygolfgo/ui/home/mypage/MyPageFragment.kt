package com.artilearn.happygolfgo.ui.home.mypage

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.PLAY_STORE
import com.artilearn.happygolfgo.databinding.FragmentMypageBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyCommentActivity
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyLikeActivity
import com.artilearn.happygolfgo.ui.home.lecture.LectureMyPickActivity
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.MyPageBottomFragment
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeActivity
import com.artilearn.happygolfgo.ui.lessonpostlist.LessonPostListActivity
import com.artilearn.happygolfgo.ui.login.LoginActivity
import com.artilearn.happygolfgo.ui.myinfo.MyInfoActivity
import com.artilearn.happygolfgo.ui.pwauth.PwAuthActivity
import com.artilearn.happygolfgo.ui.ticketmanager.TicketManagerActivity
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.common_button_view.view.*
import kotlinx.android.synthetic.main.common_view.view.*
import kotlinx.android.synthetic.main.common_view.view.parent_card
import kotlinx.android.synthetic.main.common_view.view.tv_text
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPageFragment
    : BaseFragment<FragmentMypageBinding, MyPageViewModel>(R.layout.fragment_mypage) {

    override val viewModel: MyPageViewModel by viewModel()
    private val profileChangeFragment by lazy(::MyPageBottomFragment)

    override fun init() {

        initBinding()
        initViewModelObserving()
        initView()
        initClickListener()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            message.observe(this@MyPageFragment, Observer { message ->
                showFragmentToast(ToastType.WARNNING, message = message)
            })
            eventType.observe(this@MyPageFragment, Observer {
                when (eventType.value) {
                    MyPageViewModel.MyPageStateType.LOGOUT -> goLogin()
                    MyPageViewModel.MyPageStateType.NECESSARY_UPDATE -> hideUpdate()
                    MyPageViewModel.MyPageStateType.NEED_UPDATE -> showUpdate()
                    MyPageViewModel.MyPageStateType.PLAY_STORE -> goPlayStore()
                    MyPageViewModel.MyPageStateType.PERMISSION -> checkPermission()
                    else -> goLogin() //when expression must be exhaustive
                }
            })
            appVersion.observe(this@MyPageFragment, Observer { version ->
                binding.tvVersion.text = String.format("v $version")
            })
            networkFail.observe(this@MyPageFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            appRefresh.observe(this@MyPageFragment, Observer {
                goRefreshFromFragment()
            })
        }
    }

    private fun initView() {
        with(binding) {
            includeInfor.tv_text.text = getString(R.string.mypage_info_change)
            includeRecord.tv_text.text = getString(R.string.mypage_lesson_title)
            includePassword.tv_text.text = getString(R.string.mypage_password_change)
            includeLogout.tv_text.text = getString(R.string.mypage_logout)
            //includeTicket.tv_text.text = "이용권 관리"
            includeUpdate.tv_text.text = getString(R.string.mypage_update)
            includeUpdate.tv_text.setTextColor(ContextCompat.getColor(includeUpdate.tv_text.context, R.color.color_mypage_update))
            includeUpdate.parent_card.setBackgroundResource(R.drawable.bg_mypage_update)


            includeMyPickLecture.tv_text.text = "담아둔 강의"
            includeMyComment.tv_text.text = "내 댓글"
            includeLikeLecture.tv_text.text = "좋아요한 강의"

            includeRecord.iv_icon.setBackgroundResource(R.drawable.ico_my_lesson)
            includeInfor.iv_icon.setBackgroundResource(R.drawable.ico_my_info)
            includePassword.iv_icon.setBackgroundResource(R.drawable.ico_my_password)
            includeLogout.iv_icon.setBackgroundResource(R.drawable.ico_my_logout)

            includeMyPickLecture.iv_icon.setBackgroundResource(R.drawable.ico_my_save)
            includeMyComment.iv_icon.setBackgroundResource(R.drawable.ico_my_comment)
            includeLikeLecture.iv_icon.setBackgroundResource(R.drawable.ico_my_like)
        }
    }

    private fun initClickListener() {
        with(binding) {
            includeUpdate.setOnClickListener { viewModel.appUpdateOnNext() }
            includeInfor.setOnClickListener { goMyInfo() }
            includeRecord.setOnClickListener { goLessonLog() }
            includeLogout.setOnClickListener { viewModel.logout() }
            includePassword.setOnClickListener { goPwAuth() }
            includeMyPickLecture.setOnClickListener{goFavor()}
            includeLikeLecture.setOnClickListener{goLike()}

            includeMyComment.setOnClickListener{goMyComment()}

            //includeTicket.setOnClickListener { goTicketManager() }
        }
    }

    private fun showUpdate() {
        binding.updateLayout.visibility = View.VISIBLE
    }

    private fun hideUpdate() {
        binding.updateLayout.visibility = View.GONE
    }

    private fun goLogin() {
        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }

    private fun goPlayStore() {
        val playStore = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PLAY_STORE)
            setPackage("com.android.vending")
        }
        startActivity(playStore)
    }

    private fun goMyInfo() {
        startActivity(Intent(activity, MyInfoActivity::class.java))
    }

    private fun goLike() {
        startActivity(Intent(activity, LectureMyLikeActivity::class.java))
    }

    private fun goMyComment() {
        startActivity(Intent(activity, LectureMyCommentActivity::class.java))
    }

    private fun goFavor() {
        startActivity(Intent(activity, LectureMyPickActivity::class.java))
    }


    private fun goPwAuth() {
        startActivity(Intent(activity, PwAuthActivity::class.java))
    }

    private fun goLessonLog() {
        startActivity(Intent(activity, LessonPostListActivity::class.java))
    }

    private fun  goTicketManager() {
        startActivity(Intent(activity, TicketManagerActivity::class.java))
    }

    private fun checkPermission() {
        //TODO: behavior change : app targeting android 13 or higher
        TedRx2Permission.with(this.context)
            .setPermissions(
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            )
            .request()
            .subscribe({ result ->
                if (result.isGranted) {
                    startActivity(Intent(requireActivity(), ProfileChangeActivity::class.java))
                }
            }, {
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMyInfo()
    }

}