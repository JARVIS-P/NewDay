package com.example.more.viewmoudle

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baselibs.MyLoginInterceptor
import com.example.baselibs.Utility
import com.example.more.db.MoreRetrofitInterface
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.lang.StringBuilder

class MoreViewModel : ViewModel(){
    private lateinit var context:Context
    private lateinit var sp:SharedPreferences
    private lateinit var edit:SharedPreferences.Editor
    private var flag=false

    var dogTalk= MutableLiveData<String>()
    var loveTalk= MutableLiveData<String>()
    var poetryTalk= MutableLiveData<String>()
    var luckyTalk= MutableLiveData<String>()

    fun init(context: Context){
        this.context=context
        sp=context.getSharedPreferences("More",Context.MODE_PRIVATE)
        edit=sp.edit()
        edit.apply()
        flag=!(sp.getString("day","")==Utility.getUtility().time.monthDay.toString())
        if(flag){
            edit.putString("day",Utility.getUtility().time.monthDay.toString())
            edit.putBoolean("dog_like",false)
            edit.putBoolean("love_like",false)
            edit.putBoolean("poetry_like",false)
            edit.apply()
        }
    }

    fun getDogRetrofit(){
        if(flag){
            val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()
            val retrofit=Utility.getUtility().getRetrofit(client,"https://v2.alapi.cn")
            val request=retrofit.create(MoreRetrofitInterface::class.java)
            val map=HashMap<String,Any>()
            map.put("format","json")
            map.put("token","8ygdvvKZrGiM1xH9")
            val single=request.getDog(map)
            single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onSuccess={
                        dogTalk.value=it.data.content
                        edit.putString("dog_talk",it.data.content)
                        edit.apply()
                    },
                    onError={
                        Log.e("TAG", "getDogRetrofit: "+it)
                    }
            )
        }else {
            dogTalk.value=sp.getString("dog_talk","")
        }
    }

    fun getLoveRetrofit(){
        if(flag){
            val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()
            val retrofit=Utility.getUtility().getRetrofit(client,"https://v2.alapi.cn")
            val request=retrofit.create(MoreRetrofitInterface::class.java)
            val map=HashMap<String,Any>()
            map.put("format","json")
            map.put("token","8ygdvvKZrGiM1xH9")
            val single=request.getLove(map)
            single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onSuccess={
                        loveTalk.value=it.data.content
                        edit.putString("love_talk",it.data.content)
                        edit.apply()
                    },
                    onError={
                        Log.e("TAG", "getDogRetrofit: "+it)
                    }
            )
        }else {
            loveTalk.value=sp.getString("love_talk","")
        }
    }

    fun getPoetryRetrofit(){
        if(flag){
            val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()
            val retrofit=Utility.getUtility().getRetrofit(client,"https://v2.alapi.cn")
            val request=retrofit.create(MoreRetrofitInterface::class.java)
            val map=HashMap<String,Any>()
            map.put("format","json")
            map.put("type","all")
            map.put("token","8ygdvvKZrGiM1xH9")
            val single=request.getPoetry(map)
            single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onSuccess={
                        val temp=StringBuilder("")
                        temp.append(it.data.content)
                        temp.append("\n")
                        temp.append("\n")
                        temp.append("      ——")
                        temp.append(it.data.author)
                        poetryTalk.value=temp.toString()
                        edit.putString("poetry_talk",temp.toString())
                        edit.apply()
                    },
                    onError={
                        Log.e("TAG", "getDogRetrofit: "+it)
                    }
            )
        }else {
            poetryTalk.value=sp.getString("poetry_talk","")
        }
    }

    fun getLuckyRetrofit(){
        if(flag){
            val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()
            val retrofit=Utility.getUtility().getRetrofit(client,"http://api.tianapi.com")
            val request=retrofit.create(MoreRetrofitInterface::class.java)
            val map=HashMap<String,Any>()
            map.put("key","91d7d68475a1232400c4c434ad8bedf4")
            map.put("astro","sagittarius")
            val single=request.getLucky(map)
            single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                    onSuccess={
                        val temp=StringBuilder("")
                        temp.append(it.newslist[0].type+":  "+it.newslist[0].content+"\n")
                        temp.append(it.newslist[5].type+":  "+it.newslist[5].content+"\n")
                        temp.append(it.newslist[6].type+":  "+it.newslist[6].content+"\n")
                        temp.append(it.newslist[8].type+":  "+it.newslist[8].content+"\n")
                        luckyTalk.value=temp.toString()
                        edit.putString("lucky_talk",temp.toString())
                        edit.apply()
                    },
                    onError={
                        Log.e("TAG", "getDogRetrofit: "+it)
                    }
            )
        }else {
            //Log.e("TAG", "getLuckyRetrofit: "+sp.getString("lucky_talk","123"))
            luckyTalk.value=sp.getString("lucky_talk","")
        }
    }
}