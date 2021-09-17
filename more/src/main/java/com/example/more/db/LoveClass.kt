package com.example.more.db

data class LoveClass(
    val code: Int,
    val `data`: LoveData,
    val log_id: Long,
    val msg: String,
    val time: Int
)

data class LoveData(
    val content: String
)