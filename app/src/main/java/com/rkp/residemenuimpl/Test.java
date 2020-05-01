package com.rkp.residemenuimpl;

import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Test extends AppCompatActivity {

    Button btn1;
    float startX;
    private static final String TAG = "Test";
    ConstraintLayout dashboard;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);
        btn1=findViewById(R.id.btn2);
        dashboard=findViewById(R.id.cv_dashboard);


findViewById(R.id.cv_dashboard).setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):

                 startX = event.getRawX();
                 float x1=event.getX();
                 float y1=event.getY();

                Log.d(TAG, "ACTION_DOWN: " + x1 + " ,  " + y1);
                Log.d(TAG, "ACTION_DOWN: getX " + startX);
                break;

            case (MotionEvent.ACTION_MOVE):
                Log.d(TAG,"xoffset"+String.valueOf(event.getX()-startX)) ;
                dashboard.setTranslationX(event.getX()-startX);

                break;

            case (MotionEvent.ACTION_UP):

                break;


        }

        return false;
    }
});

findViewById(R.id.btn2).setTranslationX(100);





    }
}
