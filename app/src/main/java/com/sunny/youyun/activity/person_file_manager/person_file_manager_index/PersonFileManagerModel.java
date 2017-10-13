package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.callback.SimpleListener;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerModel implements PersonFileManagerContract.Model {
    private final PersonFileManagerPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();

    PersonFileManagerModel(PersonFileManagerPresenter personFileManagerPresenter) {
        mPresenter = personFileManagerPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void getUploadFilesOnline(String parent) {
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .getUploadFiles()
                .map(baseResponseBody -> {
                    if (baseResponseBody.isSuccess()) {
                        mList.clear();
                        Collections.addAll(mList, baseResponseBody.getData());
                        return true;
                    }
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mPresenter.getUploadFilesSuccess();
                        } else {
                            mPresenter.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取上传文件列表失败: " + e.getMessage(), e);
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void createDirectory(String parentId, String name) {
        EasyYouyunAPIManager.createNewDirectory(parentId, name, new SimpleListener() {
            @Override
            public void onSuccess() {
                mPresenter.createDirectorySuccess();
                mPresenter.getUploadFilesOnline(null);
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(ApiInfo.CREATE_DIRECTORY_NAME, name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(
//                MediaType.parse(ApiInfo.MEDIA_TYPE_JSON), jsonObject.toString()
//        );
//        APIManager.getInstance()
//                .getFileServices(GsonConverterFactory.create())
//                .createDirectory(body)
//                .map(baseResponseBody -> {
//                    if (baseResponseBody.isSuccess()) {
//                        return true;
//                    }
//                    return false;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Boolean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mPresenter.addSubscription(d);
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        Logger.e("创建文件夹失败", e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    @Override
    public void delete(final String id, final int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.DELETE_FILE_OR_DIRECTORY_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        RequestBody body = RequestBody.create(
                MediaType.parse(ApiInfo.MEDIA_TYPE_JSON), jsonObject.toString()
        );
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .deleteFileOrDirectory(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody baseResponseBody) {
                        if(baseResponseBody.isSuccess()) {
                            mPresenter.deleteSuccess(position);
                            mList.remove(position);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("删除文件夹或文件失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
