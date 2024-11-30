package edu.bluejack23_1.scoodnkicks.Model

data class LocationModel (
    val id : String,
    val name: String,
    val address: String,
    val availableScooter : Int,
    val maxScooter : Int
)