package com.sunny.youyun.internet.upload;

public class FileUploadFileParam {
    private final String filePath;
    private final boolean isShare;
    private final int allowDownCount;
    private final long expireTime;
    private final int score;
    private final boolean isPrivate;
    private final String description;
    private final String parentId;

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isShare() {
        return isShare;
    }

    public String getDescription() {
        return description;
    }

    public int getAllowDownCount() {
        return allowDownCount;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public int getScore() {
        return score;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getParentId() {
        return parentId;
    }

    private FileUploadFileParam(Builder builder) {
        filePath = builder.filePath;
        isShare = builder.isShare;
        allowDownCount = builder.allowDownCount;
        expireTime = builder.expireTime;
        score = builder.score;
        isPrivate = builder.isPrivate;
        description = builder.description;
        parentId = builder.parentId;
    }


    public static final class Builder {
        private String filePath;
        private boolean isShare;
        private int allowDownCount;
        private long expireTime;
        private int score;
        private boolean isPrivate;
        private String description;
        private String parentId;

        public Builder() {
        }

        public Builder filePath(String val) {
            filePath = val;
            return this;
        }

        public Builder isShare(boolean val) {
            isShare = val;
            return this;
        }

        public Builder allowDownCount(int val) {
            allowDownCount = val;
            return this;
        }

        public Builder expireTime(long val) {
            expireTime = val;
            return this;
        }

        public Builder score(int val) {
            score = val;
            return this;
        }

        public Builder isPrivate(boolean val) {
            isPrivate = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder parentId(String val) {
            parentId = val;
            return this;
        }

        public FileUploadFileParam build() {
            return new FileUploadFileParam(this);
        }
    }
}