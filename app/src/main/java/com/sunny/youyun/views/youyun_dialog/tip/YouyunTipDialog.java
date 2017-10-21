package com.sunny.youyun.views.youyun_dialog.tip;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunny.youyun.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class YouyunTipDialog extends DialogFragment {

    @DrawableRes
    private int drawableRes = -1;

    private String content;

    private OnYouyunTipDialogClickListener listener;
    private boolean isBtnVisiable = true;
    private String leftText = null;
    private String rightText = null;

    private static final int defaultIcon = R.drawable.icon_transfer_back;
    private static final String defaultContent = "提示";

    private static final String DRAWABLE_RES = "DRAWABLE_RES";
    private static final String CONTENT = "CONTENT";

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    Unbinder unbinder;

    private DialogInterface.OnDismissListener onDismissListener;
    public YouyunTipDialog() {
    }

    public void setListener(OnYouyunTipDialogClickListener listener) {
        this.listener = listener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        System.out.println("onDismiss");
        if(onDismissListener != null)
            onDismissListener.onDismiss(dialog);
    }

    public static YouyunTipDialog newInstance(final int drawableRes, final String content, final OnYouyunTipDialogClickListener listener) {
        Bundle args = new Bundle();
        args.putInt(DRAWABLE_RES, drawableRes);
        args.putString(CONTENT, content);
        YouyunTipDialog fragment = new YouyunTipDialog();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (drawableRes == -1) {
            drawableRes = getArguments().getInt(DRAWABLE_RES);
            content = getArguments().getString(CONTENT);
        }
        //去掉标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.exit_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void setText(String info) {
        if (tvText != null) {
            tvText.setText(info);
        }
        content = info;
    }

    private void setBtnVisible() {
        if (tvCancel != null)
            tvCancel.setVisibility(View.VISIBLE);
        if (tvSure != null)
            tvSure.setVisibility(View.VISIBLE);
    }

    private void setBtnInVisible() {
        if (tvCancel != null)
            tvCancel.setVisibility(View.INVISIBLE);
        if (tvSure != null)
            tvSure.setVisibility(View.INVISIBLE);
    }

    public void setBtnVisible(boolean isVisible) {
        isBtnVisiable = isVisible;
        if (isBtnVisiable) {
            setBtnVisible();
        } else {
            setBtnInVisible();
        }
    }

    public void setLeftText(String text){
        leftText = text;
        if(tvCancel != null)
            tvCancel.setText(text);
    }

    public void setRightText(String text){
        rightText = text;
        if(tvSure != null)
            tvSure.setText(text);
    }

    private void initView() {
        imgIcon.setImageResource(drawableRes);
        tvText.setText(content);
        if(leftText != null)
            tvCancel.setText(leftText);
        if(rightText != null)
            tvSure.setText(rightText);

        if (isBtnVisiable) {
            setBtnVisible();
        } else {
            setBtnInVisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (listener != null)
                    listener.onCancelClick();
                this.dismiss();
                break;
            case R.id.tv_sure:
                if (listener != null)
                    listener.onSureClick();
                this.dismiss();
                break;
        }
    }
}
