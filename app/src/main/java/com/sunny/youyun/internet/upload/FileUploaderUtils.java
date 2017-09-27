package com.sunny.youyun.internet.upload;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.event.FileUploadEvent;
import com.sunny.youyun.internet.progress_handle.ProgressRequestBody;
import com.sunny.youyun.internet.progress_handle.ProgressRequestListener;
import com.sunny.youyun.internet.service.FileServices;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.MD5Util;
import com.sunny.youyun.utils.Tool;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/25 0025.
 */

public class FileUploaderUtils {

    public interface FileUploadCallback {
        void onSuccess(InternetFile internetFile, int position);

        void onFinish(InternetFile internetFile, int position);

        void onSubscribe(Disposable disposable);

        void onError(InternetFile internetFile, int position);
    }

    public static void uploadWithCheck(final InternetFile internetFile, final FileUploadFileParam uploadFileParam,
                                       final int position, FileUploadCallback callback) throws IOException {
        File f = new File(uploadFileParam.getFilePath());
        if (!f.exists())
            return;

        String md5 = MD5Util.getFileMD5(new FileInputStream(f));
        String MIME = FileTypeUtil.getMyMIME(f.getName());
        long size = f.length();
        //构造检查文件秒传判断的请求
        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                getCheckJson(md5, MIME, size, f.getName(), uploadFileParam));

