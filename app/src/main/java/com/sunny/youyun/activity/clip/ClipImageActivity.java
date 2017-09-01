package com.sunny.youyun.activity.clip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.clip.config.ClipImageConfig;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.clip_image.ClipImageLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

@Router(value = IntentRouter.ClipImageActivity + "/:" + ClipImageConfig.PATH,
        stringParams = ClipImageConfig.PATH)
public class ClipImageActivity extends MVPBaseActivity<ClipImagePresenter> implements ClipImageContrat.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.clip_header_clip_image_layout)
    ClipImageLayout clipHeaderClipImageLayout;

    private Observable<File> saveFileTO = Observable.create(e -> {
        File file = new File(FileUtils.getAppPath() + "clip_image_tmp.png");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            clipHeaderClipImageLayout.clip(0.3f)
                    //变为100~200k左右，0.2是50~100k左右
                    .compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
            e.onError(e1);
        } finally {
            if(fos != null)
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        e.onNext(file);
        e.onComplete();
    });

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.select_avatar));
        easyBar.setRightIcon(R.drawable.icon_sure);
        easyBar.setRightIconVisible();
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO clip image hre
                showLoading();
                mPresenter.saveFile(saveFileTO);
            }
        });
        String path = getIntent().getStringExtra(ClipImageConfig.PATH);
        System.out.println("path: " + path);
        clipHeaderClipImageLayout.setImagePath(path);
    }

    @Override
    protected ClipImagePresenter onCreatePresenter() {
        return new ClipImagePresenter(this);
    }

    @Override
    public void updateSuccess() {
        dismissDialog();
        finish();
    }

    @Override
    public void updateFail() {
        finish();
    }
}
