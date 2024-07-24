package com.artilearn.happygolfgo.util

import android.content.Context
import android.content.pm.PackageInfo

class CurrentVersion(val context: Context) {
    private lateinit var packageInfo: PackageInfo

    val version: String
        get() {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

            return packageInfo.versionName
        }
}