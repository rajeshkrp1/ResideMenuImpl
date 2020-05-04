package com.rkp.residemenuimpl;

import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Test extends AppCompatActivity {

    ImageView btn1;
    float startX;
    private static final String TAG = "Test";
    ConstraintLayout dashboard;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);
        btn1=findViewById(R.id.imageView);
        dashboard=findViewById(R.id.cv_dashboard);


        dashboard.setRotationY(10);


    }


}
