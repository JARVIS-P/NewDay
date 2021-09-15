package com.example.mine.db;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mine.R;

public class MineDialog extends Dialog {

    public TextView textView;

    public MineDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.mine_dialog);
        textView=findViewById(R.id.mine_dialog_text);
    }
}
