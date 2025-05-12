package com.example.flyapp.ui.data.models

data class SeatInfo(
    val id: String,
    val row: Int,
    val column: String,
    val type: SeatType,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = true
)

enum class SeatType {
    ECONOMY, BUSINESS, FIRST_CLASS
}

enum class SeatPosition {
    WINDOW, MIDDLE, AISLE
}

data class SeatRow(
    val rowNumber: Int,
    val seatType: SeatType
)
