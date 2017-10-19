package com.sunny.youyun.internet.api;


import android.content.Context;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.cookie_persisten.CookieJarImpl;
import com.sunny.youyun.internet.cookie_persisten.PersistentCookieStore;
import com.sunny.youyun.internet.exception.LoginTokenInvalidException;
import com.sunny.youyun.internet.exception.NotLoginException;
import com.sunny.youyun.internet.service.ChatServices;
import com.sunny.youyun.internet.service.FileServices;
import com.sunny.youyun.internet.service.ForumServices;
import com.sunny.youyun.internet.service.TokenServices;
import com.sunny.youyun.internet.service.UserServices;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.model.response_body.LoginTokenResponseBody;
import com.sunny.youyun.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class APIManager {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static APIManager getInstance() {
        return SingleTon.mInstance;
    }

    private static class SingleTon {
        static APIManager mInstance = new APIManager();
    }

    private static int cacheSize = 20 * 1024 * 1024;
    //    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static final int DEFAULT_TIMEOUT = 20;
    private static OkHttpClient client;
    private UserServices userService;
    private FileServices fileServices;
    private TokenServices tokenServices;
    private ForumServices forumServices;
    private ChatServices chatServices;


    /**
     * 初始化，在Application类里面的onCreate方法当中初始化
     *
     * @param applicationContext
     */
    public static void init(Context applicationContext) {
        client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new youyunRequestInterceptor())
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(applicationContext)))
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }


    public UserServices getUserService(Converter.Factory factory) {
        if (userService == null) {
            userService = createService(UserServices.class, factory);
        }
        return userService;
    }


    public FileServices getFileServices(Converter.Factory... factory) {
        if (fileServices == null)
            fileServices = createService(FileServices.class, factory);
        return fileServices;
    }

    public TokenServices getTokenServices(Converter.Factory... factory) {
        if (tokenServices == null)
            tokenServices = createService(TokenServices.class, factory);
        return tokenServices;
    }

    public ForumServices getForumServices(Converter.Factory... factories) {
        if (forumServices == null)
            forumServices = createService(ForumServices.class, factories);
        return forumServices;
    }

    public ChatServices getChatServices(Converter.Factory... factories) {
        if (chatServices == null)
            chatServices = createService(ChatServices.class, factories);
        return chatServices;
    }

    private <T> T createService(Class<T> serviceClass, Converter.Factory... factory) {
        Retrofit.Builder build = new Retrofit.Builder()
                .baseUrl(ApiInfo.BaseUrl)
                .client(client);
        for (Converter.Factory aFactory : factory) {
            build.addConverterFactory(aFactory);
        }
        Retrofit retrofit = build
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    private static class youyunRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //begin deal request before request

            Response originResponse = chain.proceed(request);
            //deal response after receive response
            ResponseBody responseBody = originResponse.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String responseString = buffer.clone().readString(charset);
            Logger.i(responseString);
            Logger.i(request.url().toString());
            BaseResponseBody result = GsonUtil.json2Bean(responseString, BaseResponseBody.class);
            Logger.i(result.toString());
            if (result.getCode() == ApiInfo.STATUS_CODE_NOT_LOGIN) {
                System.out.println("notLogin: " + YouyunAPI.getLoginToken());
            }
            //如果现在保存的登录状态是已登录，并且报未登录错误，则说明是cookie失效了，此时用
            // LoginToken重新请求
            if (result.getCode() == ApiInfo.STATUS_CODE_NOT_LOGIN &&
                    YouyunAPI.isIsLogin() && YouyunAPI.getLoginToken() != null &&
                    !YouyunAPI.getLoginToken().equals("")) {
                System.out.println("update Token");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(ApiInfo.UPDATE_COOKIE_BY_TOKEN_TOKEN, YouyunAPI.getLoginToken());
                    Logger.i("update token: " + YouyunAPI.getLoginToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                        , jsonObject.toString());
                TokenServices tokenServices = APIManager.getInstance()
                        .getTokenServices(GsonConverterFactory.create());
                Call<LoginTokenResponseBody> call = tokenServices
                        .updateCookieByToken(body);
                LoginTokenResponseBody loginTokenResponseBody = call.execute().body();

                //如果LoginToken还有效，就可以请求到用户的信息以及新token
                if (loginTokenResponseBody.isSuccess()) {
                    //尝试用LoginToken更新都没用，还返回-1，则表示并没有登录
                    if(loginTokenResponseBody.getCode() == ApiInfo.STATUS_CODE_NOT_LOGIN){
                        throw new NotLoginException("Not Login!");
                    }
                    if (loginTokenResponseBody.getData() != null) {
                        YouyunAPI.updateIsLogin(true);
                        YouyunAPI.updateLoginToken(loginTokenResponseBody.getData().getLoginToken());
                        Request newRequest = request.newBuilder()
                                .build();
                        //重新发起之前的请求
                        if (loginTokenResponseBody.isSuccess() && loginTokenResponseBody.getStatus() == 0) {
                            originResponse.close();
                            return chain.proceed(newRequest);
                        }
                    }
                }

            }

            //如果LoginToken失效了，就抛出异常
            if (result.getCode() == ApiInfo.STATUS_CODE_LOGIN_INVALID &&
                    YouyunAPI.isIsLogin()) {
                System.out.println("抛出LoginToken失效异常");
                throw new LoginTokenInvalidException("LoginToken Invalid!!");
            }
//            //对下载文件的Response拦截处理
//            if (request.url().toString().startsWith(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD)) {
//                System.out.println("返回带下载进度监听的responseBody");
//                return response.newBuilder()
//                        .body(new ProgressResponseBody(response.body()))
//                        .build();
//            }
            return originResponse;
        }
    }
}
