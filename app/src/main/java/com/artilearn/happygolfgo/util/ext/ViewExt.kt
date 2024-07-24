package com.artilearn.happygolfgo.util.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun View.hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}