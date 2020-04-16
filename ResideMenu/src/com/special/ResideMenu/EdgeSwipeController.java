package com.special.ResideMenu;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public class EdgeSwipeController {
        private static final float SWIPE_START_MAX_DX = 50.0f;
        private static final float SWIPE_START_MIN_DY = 35.0f;
        private static final float SWIPE_AVAILABLE_VELOCITY = 0.5f;
        private static final float SWIPE_LEFT_START_MIN_DX =  30f;
        private static final float SWIPE_LEFT_START_MAX_DX =  300f;

    private GestureDetector mDetector;
        private Activity activity;

    private boolean isAvailableEdgeSwipe = false;
        private boolean isExecuteEdgeSwipe = false;

        private boolean isEnabled = true;
    private OnSwipeListener onSwipeListener;


    public EdgeSwipeController(Activity activity) {

            this.activity = activity;
            mDetector = new GestureDetector(activity, mGestureListener);

        }

        /**
         * @param isEnabled
         */
        public void setEnabledEdgeSwipe(boolean isEnabled) {
            this.isEnabled = isEnabled;
        }


        /**intercept
         * @param event
         * @return
         */
        public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
            if(!isEnabled) {
                return true;
            }

            // GestureDetector
            mDetector.onTouchEvent(event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(event.getX() < SWIPE_START_MAX_DX && event.getY() > SWIPE_START_MIN_DY){
                        isAvailableEdgeSwipe = true;
                        return false;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(!isAvailableEdgeSwipe) {
                        return true;
                    }


                    return false;

                case MotionEvent.ACTION_UP:
                    if(!isExecuteEdgeSwipe) {

                    }
                    isExecuteEdgeSwipe = false;
                    isAvailableEdgeSwipe = false;
                    break;

            }
            return true;
        }



        private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float AVAILABLE_SWIPE_DISTANCE = activity.getWindow().getDecorView().getWidth() * SWIPE_AVAILABLE_VELOCITY;
                if(e1.getX() > SWIPE_LEFT_START_MIN_DX && e1.getX() < SWIPE_LEFT_START_MAX_DX ){
                    if(e2.getX() - e1.getX() < AVAILABLE_SWIPE_DISTANCE) {
                        if(onSwipeListener!=null)
                        onSwipeListener.leftEdgeSwipe();
                        return true;
                    }
                }else if((e1.getX()<=(AVAILABLE_SWIPE_DISTANCE*2) && e1.getX()>=(AVAILABLE_SWIPE_DISTANCE*2-SWIPE_START_MIN_DY))){
                    if((e1.getX() - e2.getX())> 50f) {
                        if(onSwipeListener!=null)
                        onSwipeListener.rightEdgeSwipe();
                        return true;
                    }
                }
                return false;
            }
        };

    boolean onEdgeSwipeEvent(MotionEvent ev) {

        return onInterceptTouchEvent(ev);
    }

    public void setSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }


    public interface OnSwipeListener {

        /**
         * This method will be called at the right edge swipe.
         */
        void rightEdgeSwipe();

        /**
         * This method will be called at the right edge swipe
         */
        void leftEdgeSwipe();
    }
}

