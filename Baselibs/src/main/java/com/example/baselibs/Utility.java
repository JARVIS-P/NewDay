package com.example.baselibs;

import android.os.Bundle;
import android.text.format.Time;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utility{
    private Utility() {
    }

    private static Utility utility=new Utility();

    public static Utility getUtility(){
        return utility;
    }

    private static User user=new User();

    public static User getUser(){
        return user;
    }

    public Retrofit getRetrofit(OkHttpClient client,String url){
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public android.text.format.Time getTime(){
        Time time=new android.text.format.Time("GMT+8");
        time.setToNow();
        return time;
    }
}