        final FileServices fileServices = APIManager.getInstance().getFileServices(GsonConverterFactory.create());
        fileServices
                .checkMd5(body)
//                .retry(1)
                .flatMap(stringBaseResponseBody -> {
                    if (stringBaseResponseBody.isSuccess()) {
                        return fileServices.uploadFile(getPartMap(md5, MIME, size, f.getName()
                                , uploadFileParam), getFile(f, position, internetFile));
                    } else {
                        //when a same file is already upload before, we don't need to do upload operator
                        //just do something on this, do not care the under operator
                        internetFile.setStatus(InternetFile.Status.FINISH);
                        internetFile.setIdentifyCode(stringBaseResponseBody.getMsg());
                        EventBus.getDefault().post(new FileUploadEvent.Builder()
                                .position(position)
                                .type(FileUploadEvent.Type.FINISH)
                                .build());
                        if (callback != null)
                            callback.onSuccess(internetFile, position);
                        save(internetFile);
                        return Observable.empty();      //do nothing
                    }
                })
//                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "文件上传失败");
                        internetFile.setRate("上传错误");
                        internetFile.setStatus(InternetFile.Status.ERROR);
                        EventBus.getDefault()
                                .post(new FileUploadEvent.Builder()
                                        .type(FileUploadEvent.Type.ERROR)
                                        .position(position)
                                        .build());
                        if (callback != null)
                            callback.onError(internetFile, position);
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (callback != null)
                            callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        if (stringBaseResponseBody.isSuccess()) {
                            internetFile.setStatus(InternetFile.Status.FINISH);
                            internetFile.setIdentifyCode(stringBaseResponseBody.getData());
                            EventBus.getDefault()
                                    .post(new FileUploadEvent.Builder()
//                                                    .identifyCode(stringBaseResponseBody.getData())
                                            .type(FileUploadEvent.Type.FINISH)
                                            .position(position)
                                            .build());
                            if (callback != null)
                                callback.onSuccess(internetFile, position);
                            save(internetFile);
                        } else {
                            Logger.i("上传失败");
                            internetFile.setRate("上传错误");
                            internetFile.setStatus(InternetFile.Status.ERROR);
                            EventBus.getDefault()
                                    .post(new FileUploadEvent.Builder()
                                            .type(FileUploadEvent.Type.ERROR)
                                            .position(position)
                                            .build());
                            if (callback != null)
                                callback.onError(internetFile, position);
                        }
                        if (callback != null)
                            callback.onFinish(internetFile, position);
                    }
                });
    }

    private static void save(InternetFile internetFile) {
        boolean b = internetFile.saveOrUpdate("identifyCode=? and fileTAG=?", internetFile.getIdentifyCode(),
                String.valueOf(InternetFile.TAG_UPLOAD));
        Logger.i("保存：" + b);
    }

    private static MultipartBody.Part getFile(final File f, final int position, InternetFile internetFile) {
        return MultipartBody.Part.createFormData("file", f.getName(),
                new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), f),
                        new ProgressRequestListener() {
                            long lastBytes;
                            long startTime = System.currentTimeMillis();
                            long lastTime = startTime;

                            @Override
                            public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                                if (contentLength == 0) {
                                    Logger.i("contentLength = 0");
                                    return;
                                }
                                long span = System.currentTimeMillis() - lastTime;
                                if (span < 200 && !done)        //文件未传输完，且间隔太短，则不更新进度
                                    return;
                                lastTime += span;
                                String rate = Tool.convertToRate(bytesWritten - lastBytes, span);
                                lastBytes = bytesWritten;
                                if (done) {
                                    internetFile.setStatus(InternetFile.Status.FINISH);
                                } else {
                                    internetFile.setStatus(InternetFile.Status.DOWNLOADING);
                                }
                                internetFile.setRate(rate);
                                internetFile.setProgress((int) (bytesWritten * 1.0 / contentLength * 100));
                                EventBus.getDefault()
                                        .post(new FileUploadEvent.Builder()
                                                .position(position)
                                                .type(FileUploadEvent.Type.PROGRESS)
                                                .build());
                            }
                        }));
    }

    private static Map<String, RequestBody> getPartMap(String md5, String MIME, long size, String name,
                                                       FileUploadFileParam uploadFileParam) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put(ApiInfo.UPLOAD_FILE_PARAM_SHARE,
                RequestBody.create(null, String.valueOf(uploadFileParam.isShare())));
        if (uploadFileParam.getAllowDownCount() > 0)
            map.put(ApiInfo.UPLOAD_FILE_PARAM_LEFT_ALLOW_DOWNLOAD_COUNT
                    , RequestBody.create(null, String.valueOf(uploadFileParam.getAllowDownCount())));
        if (uploadFileParam.getExpireTime() > 0)
            map.put(ApiInfo.UPLOAD_FILE_PARAM_EXPIRE_TIME,
                    RequestBody.create(null, String.valueOf(uploadFileParam.getExpireTime())));
        map.put(ApiInfo.UPLOAD_FILE_PARAM_SCORE,
                RequestBody.create(null, String.valueOf(uploadFileParam.getScore())));
        map.put(ApiInfo.UPLOAD_FILE_PARAM_IS_PRIVATE,
                RequestBody.create(null, String.valueOf(uploadFileParam.isPrivate())));
        map.put(ApiInfo.UPLOAD_FILE_PARAM_DESCRIPTION,
                RequestBody.create(null, String.valueOf(uploadFileParam.getDescription())));

        if (MIME != null) {
            map.put(ApiInfo.UPLOAD_FILE_PARAM_MIME,
                    RequestBody.create(null, MIME));
        } else {
            map.put(ApiInfo.UPLOAD_FILE_PARAM_MIME,
                    RequestBody.create(null, "application/octet-stream"));
        }
        map.put(ApiInfo.UPLOAD_FILE_PARAM_MD5,
                RequestBody.create(null, md5));

        map.put(ApiInfo.UPLOAD_FILE_PARAM_SIZE,
                RequestBody.create(null, String.valueOf(size)));
        map.put(ApiInfo.UPLOAD_FILE_PARAM_NAME,
                RequestBody.create(null, name));
        if (uploadFileParam.getParentId() != null)
            map.put(ApiInfo.UPLOAD_FILE_PARAM_PARENT_PATH,
                    RequestBody.create(null, uploadFileParam.getParentId()));
        return map;
    }

    private static String getCheckJson(String md5, String MIME, long size, String name,
                                       FileUploadFileParam uploadFileParam) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
            if (uploadFileParam.getAllowDownCount() > 0)
                jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_LEFT_ALLOW_DOWNLOAD_COUNT
                        , uploadFileParam.getAllowDownCount());
            if (uploadFileParam.getExpireTime() > 0)
                jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_EXPIRE_TIME,
                        uploadFileParam.getExpireTime());
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_SCORE,
                    uploadFileParam.getScore());
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_IS_PRIVATE,
                    uploadFileParam.isPrivate());
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_DESCRIPTION,
                    uploadFileParam.getDescription());
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_NAME, name);
            if (uploadFileParam.getParentId() != null)
                jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_PARENT_PATH,
                        uploadFileParam.getParentId());
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_MIME, MIME);
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_MD5, md5);
            jsonObject.put(ApiInfo.UPLOAD_FILE_PARAM_SIZE, size);
        } catch (JSONException e) {
            Logger.e(e, "参数构建错误");
        }
        return jsonObject.toString();
    }
}
