package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.User;

/**
 * 广场Item
 * Created by Sunny on 2017/9/25 0025.
 */

public class FindingModel {

    /**
     * name : a.jpg
     * storeName : eb5a6dec-9b5e-416e-9135-5a228feacc66
     * size : 11
     * lookNum : 0
     * downloadCount : 0
     * identifyCode : 0H7rS9
     * userId : 1004
     * share : true
     * privateOwn : false
     * description : 来自优云的分享～
     * score : 0
     * md5 : 1231
     * MIME : img
     * star : 0
     * user : {"username":"username4","email":"email4","sex":0,"phone":"18340857285","avatar":"http://q.qlogo.cn/qqapp/1105716704/9F0208D3381DA5DCBAD56380900972B2/100","loginToken":"720fc4ba-11c2-41c7-a7e9-93009ca55fc2","id":1004,"createTime":1506257403191,"updateTime":1506309446000}
     * canStar : true
     * id : 1021
     * createTime : 1506308955779
     * updateTime : 1506308955779
     */

    private String name;
    private String storeName;
    private int size;
    private int lookNum;
    private int downloadCount;
    private String identifyCode;
    private int userId;
    private boolean share;
    private boolean privateOwn;
    private String description;
    private int score;
    private String md5;
    private String MIME;
    private int star;
    private User user;
    private boolean canStar;
    private int id;
    private long createTime;
    private long updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isPrivateOwn() {
        return privateOwn;
    }

    public void setPrivateOwn(boolean privateOwn) {
        this.privateOwn = privateOwn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMIME() {
        return MIME;
    }

    public void setMIME(String MIME) {
        this.MIME = MIME;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public boolean isCanStar() {
        return canStar;
    }

    public void setCanStar(boolean canStar) {
        this.canStar = canStar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
