package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

public class FindingItem {
    private final User user;
    private final InternetFile internetFile;

    private FindingItem(Builder builder) {
        user = builder.user;
        internetFile = builder.internetFile;
    }


    public InternetFile getInternetFile() {
        return internetFile;
    }

    public User getUser() {
        return user;
    }

    public static final class Builder {
        private User user;
        private InternetFile internetFile;

        public Builder() {
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder internetFile(InternetFile val) {
            internetFile = val;
            return this;
        }

        public FindingItem build() {
            return new FindingItem(this);
        }
    }
}
