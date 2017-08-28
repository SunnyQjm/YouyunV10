package com.sunny.youyun.model;

/**
 * Created by Sunny on 2017/8/27 0027.
 */

public class QQLoginResult {
    public LoginInfo nameValuePairs;

    private static QQLoginResult empty = null;
    @Override
    public String toString() {
        return "QQLoginResult{" +
                "nameValuePairs=" + nameValuePairs +
                '}';
    }

    public static QQLoginResult empty(){
        if(empty == null)
            empty = new QQLoginResult();
        return empty;
    }

    public String getOpenid(){
        if(nameValuePairs == null)
            return "";
        return nameValuePairs.openid;
    }

    public String getAccessToken(){
        if(nameValuePairs == null)
            return "";
        return nameValuePairs.access_token;
    }

    public long getExpiresIn(){
        if(nameValuePairs == null)
            return 0;
        return nameValuePairs.expires_in;
    }



    public static class LoginInfo{
        public int ret;
        public String openid;
        public String access_token;
        public String pay_token;
        public long expires_in;
        public String pf;
        public String pfkey;
        public String msg;
        public int login_cost;
        public int query_authority_cost;
        public int authority_cost;

        private LoginInfo(Builder builder) {
            ret = builder.ret;
            openid = builder.openid;
            access_token = builder.access_token;
            pay_token = builder.pay_token;
            expires_in = builder.expires_in;
            pf = builder.pf;
            pfkey = builder.pfkey;
            msg = builder.msg;
            login_cost = builder.login_cost;
            query_authority_cost = builder.query_authority_cost;
            authority_cost = builder.authority_cost;
        }

        @Override
        public String toString() {
            return "LoginInfo{" +
                    "ret=" + ret +
                    ", openid='" + openid + '\'' +
                    ", access_token='" + access_token + '\'' +
                    ", pay_token='" + pay_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", pf='" + pf + '\'' +
                    ", pfkey='" + pfkey + '\'' +
                    ", msg='" + msg + '\'' +
                    ", login_cost=" + login_cost +
                    ", query_authority_cost=" + query_authority_cost +
                    ", authority_cost=" + authority_cost +
                    '}';
        }

        public static final class Builder {
            private int ret;
            private String openid;
            private String access_token;
            private String pay_token;
            private long expires_in;
            private String pf;
            private String pfkey;
            private String msg;
            private int login_cost;
            private int query_authority_cost;
            private int authority_cost;

            public Builder() {
            }

            public Builder ret(int val) {
                ret = val;
                return this;
            }

            public Builder openid(String val) {
                openid = val;
                return this;
            }

            public Builder access_token(String val) {
                access_token = val;
                return this;
            }

            public Builder pay_token(String val) {
                pay_token = val;
                return this;
            }

            public Builder expires_in(long val) {
                expires_in = val;
                return this;
            }

            public Builder pf(String val) {
                pf = val;
                return this;
            }

            public Builder pfkey(String val) {
                pfkey = val;
                return this;
            }

            public Builder msg(String val) {
                msg = val;
                return this;
            }

            public Builder login_cost(int val) {
                login_cost = val;
                return this;
            }

            public Builder query_authority_cost(int val) {
                query_authority_cost = val;
                return this;
            }

            public Builder authority_cost(int val) {
                authority_cost = val;
                return this;
            }

            public LoginInfo build() {
                return new LoginInfo(this);
            }
        }
    }
}
