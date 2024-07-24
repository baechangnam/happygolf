@file: JvmName("SizeUtil")
package com.artilearn.happygolfgo.util.ext

import android.util.TypedValue
import android.util.TypedValue.applyDimension
import com.artilearn.happygolfgo.util.BaseUtils.context

fun Int.px(): Float = applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), context.resources.displayMetrics)

fun Float.px(): Float = applyDimension(TypedValue.COMPLEX_UNIT_PX, this, context.resources.displayMetrics)

fun Int.dp(): Float = applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

fun Float.dp(): Float = applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

val Int.px: Int
    get() = applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), context.resources.displayMetrics).toInt()

val Int.dp: Int
    get() = applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()

val Float.dp: Float
    get() = applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

fun Boolean.toFloat() = if (this) 1f else 0f