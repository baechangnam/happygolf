package com.artilearn.happygolfgo.ui.home.reservation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.constants.MARKET_PLAY_STORE
import com.artilearn.happygolfgo.constants.PLAY_STORE
import com.artilearn.happygolfgo.constants.TICKET
import com.artilearn.happygolfgo.data.Ticket
import com.artilearn.happygolfgo.databinding.FragmentReservationBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.HomeActivity
import com.artilearn.happygolfgo.ui.home.record.BannerViewPagerAdapter
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.EXPIRED_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.PAUSE_TICKET
import com.artilearn.happygolfgo.ui.home.reservation.ReservationNewAdapter.Companion.USE_TICKET
import com.artilearn.happygolfgo.ui.reservelist.ReserveListActivity
import com.artilearn.happygolfgo.ui.ticketdate.TicketDateActivity
import com.artilearn.happygolfgo.util.convertDate
import com.artilearn.happygolfgo.util.getNowDate
import com.artilearn.happygolfgo.util.snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.logging.Handler

class ReservationFragment
    : BaseFragment<FragmentReservationBinding, ReservationViewModel>(R.layout.fragment_reservation) {

    private var ANNOUNCE_URI = ""
    private var package_name = "com.android.chrome"
    private lateinit var pagerAdapter: BannerViewPagerAdapter

    fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    private fun setupViewPagerAdapter() {
        pagerAdapter = BannerViewPagerAdapter {
            if(it.isOpenInNewWindow == "yes"){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.openUrl))
                startActivity(browserIntent)
            }
        }
        binding.vpBanner.adapter = pagerAdapter
    }

    private fun openWebBrowser() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ANNOUNCE_URI))
        startActivity(intent)
    }

    private fun goPlayStore() {
        val playStore = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PLAY_STORE)
            setPackage("com.android.vending")
        }
        startActivity(playStore)
    }

    private fun goPlayStoreByMarket() {
        val playStore = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(MARKET_PLAY_STORE)
        }
        startActivity(playStore)
    }
    private fun launchChromeTab() {
        try {
            if (ANNOUNCE_URI.isEmpty()) {
                return ;
            }
            val builder = CustomTabsIntent.Builder()
            // to set the toolbar color use CustomTabColorSchemeParams
            // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.color_white))
            builder.setDefaultColorSchemeParams(params.build())

            // shows the title of web-page in toolbar
            builder.setShowTitle(true)

            // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

            // To modify the close button, use
            // builder.setCloseButtonIcon(bitmap)

            // to set weather instant apps is enabled for the custom tab or not, use
            builder.setInstantAppsEnabled(true)

            //  To use animations use -
            //  builder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
            //  builder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
            val customBuilder = builder.build()

            if (requireActivity().isPackageInstalled(package_name)) {
                // if chrome is available use chrome custom tabs
                customBuilder.intent.setPackage(package_name)
                customBuilder.launchUrl(requireActivity(), Uri.parse(ANNOUNCE_URI))
            } else {
                openWebBrowser();
            }
        } catch (_:Exception) {

        }
    }

    override val viewModel: ReservationViewModel by viewModel()
    //private lateinit var adapter: ReservationAdapter
