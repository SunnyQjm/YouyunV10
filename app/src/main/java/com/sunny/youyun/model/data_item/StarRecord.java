package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class StarRecord {

    /**
     * fileId : 1008
     * comment : 文件太大
     * userId : 1001
     * user : {"username":"大爷A","email":"email","sex":1,"phone":"18340857280","avatar":"http://210.30.100.189:8080/avatar/bccec38d-4bb0-468e-a35b-90ad21d1539cYouyunclip_image_tmp.png","signature":"ASDFASDSAFASFDSAASGDWARGEANVDKNWVNWEIUVNISENDSBNEHCBSEAUHBCEIHRNCEIADNCIWEMCIEROUFIWENHVIUSHFICUWHEIFUCWEHIFUWCEIFWHEIFUWEHIORUFCHEWURIHFICEUWRHFCUWIERHUWERO NVWHVWUFHOWERGHWVEUR"}
     * file : {"name":"VID_20170926_143738.mp4","size":654346232,"lookNum":7,"downloadCount":3,"identifyCode":"qUR43L","share":true,"privateOwn":false,"description":"","score":0,"MIME":"video","star":0}
     * id : 5
     * createTime : 1507385193940
     * updateTime : 1507385193940
     */

    private final int fileId;
    private final String comment;
    private final int userId;
    private final User user;
    private final InternetFile file;
    private final int id;
    private final long createTime;
    private final long updateTime;

    private StarRecord(Builder builder) {
        fileId = builder.fileId;
        comment = builder.comment;
        userId = builder.userId;
        user = builder.user;
        file = builder.file;
        id = builder.id;
        createTime = builder.createTime;
        updateTime = builder.updateTime;
    }

    public int getFileId() {
        return fileId;
    }

    public String getComment() {
        return comment;
    }

    public int getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public InternetFile getFile() {
        return file;
    }

    public int getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public static final class Builder {
        private int fileId;
        private String comment;
        private int userId;
        private User user;
        private InternetFile file;
        private int id;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder fileId(int val) {
            fileId = val;
            return this;
        }

        public Builder comment(String val) {
            comment = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder file(InternetFile val) {
            file = val;
            return this;
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder updateTime(long val) {
            updateTime = val;
            return this;
        }

        public StarRecord build() {
            return new StarRecord(this);
        }
    }
}
