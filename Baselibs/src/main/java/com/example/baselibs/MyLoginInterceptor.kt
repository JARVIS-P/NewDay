package com.example.baselibs

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.nio.charset.Charset

class MyLoginInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request()
        val response=chain.proceed(request)
        Log.e("Interceptor", String.format("...\n请求连接：%s\n请求头：%s\n请求参数：%s\n请求响应：%s",request.url(),getRequestHeaders(request),getRequestInfo(request),getResponseInfo(response)))
        return response
    }

    fun getRequestHeaders(request: Request):String{
        return request.headers().toString()
    }

    fun getRequestInfo(request: Request):String{
        val requestBody=request.body()
        val bufferSink=okio.Buffer()
        requestBody?.writeTo(bufferSink)
        val charset=Charset.forName("utf-8")
        return bufferSink.readString(charset)
    }

    fun getResponseInfo(response: Response):String{
        val responseBody=response.body()
        val contentLength=responseBody?.contentLength()
        val source=responseBody?.source()
        source?.request(Long.MAX_VALUE)

        val bufferSink=source?.buffer
        val charset=Charset.forName("utf-8")
        if(contentLength!=0L){
            return bufferSink?.clone()?.readString(charset).toString()
        }
        return ""
    }
}