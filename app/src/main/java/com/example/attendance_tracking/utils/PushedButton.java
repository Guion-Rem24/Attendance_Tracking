package com.example.attendance_tracking.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class PushedButton extends androidx.appcompat.widget.AppCompatButton {

    public PushedButton(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public PushedButton(Context context) {
        super(context);
    }

    @Override
    public void setPressed(boolean pressed){
        if(pressed){
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1.0f, 0.8f / 1.0f, 1.0f, 0.8f / 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnimation.setDuration(80);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setFillAfter(true);

            this.startAnimation(scaleAnimation);
        }else{
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    0.8f / 1.0f, 1.0f, 0.8f / 1.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnimation.setDuration(100);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setFillAfter(true);

            this.startAnimation(scaleAnimation);
        }
        super.setPressed(pressed);
    }
}
