package com.artilearn.happygolfgo.ui.home.record

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.artilearn.happygolfgo.databinding.DialogRankingInfoBinding

class RankingInfoDialog(context:Context,
                        private  val callBack:(String) -> Unit
) : Dialog(context) {

    private lateinit var binding:DialogRankingInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context.dialogResize(this@RankingInfoDialog, 0.9f, 0.9f)
        binding = DialogRankingInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
//           setCancelable(false) : block backbutton
           window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
           btnPopupClose.setOnClickListener {
                dismiss();
           }
    }

    fun Context.dialogResize(dialog: Dialog, width: Float, height: Float){
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val window = dialog.window
            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        }else{
            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialog.window
            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }

    }
}