package com.example.more.db

data class LuckyClass(
    val code: Int,
    val msg: String,
    val newslist: List<Newslist>
)

data class Newslist(
    val content: String,
    val type: String
)