package com.sunny.youyun.model.result;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;

import java.util.List;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

public class GetUserInfoResult {
    private final List<InternetFile> uploadList;
    private final List<InternetFile> downloadList;
    private final User user;

    private GetUserInfoResult(Builder builder) {
        uploadList = builder.uploadList;
        downloadList = builder.downloadList;
        user = builder.user;
    }

    public List<InternetFile> getUploadList() {
        return uploadList;
    }

    public List<InternetFile> getDownloadList() {
        return downloadList;
    }

    public User getUser() {
        return user;
    }


    public static final class Builder {
        private List<InternetFile> uploadList;
        private List<InternetFile> downloadList;
        private User user;

        public Builder() {
        }

        public Builder uploadList(List<InternetFile> val) {
            uploadList = val;
            return this;
        }

        public Builder downloadList(List<InternetFile> val) {
            downloadList = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public GetUserInfoResult build() {
            return new GetUserInfoResult(this);
        }
    }
}
