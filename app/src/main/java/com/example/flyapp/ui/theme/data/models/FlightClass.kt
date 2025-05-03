package com.example.flyapp.ui.theme.data.models


enum class FlightClass(
    val displayName: String,
    val basePrice: Int,
    val baggageAllowance: Int,
    val isPremium: Boolean
) {
    FIRST_CLASS(
        displayName = "First Class",
        basePrice = 450,
        baggageAllowance = 40,
        isPremium = true
    ),

    BUSINESS_CLASS(
        displayName = "Business Class",
        basePrice = 250,
        baggageAllowance = 30,
        isPremium = true
    ),

    ECONOMY_CLASS(
        displayName = "Economy Class",
        basePrice = 120,
        baggageAllowance = 20,
        isPremium = false
    );

    /**
     * Helper functions to determine class from seat number
     */
    companion object {
        fun fromSeatId(seatId: String): FlightClass {
            return when {
                seatId.startsWith("1") || seatId.startsWith("2") -> FIRST_CLASS
                seatId.startsWith("3") || seatId.startsWith("4") ||
                        seatId.startsWith("5") || seatId.startsWith("6") ||
                        seatId.startsWith("7") -> BUSINESS_CLASS
                else -> ECONOMY_CLASS
            }
        }
        fun isPremiumSeat(seatId: String): Boolean {
            return fromSeatId(seatId).isPremium
        }
        fun getClassDisplayInfo(seatClass: FlightClass): Pair<String, Boolean> {
            return when(seatClass) {
                FIRST_CLASS -> Pair("F", true)
                BUSINESS_CLASS -> Pair("B", true)
                ECONOMY_CLASS -> Pair("E", false)
            }
        }
    }
}