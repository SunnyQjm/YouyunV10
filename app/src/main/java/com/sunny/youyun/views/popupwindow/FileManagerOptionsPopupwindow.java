package com.sunny.youyun.views.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sunny.youyun.R;
import com.sunny.youyun.views.MyPopupWindow;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

public enum FileManagerOptionsPopupwindow implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private TextView tvCancel;
    private Button btnShare;
    private Button btnMove;
    private Button btnRename;
    private Button btnDelete;
    private View view = null;
    private MyPopupWindow popupWindow;
    private OnOptionClickListener mListener;
    private int position = -1;

    public static FileManagerOptionsPopupwindow bind(@NonNull Context context, OnOptionClickListener mListener) {
        INSTANCE.view = LayoutInflater.from(context).inflate(R.layout.person_file_manager_options, null, false);
        INSTANCE.popupWindow = new MyPopupWindow(INSTANCE.view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        INSTANCE.mListener = mListener;
        INSTANCE.bindListener();
        return INSTANCE;
    }

    private void bindListener() {
        if(view != null){
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(this);
            btnDelete = (Button) view.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(this);
            btnMove = (Button) view.findViewById(R.id.btn_move);
            btnMove.setOnClickListener(this);
            btnRename = (Button) view.findViewById(R.id.btn_rename);
            btnRename.setOnClickListener(this);
            btnShare = (Button) view.findViewById(R.id.btn_share);
            btnShare.setOnClickListener(this);
            popupWindow.setOnDismissListener(() -> {
                if(mListener != null)
                    mListener.onDismiss();
            });
        }
    }

    public static void unbind(){
        INSTANCE.view = null;
        INSTANCE.popupWindow = null;
    }

    public void show(View parent, int position) {
        this.position = position;
        if(popupWindow != null)
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 如果弹窗已创建并且处于显示状态，就dismiss
     * @param position
     */
    public void dismiss(int position){
        if(popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                INSTANCE.dismiss(position);
                break;
            case R.id.btn_share:
                //TODO share
                if(mListener != null)
                    mListener.onShareClick(position);
                break;
            case R.id.btn_move:
                //TODO move
                if(mListener != null)
                    mListener.onMoveClick(position);
                break;
            case R.id.btn_rename:
                //TODO rename
                if(mListener != null)
                    mListener.onRenameClick(position);
                break;
            case R.id.btn_delete:
                //TODO delete
                if(mListener != null)
                    mListener.onDeleteClick(position);
                break;
        }
    }

    public interface OnOptionClickListener{
        void onCancelClick();
        void onRenameClick(int position);
        void onDeleteClick(int position);
        void onMoveClick(int position);
        void onShareClick(int position);
        void onDismiss();
    }
}
