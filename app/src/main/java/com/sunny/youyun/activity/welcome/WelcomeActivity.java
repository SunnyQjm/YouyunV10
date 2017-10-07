package com.sunny.youyun.activity.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.utils.RouterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.welcome_layout)
    ConstraintLayout welcomeLayout;
//    @BindView(R.id.draggableView)
//    DraggableFlagView draggableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_welcome_acivity);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
//        draggableView.setText("8");
//        draggableView.setOnDraggableFlagViewListener(new DraggableFlagView.OnDraggableFlagViewListener() {
//            @Override
//            public void onFlagDismiss(DraggableFlagView view) {
//                Toast.makeText(WelcomeActivity.this, "drag", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        welcomeLayout.postDelayed(() -> {
            RouterUtils.open(WelcomeActivity.this, IntentRouter.MainActivity);
            finish();
        }, 500);
    }
}
