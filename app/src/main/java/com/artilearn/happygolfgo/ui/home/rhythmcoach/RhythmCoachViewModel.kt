package com.artilearn.happygolfgo.ui.home.rhythmcoach

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RhythmCoachViewModel : ViewModel() {
    val speedDisplay : MutableLiveData<String> =  MutableLiveData<String> ("X 1")
//    unused property
//    private val _playSpeed: MutableLiveData<Int> = MutableLiveData(1)
// unused property
//    val playSpeed: LiveData<Int>
//        get() = _playSpeed
// unused function
//    fun  upPlaySpeed() {
//        val cur = playSpeed.value
//        if (cur != null) {
//            if (cur  <  5) {
//                _playSpeed.value = cur + 1
//            }
//        }
//        speedDisplay.value = "X ${playSpeed.value}"
//    }
//
//    fun downPlaySpeed() {
//        val cur = playSpeed.value
//        if (cur != null && cur > 1) {
//             _playSpeed.value = cur - 1
//        }
//        speedDisplay.value = "X ${playSpeed.value}"
//    }
}