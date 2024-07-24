package com.artilearn.happygolfgo.data.reserveconfirm

data class ReservePlateModel(
    val ticketID: Int,
    val plateAvailabilityID: Int
)

data class ReserveThreeOneTimePlateModel (
    val ticketID: Int,
    val firstPlateAvailabilityID: Int,
    val secondPlateAvailabilityID: Int,
    val thirdPlateAvailabilityID: Int
)