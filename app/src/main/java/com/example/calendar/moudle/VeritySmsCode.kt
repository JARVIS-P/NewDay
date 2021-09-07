package com.example.calendar.moudle

import android.util.Log
import android.widget.Toast
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.example.calendar.db.SMSMessage
import org.greenrobot.eventbus.EventBus

fun bMobSMSVerify(phone : String , code : String){
    BmobSMS.verifySmsCode(phone,code,object : UpdateListener(){
        override fun done(e: BmobException?) {
            if(e == null){//验证成功
                EventBus.getDefault().postSticky(SMSMessage(true))
                Log.e("TAG", "true")
            }else{//验证失败
                EventBus.getDefault().postSticky(SMSMessage(false))
                Log.e("TAG", "false")
            }
        }
    })
}