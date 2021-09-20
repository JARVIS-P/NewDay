package com.example.today.db

data class DayData(
    val error_code: Int,
    val reason: String,
    val result: Result
)

data class Result(
    val `data`: Data
)

data class Data(
    val holiday:String,
    val animalsYear: String,
    val avoid: String,
    val date: String,
    val lunar: String,
    val lunarYear: String,
    val suit: String,
    val weekday: String,
    val year_month: String
)