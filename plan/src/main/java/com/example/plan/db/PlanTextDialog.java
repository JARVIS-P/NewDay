package com.example.plan.db;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.plan.R;

public class PlanTextDialog extends Dialog {

    private TextView textView;

    public PlanTextDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.plan_text_dialog);

        textView=findViewById(R.id.plan_dialog_text);
    }

    public void setTextView(String string){
        textView.setText(string);
    }
}
