package com.example.more.db

data class DogClass(
    val code: Int,
    val `data`: Data,
    val log_id: Long,
    val msg: String,
    val time: Int
)

data class Data(
    val content: String
)