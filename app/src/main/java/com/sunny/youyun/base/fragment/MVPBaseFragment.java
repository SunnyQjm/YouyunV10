package com.sunny.youyun.base.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.youyun_dialog.loading.YouyunLoadingView;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;


/**
 * Created by Administrator on 2017/3/18 0018.
 */

public abstract class MVPBaseFragment<P extends BasePresenter> extends Fragment {
    protected P mPresenter;
    protected AppCompatActivity activity;
    protected OnFragmentInteractionListener mListener;
    protected OnFragmentListener paramListener;

    protected YouyunTipDialog dialog;
    protected YouyunLoadingView loadingView;

    //用来标识保存Fragment的显示状态
    public static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mPresenter == null){
            mPresenter = onCreatePresenter();
        }
        //页面重启时，Fragment会被保存恢复，而此时再加载Fragment，会导致重叠
        if(savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if(isHidden){
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.clearAllDisposable();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(context instanceof OnFragmentListener){
            paramListener = (OnFragmentListener) context;
        }
        activity = (AppCompatActivity) context;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    public boolean onBackPressed(){
        return false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        paramListener = null;
        //当Activity被销毁时，取消所有订阅
        if (mPresenter != null) {
            mPresenter.clearAllDisposable();
        }
    }

    /**
     * 显示提示
     *
     * @param info 要提示的信息
     */
    public void showTip(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showTip(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showSuccess(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showSuccess(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showError(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showError(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    protected void showLoading() {
        dismissDialog();
        loadingView = EasyDialog.showLoading(activity);
    }

    protected void dismissDialog() {
        if (dialog != null && !dialog.isHidden())
            dialog.dismiss();
        if (loadingView != null && loadingView.isShowing())
            loadingView.dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnFragmentListener{
        void integerParam(Integer code, Object... params);
    }

    protected abstract P onCreatePresenter();
}
