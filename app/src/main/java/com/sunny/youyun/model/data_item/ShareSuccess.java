package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.FileManageItem;

/**
 * Created by Sunny on 2017/10/22 0022.
 */

public class ShareSuccess {
    private final FileManageItem file;
    private final String downloadLink;

    private ShareSuccess(Builder builder) {
        file = builder.file;
        downloadLink = builder.downloadLink;
    }



    public FileManageItem getFile() {
        return file;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public static ShareSuccess empty(){
        return new Builder()
                .file(FileManageItem.empty())
                .downloadLink("")
                .build();
    }


    public static final class Builder {
        private FileManageItem file;
        private String downloadLink;

        public Builder() {
        }

        public Builder file(FileManageItem val) {
            file = val;
            return this;
        }

        public Builder downloadLink(String val) {
            downloadLink = val;
            return this;
        }

        public ShareSuccess build() {
            return new ShareSuccess(this);
        }
    }
}
