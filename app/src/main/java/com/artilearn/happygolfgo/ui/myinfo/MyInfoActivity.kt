package com.artilearn.happygolfgo.ui.myinfo

import android.os.Bundle
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.databinding.ActivityMyInfoBinding
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.enummodel.UploadProfileType
import kotlinx.android.synthetic.main.custom_bottom_view.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class
MyInfoActivity : BaseActivity<ActivityMyInfoBinding>(R.layout.activity_my_info) {

    private val viewModel: MyInfoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViewModelObserve()
        initView()
    }

    private fun initBinding() {
        binding.vm = viewModel
    }

    private fun initViewModelObserve() {
        with(viewModel, fun MyInfoViewModel.() {
            emptyNickName.observe(this@MyInfoActivity, Observer {
                showEmptyNickName()
            })
            failNickName.observe(this@MyInfoActivity, Observer {
                showFailNickName()
            })
            localName.observe(this@MyInfoActivity, Observer { name ->
                loadName(name)
            })
            localNickName.observe(this@MyInfoActivity, Observer { nickname ->
                loadNickName(nickname)
            })
            upload.observe(this@MyInfoActivity, Observer {
                uploadType(it)
            })
            appRefresh.observe(this@MyInfoActivity, Observer {
                goRefresh()
            })
        })
    }

    private fun initView() {
        initToolbar(binding.myinfoToolbar.toolbar, getString(R.string.myinfo_toolbar_title), true)
        initBottomView(binding.myinfoBottomView.bottom_layout, getString(R.string.bottom_view_default), BottimViewType.DEFAULT_TYPE)
    }

    private fun showEmptyNickName() {
        binding.etNickname.error = getString(R.string.myinfo_empty_nickname)
    }

    private fun showFailNickName() {
        binding.etNickname.error = getString(R.string.myinfo_fail_nickname)
    }

    private fun loadName(name: String) {
        binding.etName.setText(name)
    }

    private fun loadNickName(nickname: String?) {
        nickname?.let {
            binding.etNickname.hint = it
        }
    }

    private fun uploadType(type: UploadProfileType) {
        when (type) {
            UploadProfileType.SUCCESS -> {
                showToast(ToastType.SUCCESS, getString(R.string.myinfo_upload_success))
                finish()
            }
            UploadProfileType.NETWORK_FAIL -> showToast(ToastType.WARNNING, getString(R.string.myinfo_upload_fail))
            UploadProfileType.UNKNOW_ERROR -> showToast(ToastType.ERROR, getString(R.string.myinfo_upload_error))
        }
    }

    override fun bottomAction() {
        super.bottomAction()
        viewModel.onConfirm()
    }
}