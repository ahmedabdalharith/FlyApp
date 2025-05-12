package com.example.flyapp.ui.data.models

data class FlightDetails(
    val flightNumber: String,
    val airline: String,
    val departureCity: String,
    val departureAirport: String,
    val departureCode: String,
    val departureTime: String,
    val departureDate: String,
    val arrivalCity: String,
    val arrivalAirport: String,
    val arrivalCode: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val duration: String,
    val aircraft: String,
    val gate: String,
    val terminal: String,
    val boardingTime: String,
    val classType: String,
    val price: Double,
    val hasWifi: Boolean,
    val hasMeal: Boolean,
    val luggageAllowance: String
)