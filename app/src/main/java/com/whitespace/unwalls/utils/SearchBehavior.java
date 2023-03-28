package com.whitespace.unwalls.utils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whitespace.unwalls.R;
import com.whitespace.unwalls.activity.MainActivity;


public class SearchBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    private boolean isSearchVisible = true;
    Context context;
    AttributeSet attrs;

    public SearchBehavior() {
    }

    public SearchBehavior(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
//        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, @NonNull View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (dyConsumed > 0 && isSearchVisible) {
            // Scrolling up, hide the EditText
            hideEditText(child);
        } else if (dyConsumed < 0 && !isSearchVisible) {
            // Scrolling down, show the EditText
            showEditText(child);
        }
    }

    private void hideEditText(RelativeLayout relativeLayout) {
        float floatValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
        relativeLayout.animate().translationY(floatValue).setInterpolator(new AccelerateInterpolator(2f)).start();
        isSearchVisible = false;
    }

    private void showEditText(RelativeLayout relativeLayout) {
        relativeLayout.animate().translationY(0f).setInterpolator(new DecelerateInterpolator(2f)).start();
        isSearchVisible = true;
    }
}
