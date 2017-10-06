package com.sunny.youyun.model;

import android.graphics.Bitmap;

import com.tencent.connect.share.QQShare;

/**
 * Created by Sunny on 2017/8/25 0025.
 */

public class ShareContent {
    /**
     * 分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
     * QQShare.SHARE_TO_QQ_KEY_TYPE
     */
    private final int shareType;

    /**
     * 这条分享消息被好友点击后的跳转URL。
     * QQShare.PARAM_TARGET_URL
     */
    private final String shareUrl;

    /**
     * 分享的标题, 最长30个字符。
     * QQShare.PARAM_TITLE
     */
    private final String shareTitle;

    /**
     * 分享的消息摘要，最长40个字。
     * QQShare.PARAM_SUMMARY (可选)
     */
    private final String shareSummary;

    /**
     * 分享图片的URL或者本地路径
     * QQShare.SHARE_TO_QQ_IMAGE_URL (可选)
     */
    private final String shareImageUrl;

    /**
     * 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     * QQShare.SHARE_TO_QQ_APP_NAME	 (可选)
     */
    private final String shareAppName;

    /**
     * 分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
     QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
     QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
     *  QQShare.SHARE_TO_QQ_EXT_INT (可选)
     */
    private final int shareExtra;

    private final String identifyCode;

    private final Bitmap thumb;

    /**
     * 是否可收藏
     */
    private final boolean canStore;
    private final int fileId;


    private ShareContent(Builder builder) {
        shareType = builder.shareType;
        shareUrl = builder.shareUrl;
        shareTitle = builder.shareTitle;
        shareSummary = builder.shareSummary;
        shareImageUrl = builder.shareImageUrl;
        shareAppName = builder.shareAppName;
        shareExtra = builder.shareExtra;
        identifyCode = builder.identifyCode;
        thumb = builder.thumb;
        canStore = builder.canStore;
        fileId = builder.fileId;
    }


    public int getFileId() {
        return fileId;
    }

    public boolean isCanStore() {
        return canStore;
    }

    public int getShareType() {
        return shareType;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public String getShareSummary() {
        return shareSummary;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public String getShareAppName() {
        return shareAppName;
    }

    public int getShareExtra() {
        return shareExtra;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public static final class Builder {
        private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
        private String shareUrl;
        private String shareTitle;
        private String shareSummary;
        private String shareImageUrl;
        private String shareAppName;
        private int shareExtra;
        private String identifyCode;
        private Bitmap thumb;
        private boolean canStore = true;
        private int fileId;

        public Builder() {
        }

        public Builder shareType(int val) {
            shareType = val;
            return this;
        }

        public Builder shareUrl(String val) {
            shareUrl = val;
            return this;
        }

        public Builder shareTitle(String val) {
            shareTitle = val;
            return this;
        }

        public Builder shareSummary(String val) {
            shareSummary = val;
            return this;
        }

        public Builder shareImageUrl(String val) {
            shareImageUrl = val;
            return this;
        }

        public Builder shareAppName(String val) {
            shareAppName = val;
            return this;
        }

        public Builder shareExtra(int val) {
            shareExtra = val;
            return this;
        }

        public Builder identifyCode(String val) {
            identifyCode = val;
            return this;
        }

        public Builder thumb(Bitmap val) {
            thumb = val;
            return this;
        }

        public Builder canStore(boolean val) {
            canStore = val;
            return this;
        }

        public Builder fileId(int val) {
            fileId = val;
            return this;
        }


        public ShareContent build() {
            return new ShareContent(this);
        }
    }
}
