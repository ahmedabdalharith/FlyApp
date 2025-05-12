package com.example.flyapp.ui.data.models
data class Flight(
    val airline: String,
    val flightNumber: String,
    val departureCode: String,
    val arrivalCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val date: String,
    val price: String,
    val airlineLogoRes: Int
)