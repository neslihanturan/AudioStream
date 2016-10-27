package nes.com.audiostreamer.server.instance;

import nes.com.audiostreamer.main.Constant;
import nes.com.audiostreamer.server.RetrofitFactory;
import retrofit2.Retrofit;

/**
 * Created by nesli on 27.10.2016.
 */

public class WikipediaRetrofitInstanceCache {
    //TODO: seperate this to two class
    private static Retrofit INSTANCE = null;
    public static Retrofit getClient() {
        if (INSTANCE ==null) {
            INSTANCE = RetrofitFactory.newInstance(Constant.WIKIPEDIA_BASE_URL);
        }
        return INSTANCE;
    }

}
