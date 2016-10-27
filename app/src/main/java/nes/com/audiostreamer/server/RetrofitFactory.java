package nes.com.audiostreamer.server;

import android.support.annotation.NonNull;

import nes.com.audiostreamer.main.Constant;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nesli on 27.10.2016.
 */

public class RetrofitFactory {
    public static Retrofit newInstance(@NonNull String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
