package com.example.more.db

data class PoetryClass(
    val code: Int,
    val `data`: PoetryData,
    val log_id: Long,
    val msg: String,
    val time: Int
)

data class PoetryData(
    val author: String,
    val c1: String,
    val c2: String,
    val c3: String,
    val category: String,
    val content: String,
    val origin: String
)