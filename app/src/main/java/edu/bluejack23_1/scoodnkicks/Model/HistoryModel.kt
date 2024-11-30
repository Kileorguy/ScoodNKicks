package edu.bluejack23_1.scoodnkicks.Model

import com.google.firebase.firestore.FieldValue

data class HistoryModel(
    val scooterID: String,
    val place: String,
    val time: Any?,
    val type: String, // return apa borrowing
    val userEmail: String,
    val late : Int
)