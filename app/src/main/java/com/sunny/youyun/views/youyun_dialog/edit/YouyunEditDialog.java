package com.sunny.youyun.views.youyun_dialog.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.sunny.youyun.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

public class YouyunEditDialog extends DialogFragment {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.editText)
    EditText editText;
    Unbinder unbinder;
    private View view = null;

    private String title = "";
    private String hint = "";
    private int inputType = InputType.TYPE_CLASS_TEXT;

    private OnYouyunEditDialogClickListener listener;

    public YouyunEditDialog() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置允许输入的类型
     * @param inputType
     */
    public YouyunEditDialog setInputType(int inputType) {
        this.inputType = inputType;
        if(editText != null)
            editText.setInputType(inputType);
        return this;
    }

    public void setHint(String hint) {
        this.hint = hint;
        if(editText != null)
            editText.setText(hint);
    }

    public void setListener(OnYouyunEditDialogClickListener listener) {
        this.listener = listener;
    }

    public static YouyunEditDialog newInstance(String title, OnYouyunEditDialogClickListener listener) {
        Bundle args = new Bundle();

        YouyunEditDialog fragment = new YouyunEditDialog();
        fragment.setArguments(args);
        fragment.setTitle(title);
        fragment.setListener(listener);
        return fragment;
    }

    public static YouyunEditDialog newInstance(String title, String hint, OnYouyunEditDialogClickListener listener) {
        Bundle args = new Bundle();
        YouyunEditDialog fragment = new YouyunEditDialog();
        fragment.setArguments(args);
        fragment.setTitle(title);
        fragment.setHint(hint);
        fragment.setListener(listener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //去掉标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (view == null) {
            view = inflater.inflate(R.layout.youyun_edit_dialog, container, false);
            unbinder = ButterKnife.bind(this, view);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        initView();
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        tvTitle.setText(title);
        editText.setText(hint);
        editText.setInputType(inputType);
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
                this.dismiss();
                break;
            case R.id.tv_sure:
                if (listener != null) {
                    listener.onResult(editText.getText().toString());
                }
                this.dismiss();
                break;
        }
    }

    public void clear() {
        hint = "";
    }

    public interface OnYouyunEditDialogClickListener {
        void onResult(String result);
    }

    public void show(FragmentManager manager, String tag, String newValue) {
        this.hint = newValue;
        super.show(manager, tag);
    }
}
