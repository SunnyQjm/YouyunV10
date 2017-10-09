package com.sunny.youyun.activity.file_detail_online;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.Comment;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.GsonUtil;

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
 * Created by Sunny on 2017/8/19 0019.
 */

class FileDetailOnlineModel implements FileDetailOnlineContract.Model {
    private FileDetailOnlinePresenter mPresenter;
    private final List<Comment> commentList = new ArrayList<>();


    FileDetailOnlineModel(FileDetailOnlinePresenter fileDetailOnlinePresenter) {
        mPresenter = fileDetailOnlinePresenter;
    }

    @Override
    public void addComment(int fileId, String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.ADD_COMMENT_FILE_ID, fileId);
            jsonObject.put(ApiInfo.ADD_COMMENT_CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //构造检查文件秒传判断的请求
        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString());

        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .addComment(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        if(stringBaseResponseBody.isSuccess())
                            mPresenter.commentsSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("评论失败", e);
                        mPresenter.showError("评论失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getComments(int fileId, boolean isFirst) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .getComments(fileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<Comment[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<Comment[]> listBaseResponseBody) {
                        Logger.i(GsonUtil.getInstance().toJson(listBaseResponseBody));
                        if(listBaseResponseBody.isSuccess()){
                            commentList.clear();
                            Collections.addAll(commentList, listBaseResponseBody.getData());
                            mPresenter.getCommentSuccess(isFirst);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e("获取评论列表失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getFileInfo(String code) {
        mPresenter.showLoading();
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .getFileInfo(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<InternetFile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<InternetFile> baseResponseBody) {
                        if(baseResponseBody.isSuccess() && baseResponseBody.getData() != null){
                            mPresenter.getFileInfoSuccess(baseResponseBody.getData());
                        } else {
                            Logger.i("获取文件信息失败");
                        }
                        mPresenter.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mPresenter.showError("获取文件信息失败：" + e);
                        Logger.e("获取文件信息失败", e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("completed");
                    }
                });
    }

    @Override
    public void star(int fileId) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .star(fileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        if(stringBaseResponseBody.isSuccess()){
                            //点赞成功则再去重新获取文件信息
                            mPresenter.starSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("点赞失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<Comment> getCommentList() {
        return commentList;
    }
}
