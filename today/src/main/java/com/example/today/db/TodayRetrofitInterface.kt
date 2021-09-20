package com.example.today.db

import io.reactivex.Single
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TodayRetrofitInterface {
    @FormUrlEncoded
    @POST("/calendar/day")
    fun getDayData(@FieldMap map:Map<String,@JvmSuppressWildcards Any>):Single<DayData>

    @POST("/ws/api.php")
    fun getTalk():Single<Talk>
}