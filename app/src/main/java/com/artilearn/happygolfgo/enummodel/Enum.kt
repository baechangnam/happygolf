package com.artilearn.happygolfgo.enummodel


enum class BottimViewType {
    DEFAULT_TYPE,
    CANCEL_TYPE
}

enum class UploadProfileType {
    NETWORK_FAIL,
    UNKNOW_ERROR,
    SUCCESS
}

enum class ToastType {
    SUCCESS,
    WARNNING,
    ERROR
}

enum class PwAuthPhoneType {
    EMPTY,
    FAIL,
    NETWORK_FAIL,
    LEGNTH_FAIL,
    EMPTY_USER,
    SUCCESS
}

enum class PwAuthNumberType {
    UNCREATE,
    EMPTY,
    FAIL,
    AUTH_ERROR,
    NETWORK_FAIL,
    SUCCESS,
    MODEL_FAIL,
    TIMER_OVER
}