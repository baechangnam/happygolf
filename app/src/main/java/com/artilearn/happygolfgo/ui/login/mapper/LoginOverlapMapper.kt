package com.artilearn.happygolfgo.ui.login.mapper

import com.artilearn.happygolfgo.data.OverlapUser
import com.artilearn.happygolfgo.mapper.Mapper
import com.artilearn.happygolfgo.ui.login.model.LoginOverlap

object LoginOverlapMapper : Mapper<List<OverlapUser>, List<LoginOverlap>> {
    override fun mapper(item: List<OverlapUser>): List<LoginOverlap> {
        return item.map { overlapUser ->
            LoginOverlap(
                branchId = overlapUser.branchID,
                training = overlapUser.name
            )
        }
    }
}