package com.artilearn.happygolfgo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.constants.FCM_EVENT_TYPE
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.databinding.ActivityHomeBinding
import com.artilearn.happygolfgo.ui.home.alram.AlramFragment
import com.artilearn.happygolfgo.ui.home.mypage.MyPageFragment
import com.artilearn.happygolfgo.ui.home.record.TrainingRecordFragment
import com.artilearn.happygolfgo.ui.home.reservation.ReservationFragment
import com.artilearn.happygolfgo.ui.home.lecture.LectureFragment
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private val vm: HomeViewModel by viewModel()

    private lateinit var reservationFragment: ReservationFragment
    private lateinit var alramFragment: AlramFragment
    private lateinit var myPageFragment: MyPageFragment
    private lateinit var trainingRecordFragment:TrainingRecordFragment
    private lateinit var lectureFragment:LectureFragment

    private val fcmEventType by lazy { intent.getStringExtra(FCM_EVENT_TYPE) }
    private val reservationType by lazy { intent.getStringExtra(RESERVATION_TYPE) }
    private val lessonId by lazy { intent.getIntExtra(LESSON_ID, 0) }
    private val plateId by lazy { intent.getIntExtra(PLATE_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserving()
        setSupportActionBar((binding.homeToolbar.toolbar))
        startRhythmCoachService()
    }

    private fun initBinding() {
        binding.vm = vm
    }

    private fun initViewModelObserving() {
        with(vm) {
            backPressedEvent.observe(this@HomeActivity) {
                showAppFinish()
            }
            eventType.observe(this@HomeActivity) {
                when (eventType.value) {
                    HomeViewModel.FcmTypeMoveModel.PLATE -> goReservaitonPlateFromFCM()
                    HomeViewModel.FcmTypeMoveModel.LESSON -> goReservationLessonFromFCM()
                    HomeViewModel.FcmTypeMoveModel.LOG -> goLessonLogFromFCM()
                    else -> {}  // do nothing
                }
            }
            bottom.observe(this@HomeActivity) {
                toolbarText(it)
                initFragment(it)
            }
        }
    }

    private fun toolbarText(name: String) {
        var title = getString(R.string.home_toolbar_text)

        binding.homeToolbar.toolbar.visibility = View.VISIBLE
        binding.homeToolbar.logInToolbar.visibility = View.GONE

        when (name) {
            "예약" ->  {
                binding.homeToolbar.toolbar.visibility = View.GONE
                binding.homeToolbar.logInToolbar.visibility = View.VISIBLE
                 title = getString(R.string.home_toolbar_text)
            }
            "알림" -> {
                title = getString(R.string.alram_toolbar_title)
            }
            "마이페이지" -> {
                 title = getString(R.string.mypage_toolbar_title)
            }
            "훈련실" -> {
                title = getString(R.string.training_title_top1)
            }
            "기록실" -> {
                title = getString(R.string.game_record_toolbar_title)
            }
        }

        initToolbar(binding.homeToolbar.toolbar, title, false)
    }

    //comments: 홈화면에서 bottom 메뉴 아이템을 눌럭을때, 프레그먼트를 고체해준다.
    private fun initFragment(name: String) {
        when (name) {
            "예약" -> {
                reservationFragment = ReservationFragment()
                loadFragment(reservationFragment)
            }
            "알림" -> {
                alramFragment = AlramFragment()
                loadFragment(alramFragment)
            }
            "훈련실" -> {
               // val bottomSheet = CommnetListDialog(this)
                //bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                lectureFragment = LectureFragment()
                loadFragment(lectureFragment)
            }
            "마이페이지" -> {
                myPageFragment = MyPageFragment()
                loadFragment(myPageFragment)
            }
            //comments: vc45
            "기록실" -> {
//                DELME
//                rhythmCoachFragment = RhythmCoachFragment()
//                loadFragment(rhythmCoachFragment)
                trainingRecordFragment = TrainingRecordFragment()
                loadFragment(trainingRecordFragment)
            }
        }
    }

    private fun menuIsCheckItem(name: String) {
        //comments:  check menuIsCheckItem
        with(binding.bottomNavigation) {
            when (name) {
                "예약" -> menu.findItem(R.id.reservation).isChecked = true
                //"게임센터" -> menu.findItem(R.id.game center).isChecked = true
                "훈련실" -> menu.findItem(R.id.training).isChecked = true
                "알림" -> menu.findItem(R.id.alram).isChecked = true
                "마이페이지" -> menu.findItem(R.id.mypage).isChecked = true
                "기록실" -> menu.findItem(R.id.record).isChecked = true //adding rhythm coach
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        var tagName = ""
        when (fragment) {
            is ReservationFragment -> tagName = "예약"
            is AlramFragment -> tagName = "알림"
            is LectureFragment -> tagName = "훈련실"
            is MyPageFragment -> tagName = "마이페이지"
//            DELME
//            is RhythmCoachFragment -> tagName = "기록실" //adding rhythm coach
            is TrainingRecordFragment -> tagName = "기록실" //adding rhythm coach
        }

        menuIsCheckItem(tagName)
        fragmentManager.popBackStack(tagName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        transaction.apply {
            replace(R.id.frame_layout, fragment, tagName)
            addToBackStack(tagName)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
            isAddToBackStackAllowed
        }
    }

    private fun backButtonBottomNavigationListener() {
        val reservation = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_reservation))
        val training = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_training))
        val alram = supportFragmentManager.findFragmentByTag(getString(R.string.training_title_top1))
        val mypage = supportFragmentManager.findFragmentByTag(getString(R.string.home_tag_name_mypage))
        //comments: vc45
        val rhythmCoach = supportFragmentManager.findFragmentByTag("기록실")

        when {
            reservation != null && reservation.isVisible -> {
                toolbarText("예약")
                menuIsCheckItem("예약")
            }
            alram != null && alram.isVisible -> {
                toolbarText("알림")
                menuIsCheckItem("알림")
            }
            training != null && training.isVisible -> {
                toolbarText("훈련실")
                menuIsCheckItem("훈련실")
            }
            mypage != null && mypage.isVisible -> {
                toolbarText("마이페이지")
                menuIsCheckItem("마이페이지")
            }
            rhythmCoach != null && rhythmCoach.isVisible -> {
                toolbarText("기록실")
                menuIsCheckItem("기록실")
            }
        }
    }

    private fun showAppFinish() {
        val dialog = AlertDialog.Builder(this@HomeActivity)
        dialog.setTitle(getString(R.string.home_app_finish_title))
            .setPositiveButton(getString(R.string.home_app_finish_positive)) { _, _ ->
                stopRhythmCoachService()
                this.finish() }
            .setNegativeButton(getString(R.string.home_app_finish_negative)) { _, _ ->  }
        dialog.create()
        dialog.show()
    }

    private fun goAlramFCM() {
        vm.onNextBottomEvent(getString(R.string.alram_toolbar_title))
    }

    private fun goReservaitonPlateFromFCM() {
        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(PLATE_ID, plateId)
            }
        startActivity(fcmIntent)
    }

    private fun goReservationLessonFromFCM() {
        val fcmIntent = Intent(this, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationType)
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(fcmIntent)
    }

    private fun goLessonLogFromFCM() {
        val logIntent = Intent(this, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(logIntent)
    }

    override fun onStart() {
        super.onStart()
        //feature/20211222/migration
        //Smart cas to 'String' is impossible, because  'fcmEventType' is a property that has open or custom getter
        //so, save it to local temp variable and use it
        when (val  typedFcmEventType : String? =  fcmEventType) {
            "3", "4", "6" -> vm.fcmOnNext(typedFcmEventType)
            "" -> goAlramFCM()
        }
    }
// DONETODO: 백버튼 처리
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            vm.backPressed()
        } else {
            super.onBackPressed()
            backButtonBottomNavigationListener()
            vm.onNextBottomEvent("음")
        }
    }

    private fun startRhythmCoachService() {
//       2022.02.15 리듬 코치 보류 중
//        KoinApplication.mInstance.initRhythmCoachServiceInterface()
    }

    private fun stopRhythmCoachService() {
//        2022.02.15 리듬 코치 보류 중
//        KoinApplication.mInstance.mRhythmCoachServiceInterface?.stopPlay()
//        KoinApplication.mInstance.mRhythmCoachServiceInterface?.unbindRhythmCoachService()
//        KoinApplication.mInstance.mRhythmCoachServiceInterface = null
    }

    override fun onDestroy() {
        stopRhythmCoachService()
        super.onDestroy()
    }

    public fun  onGotoTrainingCenter() {
        Log.d("oo", "onGotoTrainingCenter")
       vm.moveToTheTab("기록실")
    }
}