package com.artilearn.happygolfgo.util.databinding

import androidx.databinding.BindingAdapter
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("bottomClick")
fun BottomNavigationView.onBottomClick(viewModel: HomeViewModel) {
    setOnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.reservation -> viewModel.onNextBottomEvent("예약")
            //DONETODO: game center to rhythm coach 2022.01.09
//            R.id.gamecenter -> {
//                RequestEnvironmentMode.test("게임센터")
//            }
            //  version 39(2.2.3) 버그 수정 중
//          2022.02.15 리듬 코치 배포 보류 중
//            R.id.rhythmcoach -> viewModel.onNextBottomEvent("리듬코치");
            //vc45
            R.id.record -> viewModel.onNextBottomEvent("기록실")//
            R.id.training -> viewModel.onNextBottomEvent("훈련실")//
            R.id.alram -> viewModel.onNextBottomEvent("알림")
            R.id.mypage -> viewModel.onNextBottomEvent("마이페이지")
        }
        true
    }
}