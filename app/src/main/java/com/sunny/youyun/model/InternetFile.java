package com.sunny.youyun.model;

import android.os.Build;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Sunny on 2017/6/23 0023.
 */

public class InternetFile extends DataSupport implements Serializable {
    private String identifyCode;
    private final int id;
    private final String name;
    private final long size;
    private final long expireTime;
    private final long createTime;
    private final boolean share;
    private final int score;
    private final int downloadCount;
    private final int userId;
    private final int lookNum;          //浏览量
    private final int star;             //点赞人数
    private final User user;
    private final String description;
    private final boolean isDirectory;

    private String status = Status.DOWNLOADING;
    private String path = "";
    private int progress = 0;
    private String rate;
    private int fileTAG = -1;
    private int position = 0;
    public static final int TAG_UPLOAD = 0;
    public static final int TAG_DOWNLOAD = 1;
    private final static int MAX_PROGRESS = 100;

    private static final InternetFile empty = new Builder().build();

    public static InternetFile empty(){
        return empty;
    }


    public static class Status{
        public static final String DOWNLOADING = "download";
        public static final String PAUSE = "pause";
        public static final String CANCEL = "cancel";
        public static final String ERROR = "error";
        public static final String FINISH = "finish";
    }

    @Override
    public String toString() {
        return "InternetFile{" +
                "identifyCode='" + identifyCode + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", expireTime=" + expireTime +
                ", createTime=" + createTime +
                ", share=" + share +
                ", score=" + score +
                ", downloadCount=" + downloadCount +
                ", userId=" + userId +
                ", lookNum=" + lookNum +
                ", star=" + star +
                ", status='" + status + '\'' +
                ", path='" + path + '\'' +
                ", progress=" + progress +
                ", rate='" + rate + '\'' +
                ", fileTAG=" + fileTAG +
                ", user=" + user +
                '}';
    }

    protected InternetFile(Builder builder) {
        setIdentifyCode(builder.identifyCode);
        id = builder.id;
        name = builder.name;
        size = builder.size;
        expireTime = builder.expireTime;
        createTime = builder.createTime;
        share = builder.share;
        score = builder.score;
        downloadCount = builder.downloadCount;
        userId = builder.userId;
        lookNum = builder.lookNum;
        star = builder.star;
        user = builder.user;
        description = builder.description;
        isDirectory = builder.isDiretory;
        setStatus(builder.status);
        setPath(builder.path);
        setProgress(builder.progress);
        setRate(builder.rate);
        setFileTAG(builder.fileTAG);
        setPosition(builder.position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public static int getMaxProgress() {
        return MAX_PROGRESS;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean isDone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(status, Status.FINISH);
        } else {
            return Status.FINISH.equals(status);
        }
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isShare() {
        return share;
    }

    public int getScore() {
        return score;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public int getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public int getLookNum() {
        return lookNum;
    }

    public int getStar() {
        return star;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFileTAG(int fileTAG) {
        this.fileTAG = fileTAG;
    }

    public String getDescription() {
        return description;
    }

    public static final class Builder {
        private String indentfyCode;
        private int id;
        private String name;
        private long size;
        private long expireTime;
        private long createTime;
        private boolean share;
        private int score;
        private int downloadCount;
        private int userId = -1;
        private int lookNum;
        private int star;
        private User user;
        private String description;
        private boolean isDiretory;
        private String status;
        private boolean done;
        private String path;
        private int progress;
        private String rate;
        private int fileTAG;
        private int position;
        private String identifyCode;

        public Builder() {
        }

        public Builder indentfyCode(String val) {
            indentfyCode = val;
            return this;
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }


        public Builder expireTime(long val) {
            expireTime = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder share(boolean val) {
            share = val;
            return this;
        }

        public Builder score(int val) {
            score = val;
            return this;
        }

        public Builder downloadCount(int val) {
            downloadCount = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder lookNum(int val) {
            lookNum = val;
            return this;
        }

        public Builder star(int val) {
            star = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder isDiretory(boolean val) {
            isDiretory = val;
            return this;
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder done(boolean val) {
            done = val;
            return this;
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder progress(int val) {
            progress = val;
            return this;
        }

        public Builder rate(String val) {
            rate = val;
            return this;
        }

        public Builder fileTAG(int val) {
            fileTAG = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public InternetFile build() {
            return new InternetFile(this);
        }

        public Builder identifyCode(String val) {
            identifyCode = val;
            return this;
        }
    }
}
