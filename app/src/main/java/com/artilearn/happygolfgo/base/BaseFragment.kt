package com.artilearn.happygolfgo.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.artilearn.happygolfgo.constants.EXTRA_ERROR_TEXT
import com.artilearn.happygolfgo.constants.EXTRA_INTENT
import com.artilearn.happygolfgo.enummodel.ToastType
import com.artilearn.happygolfgo.ui.common.ErrorActivity
import com.artilearn.happygolfgo.ui.common.NetworkErrorActivity
import com.artilearn.happygolfgo.ui.splash.SplashActivity
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes val layoutId: Int
) : Fragment() {

    protected val compositeDisposable = CompositeDisposable()
    protected lateinit var binding: B
    protected abstract val viewModel: VM

    abstract fun init()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        init()
    }

    protected fun showFragmentToast(type: ToastType, message: String) {
        when (type) {
            ToastType.SUCCESS -> Toasty.success(requireActivity(), message, Toast.LENGTH_SHORT).show()
            ToastType.ERROR -> Toasty.error(requireActivity(), message, Toast.LENGTH_SHORT).show()
            ToastType.WARNNING -> Toasty.warning(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun showFragmentError(errorText: String) {
        val errorActivity = Intent(activity, ErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, this)
                putExtra(EXTRA_ERROR_TEXT, errorText)
            }
        startActivity(errorActivity)
    }

    protected fun showFragmentNetworkFail() {
        val networkFailActivity = Intent(activity, NetworkErrorActivity::class.java)
            .apply {
                putExtra(EXTRA_INTENT, this)
            }
        startActivity(networkFailActivity)
    }

    protected fun goRefreshFromFragment() {
        val intent = Intent(activity, SplashActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

}