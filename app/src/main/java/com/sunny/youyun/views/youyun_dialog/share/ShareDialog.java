package com.sunny.youyun.views.youyun_dialog.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.R;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.share.TencentUtil;
import com.sunny.youyun.utils.share.WechatUtil;
import com.sunny.youyun.views.MyPopupWindow;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/25 0025.
 */

public class ShareDialog {

    private MyPopupWindow myPopupWindow;
    private final Activity context;
    private final ShareContent shareContent;
    private IUiListener iUiListener;
    private RecyclerView shareRecyclerView;
    private RecyclerView otherFunctionRecyclerView;
    private ShareAdapter shareAdapter;
    private ShareAdapter otherFunctionAdapter;
    private PopupWindow.OnDismissListener onDismissListener;

    private static final int SHARE_TO_QQ = 0;
    private static final int SHARE_TO_QQ_ZONE = 1;
    private static final int SHARE_COPY_LINK = 2;
    private static final int SHARE_TO_WE_CHAT = 3;
    private static final int SHARE_FRIEND_CIRCLE = 4;
    private static final int SHARE_COPY_CODE = 5;
    private static final int SHARE_COLLECT = 6;

    public ShareDialog(@NonNull final Activity context, ShareContent shareContent) {
        this.context = context;
        this.shareContent = shareContent;
        initMsg();
        initView();
    }

    public ShareDialog(@NonNull final Activity context, ShareContent shareContent,
                       OnCollectionListener listener) {
        this.context = context;
        this.shareContent = shareContent;
        this.onCollectionListener = listener;
        initMsg();
        initView();
    }

