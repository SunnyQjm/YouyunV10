package com.sunny.youyun.base;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sunny.youyun.mvp.BasePresenter;


/**
 * Created by Administrator on 2017/3/18 0018.
 */

public abstract class MVPBaseFragment<P extends BasePresenter> extends Fragment {
    protected P mPresenter;
    protected Activity activity;
    protected OnFragmentInteractionListener mListener;
    protected OnFragmentListener paramListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mPresenter == null){
            mPresenter = onCreatePresenter();
        }
        if(activity == null){
            activity = getActivity();
        }
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
        activity = (Activity) context;
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

    public void showTip(String info) {
//        if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
//            sweetAlertDialog.dismissWithAnimation();
//        if(info.contains("404")){
//            sweetAlertDialog = EasySweetAlertDialog.ShowTip(activity, "未登录或登录失效");
//        }else{
//            sweetAlertDialog = EasySweetAlertDialog.ShowTip(activity, info);
//        }
    }

    public void showSuccess(String info){
//        if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
//            sweetAlertDialog.dismissWithAnimation();
//        sweetAlertDialog = EasySweetAlertDialog.ShowSuccess(activity, info);
    }

    public void showError(String info){
//        if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
//            sweetAlertDialog.dismissWithAnimation();
//        sweetAlertDialog = EasySweetAlertDialog.ShowError(activity, info);
    }

    protected void showProcess(String info){
//        if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
//            sweetAlertDialog.dismissWithAnimation();
//        sweetAlertDialog = EasySweetAlertDialog.ShowProcess(activity, info);
    }

    protected void dismissDialog(){
//        if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
//            sweetAlertDialog.dismissWithAnimation();
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
