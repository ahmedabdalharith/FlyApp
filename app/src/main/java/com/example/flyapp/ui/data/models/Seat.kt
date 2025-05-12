package com.example.flyapp.ui.data.models


data class Seat(
    val id: String,
    var status: SeatStatus,
    var type: SeatStatus = SeatStatus.ECONOMY_CLASS
)