    private void initMsg() {
        List<ShareNode> shareNodes = new ArrayList<>();
        shareNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_qq)
                .name(context.getString(R.string.QQ))
                .id(SHARE_TO_QQ)
                .build());
        shareNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon__qq_zone)
                .name(context.getString(R.string.QQ_ZONE))
                .id(SHARE_TO_QQ_ZONE)
                .build());

        shareNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_wechat)
                .name(context.getString(R.string.WE_CHAT))
                .id(SHARE_TO_WE_CHAT)
                .build());

        shareNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_friend_circle)
                .name(context.getString(R.string.FRIEND_CIRCLE))
                .id(SHARE_FRIEND_CIRCLE)
                .build());

        shareNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_share_collect)
                .name(shareContent.isCanStore() ? context.getString(R.string.collection) :
                        context.getString(R.string.cancel_collection))
                .id(SHARE_COLLECT)
                .build());
        shareAdapter = new ShareAdapter(shareNodes);

        List<ShareNode> otherFunctionNodes = new ArrayList<>();
        otherFunctionNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_copy_link)
                .name(context.getString(R.string.copy_link))
                .id(SHARE_COPY_LINK)
                .build());
        otherFunctionNodes.add(new ShareNode.Builder()
                .icon(R.drawable.icon_copy_code)
                .name(context.getString(R.string.copy_code))
                .id(SHARE_COPY_CODE)
                .build());
        otherFunctionAdapter = new ShareAdapter(otherFunctionNodes);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.share_content_layout, null);
        shareRecyclerView = (RecyclerView) view.findViewById(R.id.first_line_share_item);
        shareRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        shareAdapter.bindToRecyclerView(shareRecyclerView);
        shareAdapter.setOnItemClickListener((adapter, view1, position) -> {
            ShareNode shareNode = (ShareNode) adapter.getData().get(position);
            switch (shareNode.getId()) {
                case SHARE_TO_QQ:
                    TencentUtil.getInstance(context)
                            .shareToQQ(shareContent, iUiListener);
                    break;
                case SHARE_TO_QQ_ZONE:
                    TencentUtil.getInstance(context)
                            .shareToQzon(shareContent, iUiListener);
                    break;
                case SHARE_TO_WE_CHAT:
                    WechatUtil.getInstance(context)
                            .shareToWechatSession(shareContent);
                    break;
                case SHARE_FRIEND_CIRCLE:
                    WechatUtil.getInstance(context)
                            .shareToWechatTimeline(shareContent);
                    break;
                case SHARE_COLLECT:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(ApiInfo.FILE_COLLECT_FILE_ID, shareContent.getFileId());
                        jsonObject.put(ApiInfo.FILE_COLLECT_TYPE, shareContent.isCanStore() ? 1 : 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse(ApiInfo.MEDIA_TYPE_JSON),
                            jsonObject.toString());
                    APIManager.getInstance()
                            .getFileServices(GsonConverterFactory.create())
                            .collect(body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResponseBody>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(BaseResponseBody baseResponseBody) {
                                    if(baseResponseBody.isSuccess()){
                                        if(onCollectionListener != null){
                                            onCollectionListener.onCollectionSuccess();
                                        }
                                    } else {
                                        if(onCollectionListener != null) {
                                            onCollectionListener.onCollectionFailed();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("收藏失败", e);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                    break;
            }
            myPopupWindow.dismiss();
        });
        otherFunctionRecyclerView = (RecyclerView) view.findViewById(R.id.second_line_share_item);
        otherFunctionRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        otherFunctionAdapter.bindToRecyclerView(otherFunctionRecyclerView);
        otherFunctionAdapter.setOnItemClickListener(((adapter, view1, position) -> {
            ShareNode shareNode = (ShareNode) adapter.getData().get(position);
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm == null) {
                myPopupWindow.dismiss();
                return;
            }
            switch (shareNode.getId()) {
                case SHARE_COPY_LINK:
                    ClipData data = new ClipData("share", new String[]{"text/plain"},
                            new ClipData.Item(shareContent.getShareUrl()));
                    cm.setPrimaryClip(data);
                    Toast.makeText(context, context.getString(R.string.link_already_copy_to_clipbpard), Toast.LENGTH_SHORT).show();
                    break;
                case SHARE_COPY_CODE:
                    data = new ClipData("share", new String[]{"text/plain"},
                            new ClipData.Item(shareContent.getIdentifyCode()));
                    cm.setPrimaryClip(data);
                    Toast.makeText(context, context.getString(R.string.code_already_copy_to_clipbpard), Toast.LENGTH_SHORT).show();
                    break;
            }
            myPopupWindow.dismiss();
        }));
        myPopupWindow = new MyPopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        myPopupWindow.setOnDismissListener(() -> {
            if (onDismissListener != null)
                onDismissListener.onDismiss();
        });
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void show(@NonNull final View parent, IUiListener iUiListener) {
        if (iUiListener == null)
            iUiListener = new MyIUListener(context);
        this.iUiListener = iUiListener;
        myPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    public static class MyIUListener implements IUiListener {

        private final Context context;

        public MyIUListener(Context context) {
            this.context = context;
        }

        @Override
        public void onComplete(Object o) {
            System.out.println("completed");
            Logger.i("share completed: " + o);
        }

        @Override
        public void onError(UiError uiError) {
            System.out.println("error");
            com.orhanobut.logger.Logger.e("分享错误", uiError);
        }

        @Override
        public void onCancel() {
            System.out.println("cancel");
            Logger.i("share cancel");
        }
    }

    public static class ShareNode {
        private final int id;
        private final String name;
        @DrawableRes
        private final int icon;

        private ShareNode(Builder builder) {
            id = builder.id;
            name = builder.name;
            icon = builder.icon;
        }


        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

        public static final class Builder {
            private int id;
            private String name;
            private int icon;

            public Builder() {
            }

            public Builder id(int val) {
                id = val;
                return this;
            }

            public Builder name(String val) {
                name = val;
                return this;
            }

            public Builder icon(int val) {
                icon = val;
                return this;
            }

            public ShareNode build() {
                return new ShareNode(this);
            }
        }
    }


    private OnCollectionListener onCollectionListener = null;

    public interface OnCollectionListener{
        void onCollectionSuccess();
        void onCollectionFailed();
    }
}
