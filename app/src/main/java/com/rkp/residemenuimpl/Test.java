package com.rkp.residemenuimpl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
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




          //  if(/*xOffSet>0*/true){
//        // for left to right swipe
//
//        viewActivity.setScaleX(mScaleValue);
//        viewActivity.setScaleY(mScaleValueY);
//
//        if(mUse3D){
//            viewActivity.setRotationY(rotationY);
//
//        }

        //PropertyValuesHolder rotate = PropertyValuesHolder.ofFloat(View.ROTATION_Y, -8, rotationY); // /*- (-updatedMovingValue)*/
       // ObjectAnimator threeDRotation = ObjectAnimator.ofPropertyValuesHolder(viewActivity, rotate);
//        threeDRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.d(TAG, "onAnimationUpdate: rotate " + animation.getAnimatedValue());
//               // viewActivity.setRotationY((float)animation.getAnimatedValue());
//            }
//        });
        //threeDRotation.setDuration(0);
        //threeDRotation.start();
//
//
//        AnimatorSet scaleUp_activity = buildScaleUpAnimation(viewActivity, mScaleValue, mScaleValueY, TAG);
//        AnimatorSet scaleUp_shadow = buildScaleUpAnimation(imageViewShadow, mScaleValue, mScaleValueY, TAG);
//        AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 0.0f, TAG);
//        AnimatorSet alpha_right_menu = buildMenuAnimationOpen(layoutRightMenu, 0.0f, TAG);
//        scaleUp_activity.addListener(animationDublicateListener);
//        scaleUp_activity.playTogether(scaleUp_shadow, alpha_menu, alpha_right_menu, animatorClose, outerRadiusAnimatorClose , translationClose);
//        scaleUp_activity.setDuration(0);
//        scaleUp_activity.start();

  //  }else {
//        if (mScaleValue < .68 && mScaleValueY < .73) {
//            isOpened = true;
//            showScrollViewMenu(scrollViewMenu);
//            scrollViewMenu.setTranslationX(transitionValue);
//
//            viewActivity.setScaleX(mScaleValue);
//            viewActivity.setScaleY(mScaleValueY);
//
//            if(mUse3D){
//                viewActivity.setRotationY(rotationY);
//            }
//
//            AnimatorSet scaleDown_activity = buildScaleDownAnimation(viewActivity, mScaleValue, mScaleValueY * multiPlyerValye, TAG);
//            AnimatorSet scaleDown_shadow = buildScaleDownAnimation(imageViewShadow,
//                    (mScaleValue + shadowAdjustScaleX), (mScaleValueY + shadowAdjustScaleY) * multiPlyerValye, TAG);
//            AnimatorSet alpha_menu = buildMenuAnimationOpen(scrollViewMenu, 1.0f, TAG);
//            AnimatorSet aplha_rightMenu = buildMenuAnimationOpen(layoutRightMenu, 1.0f, TAG);
//            //scaleDown_activity.addListener(animationListener);
//            scaleDown_activity.addListener(animationDublicateListener);
//            scaleDown_activity.playTogether(scaleDown_shadow, alpha_menu, aplha_rightMenu, animator, outerRadiusAnimator, translation);
//            scaleDown_activity.setDuration(0);
//            scaleDown_activity.setInterpolator(AnimationUtils.loadInterpolator(activity, android.R.anim.decelerate_interpolator));
//            scaleDown_activity.start();
      // }ghgh

    //}

}