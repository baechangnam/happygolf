package com.artilearn.happygolfgo.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.enummodel.BottimViewType
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.common.ErrorActivity
import com.artilearn.happygolfgo.ui.common.NetworkErrorActivity
import com.artilearn.happygolfgo.ui.splash.SplashActivity
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.custom_bottom_view.view.*

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()
    lateinit var binding: B

    private var actionBar: Toolbar? = null
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    protected fun initToolbar(toolbar: View, title: String, backButton: Boolean) {
        actionBar = toolbar as Toolbar
        setSupportActionBar(actionBar)

        val actionBar = supportActionBar
        actionBar?.let {
            with(it) {
                setDisplayShowTitleEnabled(true)
                setTitle(title)
                setDisplayHomeAsUpEnabled(backButton)
            }
        }
    }

    protected fun initBottomView(bottomView: View, title: String, type: BottimViewType) {
        with(bottomView) {
            tv_text.text = title
            setOnClickListener { bottomAction() }
        }

        when (type) {
            BottimViewType.DEFAULT_TYPE -> {
                bottomView.iv_image.setImageResource(R.drawable.ic_flag_bottom_button)
                bottomView.bottom_layout.setBackgroundColor(getColor(R.color.color_green))
            }
            BottimViewType.CANCEL_TYPE -> {
                bottomView.iv_image.setImageResource(R.drawable.ic_cancel)
                bottomView.bottom_layout.setBackgroundColor(getColor(R.color.color_red))
            }
        }
    }

    protected fun showError(errorText: String) {
        val errorActivity = Intent(this, ErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, intent)
                putExtra(EXTRA_ERROR_TEXT, errorText)
            }
        startActivity(errorActivity)
        finish()
    }

    protected fun showNetworkFail() {
        val networkFailActivity = Intent(this, NetworkErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, intent)
            }
        startActivity(networkFailActivity)
        finish()
    }

    protected fun showToast(type: ToastType, message: String) {
        when (type) {
            ToastType.SUCCESS -> Toasty.success(this, message, Toast.LENGTH_SHORT).show()
            ToastType.ERROR -> Toasty.error(this, message, Toast.LENGTH_SHORT).show()
            ToastType.WARNNING -> Toasty.warning(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun goRefresh() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun bottomAction() {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}

