package com.sunny.youyun.mvp;

import android.content.Context;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sunny on 2017/5/12 0012.
 */

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
    protected V mView;
    protected M mModel;
    private CompositeDisposable compositeDisposable;
    private final Context context;

    public BasePresenter(){
        context = null;
    }

    protected BasePresenter(Context context){
        this.context = context;
    }

    protected abstract void start() throws IOException;

    /**
     * RxJava注册
     * @param disposable
     */
    public void addSubscription(Disposable disposable){
        if(compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();
        if(disposable != null){
            compositeDisposable.add(disposable);
        }
    }


    /**
     * 取消单个订阅
     * @param disposable
     */
    public void removeSubcription(Disposable disposable) {
        if(disposable != null){
            compositeDisposable.remove(disposable);
        }
    }

    /**
     * RxJava取消注册，以免内存泄露
     * disposable是1.x的Subscription改名的，因为Reactive-Streams规范用这个名称，为了避免重复
     * 这个回调方法是在2.0之后新添加的
     * 可以使用d.dispose()方法来取消订阅
     */
    public void clearAllDisposable(){
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
    }

    public void showSuccess(String info){
        mView.showSuccess(info);
    }
    public void showError(String info){
        mView.showError(info);
    }
    public void showTip(String info){
        mView.showTip(info);
    }
    public void dismissDialog(){
        mView.dismissDialog();
    }
    public void showLoading(){
        mView.showLoading();
    }
    public Context getContext(){
        return context;
    }
}
