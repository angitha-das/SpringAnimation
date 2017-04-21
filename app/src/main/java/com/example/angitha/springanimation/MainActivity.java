package com.example.angitha.springanimation;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    float STIFFNESS = SpringForce.STIFFNESS_MEDIUM;
    float DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY;
    float dX = 0f;
    float dY = 0f;
    SpringAnimation xAnimation,yAnimation;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

        xAnimation = createSpringAnimation(
                imageView, SpringAnimation.X, imageView.getX(), STIFFNESS, DAMPING_RATIO);
        yAnimation = createSpringAnimation(
                imageView, SpringAnimation.Y, imageView.getY(), STIFFNESS, DAMPING_RATIO);

                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_MOVE:
                        imageView.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();

                        xAnimation.cancel();
                        yAnimation.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        xAnimation.start();
                        yAnimation.start();
                        break;
                }
                return true;
            }
        });
    }

    SpringAnimation createSpringAnimation(View view,
                                          DynamicAnimation.ViewProperty property,
                                          Float finalPosition,
                                          @FloatRange(from = 0.0) Float stiffness,
                                          @FloatRange(from = 0.0) Float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce spring = new SpringForce(finalPosition);
        spring.setStiffness(stiffness);
        spring.setDampingRatio(dampingRatio);
        animation.setSpring(spring);
        return animation;
    }
}
