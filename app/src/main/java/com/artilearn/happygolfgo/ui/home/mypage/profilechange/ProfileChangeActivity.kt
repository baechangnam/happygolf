package com.artilearn.happygolfgo.ui.home.mypage.profilechange

import android.os.*
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.base.BaseActivity
import com.artilearn.happygolfgo.base.BaseLoadingFragment
import com.artilearn.happygolfgo.databinding.ActivityProfileChangeBinding
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.home.mypage.profilechange.ProfileChangeViewModel.ProfileChangeState.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.util.*

class ProfileChangeActivity : BaseActivity<ActivityProfileChangeBinding>(
    R.layout.activity_profile_change
) {
    private val viewModel: ProfileChangeViewModel by viewModel()

    private val getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { url ->
        if (url != null) {
            setProfile(url.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

        initBinding()
        initToolbar(binding.toolbar.toolbar, "프로필 변경", true)
        initViewModelObserving()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_edit_in, R.anim.slide_edit_out)
    }

    override fun onDestroy() {
        overridePendingTransition(R.anim.slide_edit_in, R.anim.slide_edit_out)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            state.observe(this@ProfileChangeActivity, Observer {
                when (it) {
                    GALLERY -> {
                        getContent.launch("image/*")
                    }
                    BACK -> {
                        finish()
                    }
                    DEFAULT_IMAGE -> {
                        setProfile(CHANGE_DEFAULT_IMAGE)
                    }
                    SUCCESS -> {
                        showToast(ToastType.SUCCESS, "프로필 변경 완료")
                        finish()
                    }
                }
            })
            networkFail.observe(this@ProfileChangeActivity, Observer {
                showToast(ToastType.WARNNING, getString(R.string.fragment_network_fail_content))
            })
            serverError.observe(this@ProfileChangeActivity, Observer {
                showToast(ToastType.ERROR, it)
            })
            appRefresh.observe(this@ProfileChangeActivity, Observer {
                goRefresh()
            })
        }
    }

    private fun setProfile(uri: String?) {
        when {
            uri.isNullOrEmpty() -> {
                viewModel.onNextButtonText("취소")
            }
            uri == CHANGE_DEFAULT_IMAGE -> {
                with(viewModel) {
                    setProfileImage(null)
                    onNextUri("")
                    onNextButtonText("변경")
                }
            }
            else -> {
                with(viewModel) {
                    onNextButtonText("변경")
                    setProfileImage(uri)
                    onNextUri(uri.toString())
                }
            }
        }
    }

    companion object {
        private const val CHANGE_DEFAULT_IMAGE = "default_image"
    }

}