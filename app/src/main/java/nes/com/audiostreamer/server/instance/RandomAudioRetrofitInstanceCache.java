package nes.com.audiostreamer.server.instance;

import nes.com.audiostreamer.main.Constant;
import nes.com.audiostreamer.server.RetrofitFactory;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by nesli on 05.11.2016.
 */

//TODO: put All instances into one class

public class RandomAudioRetrofitInstanceCache {
    //TODO: seperate this to two class
    private static Retrofit INSTANCE = null;
    public static Retrofit getClient() {
        if (INSTANCE ==null) {
            Dispatcher dispatcher=new Dispatcher();
            dispatcher.setMaxRequests(3);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.dispatcher(dispatcher);
            OkHttpClient client = builder.build();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(Constant.COMMONS_BASE_URL)     //this is just a placeholder
                    .client(client)
                    .build();
            INSTANCE = retrofit;

        }
        return INSTANCE;
    }
}
