package com.sunny.youyun.views.behavior;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by Sunny on 2017/10/8 0008.
 */

public class ScrollingDownBehavior extends AppBarLayout.ScrollingViewBehavior{
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            // Offset the child, pinning it to the bottom the header-dependency, maintaining
            // any vertical gap and overlap
            final AppBarLayout.Behavior ablBehavior = (AppBarLayout.Behavior) behavior;
            ViewCompat.offsetTopAndBottom(child, -(dependency.getBottom() - child.getTop()));
        }
    }
}
