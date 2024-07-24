package com.artilearn.happygolfgo.util.ext

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.ViewGroup
import android.view.WindowManager

fun Context.setDialogSize(dialog: Dialog) {
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    val params: ViewGroup.LayoutParams? = dialog.window!!.attributes
    val deviceWidth = size.x
    params?.width = (deviceWidth * 0.8).toInt()
    dialog.window?.attributes = params as WindowManager.LayoutParams
}