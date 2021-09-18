package com.example.plan.db

import cn.bmob.v3.BmobObject

data class PlanClass(var theme:String,var status:String,var text:String,var isSuccess:Boolean) : BmobObject()