package nes.com.audiostreamer.server;

import nes.com.audiostreamer.main.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstanceCache {

    private static Retrofit INSTANCE = null;

    public static Retrofit getClient() {
        if (INSTANCE ==null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}