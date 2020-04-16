package com.rkp.residemenuimpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.special.ResideMenu.ResideMenu;

import static android.view.View.VISIBLE;
import static android.view.View.inflate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ResideMenu resideMenu;
    private RelativeLayout.LayoutParams layoutParams;
    private FrameLayout.LayoutParams fragParams;
    CardView cvInner,cvDashboard;
    AppCompatImageView iv_icon;
    private View mSideMenuView = null;
    float x1, x2, y1, y2, dx, dy;
    String direction;
    private static final String TAG = "MainActivity";




    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvInner=findViewById(R.id.cv_inner);
        cvDashboard=findViewById(R.id.cv_dashboard);
        iv_icon=findViewById(R.id.iv_icon);


        iv_icon.setOnClickListener(this);



        layoutParams = (RelativeLayout.LayoutParams) cvInner.getLayoutParams();
        fragParams = (FrameLayout.LayoutParams) cvDashboard.getLayoutParams();
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);
        resideMenu = new ResideMenu(this, cvDashboard, cvInner);
        resideMenu.attachToActivity(this);




        cvDashboard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):

                        x1 = event.getX();
                        y1 = event.getY();
                        Log.d(TAG, "ACTION_DOWN: " + x1 + " ,  " + y1);
                        break;

                    case (MotionEvent.ACTION_MOVE):
                        Log.d(TAG, "ACTION_MOVE: " + x1 + " ,  " + y1);
                        break;

                    case (MotionEvent.ACTION_UP): {
                        x2 = event.getX();
                        y2 = event.getY();
                        dx = x2 - x1;
                        dy = y2 - y1;
                        Log.d(TAG, "ACTION_UP: " + x1 + " ,  " + y1);


                        // Use dx and dy to determine the direction of the move
                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx > 0) {
                                direction = "right";
                            } else {
                                direction = "left";
                            }

                        } else {
                            if (dy > 0)
                                direction = "down";
                            else
                                direction = "up";
                        }
                    }


                }
                return false;
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMenu();
    }

    private void setUpMenu() {
        resideMenu.setMenuListener(menuListener);
        mSideMenuView = inflate(this, R.layout.layout_side_menu, null);

        resideMenu.setUse3D(true);
        resideMenu.addMenuItem(mSideMenuView, ResideMenu.DIRECTION_RIGHT);
        resideMenu.setShadowVisible(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_icon:
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                break;

        }
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {


        }
            return super.onTouchEvent(event);

    }*/

    private void openDoor(){

        cvDashboard.setCardElevation(this.getResources().getDimension(com.special.ResideMenu.R.dimen._10sdp));
        cvDashboard.setElevation(this.getResources().getDimension(com.special.ResideMenu.R.dimen._10sdp));
        cvInner.setCardElevation(this.getResources().getDimension(com.special.ResideMenu.R.dimen._10sdp));
       // resideMenu.scrollViewRightMenu.setElevation(-this.getResources().getDimension(com.special.ResideMenu.R.dimen._10sdp));


        //imageViewShadow.setVisibility(VISIBLE);
        //imageViewBackground.setVisibility(VISIBLE);








    }

}
