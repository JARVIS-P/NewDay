package com.example.baselibs;

import cn.bmob.v3.BmobObject;

public class LikeClass extends BmobObject {
    private String phone;
    private String content;
    private String status;

    public LikeClass(){}

    public LikeClass(String phone,String content,String status){
        this.phone=phone;
        this.content=content;
        this.status=status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
