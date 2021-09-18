package com.example.plan.db;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.plan.R;

public class PlanMyDialog extends Dialog {

    private PlanClass planClass=new PlanClass("","其他","",false);
    private PlanMyDialog myDialog=this;
    private EditText editText1;
    private EditText editText2;
    private FrameLayout yes;
    private FrameLayout no;
    private RadioGroup radioGroup;
    private PlanMyDialogListener listener;

    public PlanMyDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.plan_my_dialog);
        editText1=findViewById(R.id.plan_dialog_edit1);
        editText2=findViewById(R.id.plan_dialog_edit2);
        yes=findViewById(R.id.plan_dialog_yes);
        no=findViewById(R.id.plan_dialog_no);
        radioGroup=findViewById(R.id.plan_radioGroup);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        radioGroup.check(R.id.plan_other_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.plan_study_radio){
                    planClass.setStatus("学习");
                }else if(checkedId==R.id.plan_sports_radio){
                    planClass.setStatus("运动");
                }else if(checkedId==R.id.plan_casual_radio){
                    planClass.setStatus("娱乐");
                }else if(checkedId==R.id.plan_other_radio){
                    planClass.setStatus("其他");
                }
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planClass.setTheme(editText1.getText().toString());
                if(editText2.getText().toString().equals("")){
                    planClass.setText("该计划无正文内容");
                }else {
                    planClass.setText(editText2.getText().toString());
                }
                planClass.setSuccess(false);
                if(listener!=null) listener.yesListener();
                myDialog.dismiss();
            }
        });
    }

    public void setYesListener(PlanMyDialogListener planMyDialogListener){
        listener=planMyDialogListener;
    }

    public PlanClass getPlanClass(){
        return planClass;
    }

}
