package com.example.more.db

import io.reactivex.Single
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MoreRetrofitInterface {

    @FormUrlEncoded
    @POST("/txapi/star/index")
    fun getLucky(@FieldMap map:Map<String,@JvmSuppressWildcards Any>):Single<LuckyClass>

    @FormUrlEncoded
    @POST("/api/shici ")
    fun getPoetry(@FieldMap map:Map<String,@JvmSuppressWildcards Any>):Single<PoetryClass>

    @FormUrlEncoded
    @POST("/api/qinghua")
    fun getLove(@FieldMap map:Map<String,@JvmSuppressWildcards Any>):Single<LoveClass>

    @FormUrlEncoded
    @POST("/api/dog")
    fun getDog(@FieldMap map:Map<String,@JvmSuppressWildcards Any>):Single<DogClass>
}