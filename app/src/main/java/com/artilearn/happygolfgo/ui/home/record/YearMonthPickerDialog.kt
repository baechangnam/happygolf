package com.artilearn.happygolfgo.ui.home.record

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.DialogFragment
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.exam.YearMonth
import java.util.*
import kotlin.collections.ArrayList

data class YearMonthDate(
    val year: Int,
    val month: List<Int>
)

class YearMonthPickerDialog<Button : View?>(
    val examHistory: List<YearMonth>
) : DialogFragment() {
    private var listener: SelectDateListener? = null
    private val MAX_YEAR = 2030
    private val MIN_YEAR = 2010
    private val date: ArrayList<YearMonthDate> = arrayListOf()
    var isYearEnabled = true
    var selectYear = -1
    private val examHistoryString = examHistory.map {
        it.yearMonth
    }

    val yearToMonthsMap: Map<String, List<String>> by lazy {
        examHistory.groupBy(
            { it.yearMonth.substring(0, 4) },  // 년도 추출
            { it.yearMonth.substring(4, 6) }   // 월 추출
        )
    }
    val uniqueYears: List<Int> by lazy {
        examHistory.map { it.yearMonth.substring(0, 4).toInt() }.distinct().sorted()
    }

    var cal = Calendar.getInstance()
    fun setListener(listener: SelectDateListener) {
        this.listener = listener
    }

    var btnConfirm: Button? = null
    var btnCancel: Button? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =
            AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog: View = inflater.inflate(R.layout.year_month_picker, null).also {
            btnConfirm = it.findViewById<Button>(R.id.btn_confirm)
            btnCancel = it.findViewById<Button>(R.id.btn_cancel)
        }

        yearToMonthsMap.forEach { (s, strings) ->
            date.add(
                YearMonthDate(
                    year = s.toInt(),
                    month = strings.map {
                        it.toInt()
                    }
                )
            )
        }
        isYearEnabled = date.isNotEmpty()
        selectYear = date[0].year

        val monthPicker =
            dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker =
            dialog.findViewById<View>(R.id.picker_year) as NumberPicker
        monthPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        yearPicker.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        btnConfirm?.setOnClickListener {
            val month = String.format("%02d", monthPicker.value)
            if (examHistoryString.contains("${yearPicker.value}${month}")) {
                listener?.onSelectDate(
                    Pair(
                        yearPicker.value.toString(),
                        month
                    )
                )
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(), "" +
                            "미응시한 시험입니다. 다른 날짜를 선택해주세요.", Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnCancel?.setOnClickListener {
            dismiss()
        }

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.displayedValues = (MIN_YEAR..MAX_YEAR).map { it.toString() }.toTypedArray()
        yearPicker.value = selectYear
        setMonthPickerValues(monthPicker)
        builder.setView(dialog)

        yearPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            date.forEach {
                if (it.year == newVal) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        picker.textColor = requireContext().getColor(R.color.color_black)
                        selectYear = newVal
                    }
                    isYearEnabled = true
                    setMonthPickerValues(monthPicker)
                    return@setOnValueChangedListener
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        picker.textColor = requireContext().getColor(R.color.gray300)
                    }
                    selectYear = newVal
                    isYearEnabled = false
                }
            }
            setMonthPickerValues(monthPicker)
        }

        return builder.create()
    }

    // monthPicker에 사용할 월 설정
    private fun setMonthPickerValues(monthPicker: NumberPicker) {
        monthPicker.displayedValues = null  // 이전 displayedValues를 초기화
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.displayedValues = (1..12).map { it.toString() }.toTypedArray()
        monthPicker.value = 1

        for (item in date) {
            if (item.year == selectYear) {
                monthPicker.value = date.find { it.year == selectYear }!!.month[0]
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    monthPicker.textColor = requireContext().getColor(R.color.color_black)
                }
                break
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    monthPicker.textColor = requireContext().getColor(R.color.gray300)
                }
            }
        }

        monthPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            date.forEach {
                if (selectYear == it.year) {
                    it.month.forEach {
                        if (isYearEnabled && it == newVal) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                picker.textColor = requireContext().getColor(R.color.color_black)
                            }
                            return@setOnValueChangedListener
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                picker.textColor = requireContext().getColor(R.color.gray300)
                            }
                        }
                    }
                }
            }
        }
    }

    interface SelectDateListener {
        fun onSelectDate(yearMonth: Pair<String, String>)
    }
}