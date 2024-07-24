package com.artilearn.happygolfgo.data.ticketmanager.mapper

interface TMRemoteMapper<in D, out R> {
    fun fromRemoteToPause(item: D): R
    fun fromRemoteToExpired(item: D): R
}