//    private lateinit var adapter: ReservationNewAdapter
    private lateinit var adapter: ReservationAdapterWithHeader
    private lateinit var todaysReservationAdapter: TodaysReservationAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
        setupViewPagerAdapter()
    }

    override fun onStart() {
        super.onStart()
        /**
         * 뷰모델에 있었지만 현재 급한 관계로 여기서 통신함...(전체)
         * 각 화면에서 유저 액션이 있을 경우 리시버로 해당 아이템모델을 교체해줄 필요
         * 혹은 캐싱데이터로 가지고 놀기, 여유있을 때 수정요망
         */
        viewModel.fetchData()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            reservationListButton.observe(viewLifecycleOwner, Observer {
                reservationListCheck(adapter.itemCount)
            })
            message.observe(viewLifecycleOwner, Observer {
                showFragmentToast(ToastType.WARNNING, it)
            })
            appRefresh.observe(viewLifecycleOwner, Observer {
                goRefreshFromFragment()
            })

            announcementClick.observe(viewLifecycleOwner, Observer {
                ANNOUNCE_URI = it;
                launchChromeTab();
            })

            scoreClick.observe(viewLifecycleOwner, Observer {
                (requireActivity() as HomeActivity).onGotoTrainingCenter();
            })

            retryExamPermission.observe(viewLifecycleOwner, Observer {
                showRetryExamMessage();
            })

            mobileVersionAndroid.observe(viewLifecycleOwner, Observer {
                checkMobileAndroidVersion();
            })

            showAnnouncement.observe(viewLifecycleOwner, Observer {
                binding.llAnnouncement.isVisible = it
            })
            liveBanner.observe(viewLifecycleOwner) {
                pagerAdapter.clear()
                pagerAdapter.setItems(it)
                android.os.Handler().postDelayed({
                    setViewPagerCount()
                },100)
            }
        }
    }

    private fun initAdapter() {
        adapter = ReservationAdapterWithHeader( viewModel) { ticket ->
            when (ticket.viewType) {
                USE_TICKET -> {
                    with(ticket) {
                        val startDateEquals = convertDate(startDate)?.compareTo(getNowDate()) == -1

                        when {
                            !isUnlimited && remainingCount == 0 -> {
                                binding.parentLayout.snackbar(getString(R.string.home_fail_ticket))
                            }
                            allowToBook == false -> {
                                when (ignoreLimit) {
                                    true -> {
                                        showContinueMessage(ticket)
                                    }
                                    else -> binding.parentLayout.snackbar("예약은 최대 ${ticket.maxReservationCount}개 까지만 가능합니다")
                                }
                            }
                            ticket.type == 1 -> {
                                // group lesson
                            }
                            else -> {
                                goSelectionDate(ticket)
                            }
                        }
                    }
                }
                PAUSE_TICKET -> {
                    binding.parentLayout.snackbar("정지중인 이용권입니다")
                }
                EXPIRED_TICKET -> {
                    binding.parentLayout.snackbar("만료된 이용권입니다")
                }
            }
        }//end of adapter
        todaysReservationAdapter = TodaysReservationAdapter(){
            Toast.makeText(context, "asdf", Toast.LENGTH_SHORT).show()
        }

        with(binding) {
            recyclerview.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            recyclerview.adapter = adapter
            rvTodaysReservation.adapter = todaysReservationAdapter
        }


    }

    private fun checkMobileAndroidVersion() {
        try {
            viewModel.mobileVersionAndroid.value?.forceUpdate.let {
                if (it == "true") {
                    val dialog = AlertDialog.Builder(requireActivity())
                    dialog.setTitle(getString(R.string.app_update_title))
                        .setMessage(getString(R.string.app_update_message))
                        .setPositiveButton(getString(R.string.app_update_yes)) { _, _ ->
                            goPlayStore();
                        }
                        .setNegativeButton(getString(R.string.app_update_no)) { _, _ ->
                            activity?.finishAffinity();
                        }
                    dialog.create()
                    dialog.show()
                }
            }
        } catch(_:Exception) {

        }
    }

    private fun showRetryExamMessage() {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(getString(R.string.exam_retry_exam_title))
            .setMessage(R.string.exam_retry_exam_message)
            .setPositiveButton(getString(R.string.exam_retry_exam_yes)) { _, _ ->
                viewModel.requestJoinMonthlyExam();
            }
            .setNegativeButton(getString(R.string.exam_retry_exam_no)) { _, _ ->  }
        dialog.create()
        dialog.show()
    }
    private fun showContinueMessage(ticket: Ticket) {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(getString(R.string.fragment_alert_title_ignore_max_reservation))
            .setMessage(R.string.fragment_alert_message_ignore_max_reservation)
            .setPositiveButton(getString(R.string.fragment_alert_okay_ignore_max_reservation)) { _, _ ->
                goSelectionDate(ticket)
            }
            .setNegativeButton(getString(R.string.fragment_alert_cancel_ignore_max_reservation)) { _, _ ->  }
        dialog.create()
        dialog.show()
    }

    private fun reservationListCheck(item: Int) {
        if (item != 0) {
            goReservationList()
        } else {
            binding.parentLayout.snackbar(getString(R.string.home_empty_ticket_go))
        }
    }

    private fun goReservationList() {
        val intent = Intent(activity, ReserveListActivity::class.java)
        startActivity(intent)
    }

    private fun goSelectionDate(ticket: Ticket) {
        val selectIntent = Intent(activity, TicketDateActivity::class.java).apply {
                putExtra(TICKET, ticket)
            }
        startActivity(selectIntent)
    }

    private fun setViewPagerCount() {
        val totalCount = pagerAdapter.itemCount
        binding.vpBanner.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvBannerCount.text = "${position + 1} / ${totalCount}"
            }
        })
    }
}