package com.sunny.youyun.wifidirect.utils;

/**
 * Created by Sunny on 2017/4/27 0027.
 */
public enum  ProtocolStringBuilder {
    INSTANCE;
    public static final String DIVIDE_UNIT = "&253&";
    private String info = "";

    public static ProtocolStringBuilder getInstance(){
        INSTANCE.info = "";
        return INSTANCE;
    }

    public ProtocolStringBuilder contract(String s){
        if(info.equals("")){
            info = s;
        }else{
            info += (DIVIDE_UNIT + s);
        }
        return this;
    }

    public ProtocolStringBuilder contract(int i){
        return contract(String.valueOf(i));
    }

    public static int getCode(String str){
        return Integer.parseInt(str.split(DIVIDE_UNIT)[0]);
    }

    public static String getBody(String str){
        return str.split(DIVIDE_UNIT)[1];
    }

    public static String[] getInfoArr(String str){
        return str.split(DIVIDE_UNIT);
    }
    @Override
    public String toString(){
        return info;
    }
}
