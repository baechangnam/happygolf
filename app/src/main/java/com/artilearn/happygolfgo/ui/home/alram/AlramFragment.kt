package com.artilearn.happygolfgo.ui.home.alram

import android.content.Intent
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.api.firebase.FCMMessage.Companion.reservationLesson
import com.artilearn.happygolfgo.api.firebase.FCMMessage.Companion.reservationPlate
import com.artilearn.happygolfgo.base.BaseFragment
import com.artilearn.happygolfgo.base.BaseSwipeDelete
import com.artilearn.happygolfgo.constants.LESSON_ID
import com.artilearn.happygolfgo.constants.PLATE_ID
import com.artilearn.happygolfgo.constants.RESERVATION_TYPE
import com.artilearn.happygolfgo.data.Alram
import com.artilearn.happygolfgo.databinding.FragmentAlramBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.log.LogActivity
import com.artilearn.happygolfgo.ui.reserveconfirm.ReserveConfirmActivity
import com.artilearn.happygolfgo.util.snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlramFragment
    : BaseFragment<FragmentAlramBinding, AlramViewModel>(R.layout.fragment_alram) {
    private val TAG = javaClass.simpleName
    override val viewModel: AlramViewModel by viewModel()
    private lateinit var adapter: AlramAdapter

    override fun init() {

        initBinding()
        initViewModelObserving()
        initAdapter()
        setHasOptionsMenu(true)
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            serverError.observe(this@AlramFragment, Observer {
                showFragmentToast(ToastType.ERROR, it)
            })
            networkFail.observe(this@AlramFragment, Observer {
                showFragmentToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            appRefresh.observe(this@AlramFragment, Observer {
                goRefreshFromFragment()
            })
            deleteAlram.observe(viewLifecycleOwner, Observer {
                val currentList = adapter.currentList.toMutableList()
                currentList.remove(it)
                adapter.submitList(currentList)
                viewModel.checkAlramMessage(currentList)
            })
            emptyList.observe(viewLifecycleOwner, Observer {
                binding.root.snackbar("전체 삭제 성공")
                adapter.submitList(it)
                viewModel.checkAlramMessage(emptyList())
            })
        }
    }

    private fun initAdapter() {
        adapter = AlramAdapter { alram ->
            when (alram.eventType) {
                "3", "7" -> goReservaitonPlate(alram.eventValue.toInt())
                "4" -> goReservationLesson(alram.eventValue.toInt())
                "6" -> goLessonLog(alram.eventValue.toInt())
            }
        }

        with(binding) {
            recyclerview.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            swipeToDelete(recyclerview)
            recyclerview.adapter = adapter
        }
    }

    private fun swipeToDelete(rv: RecyclerView) {
        val swipeCallback = object : BaseSwipeDelete() {
            override fun onSwipe(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentItem = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteToAlram(currentItem)
            }
        }

        val itemHelper = ItemTouchHelper(swipeCallback)
        itemHelper.attachToRecyclerView(rv)
    }

    private fun goReservationLesson(lessonId: Int) {
        val lessonIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationLesson)
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(lessonIntent)
    }

    private fun goReservaitonPlate(plateId: Int) {
        val plateIntent = Intent(activity, ReserveConfirmActivity::class.java)
            .apply {
                putExtra(RESERVATION_TYPE, reservationPlate)
                putExtra(PLATE_ID, plateId)
            }
        startActivity(plateIntent)
    }

    private fun goLessonLog(lessonId: Int) {
        val logIntent = Intent(activity, LogActivity::class.java)
            .apply {
                putExtra(LESSON_ID, lessonId)
            }
        startActivity(logIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_alram, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            val currentList = adapter.currentList.toMutableList()
            if (currentList.isEmpty()) {
                binding.root.snackbar("삭제할 알림이 없습니다")
            } else {
                viewModel.deleteAll()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}