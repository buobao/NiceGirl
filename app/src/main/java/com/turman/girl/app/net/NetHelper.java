package com.turman.girl.app.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dqf on 2016/5/6.
 */
public class NetHelper {

    public static final String URL_BASE = "http://www.tngou.net/tnfs/api/";
    public static final String IMG_BASE_URL = "http://tnfs.tngou.net/img";
    //Cat API http://thecatapi.com/

    private static Retrofit client = null;

    public static final String LIST = "list";
    public static final String NEWS = "news";
    public static final String TYPE = "classify";
    public static final String DETAIL = "show";

    private static ImgService imgService;

    private NetHelper(){}

    public static Retrofit getClient(){
        OkHttpClient okHttpClient = new OkHttpClient();

        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return client;
    }

    public static ImgService getImgService(){
        if (imgService == null) {
            imgService = getClient().create(ImgService.class);
        }

        return imgService;
    }

}
