package nes.com.audiostreamer.server.service;

import nes.com.audiostreamer.model.gson.RestfulJsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by nesli on 27.10.2016.
 */

public interface ResfulAPIInterface {
    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("api/rest_v1/page/random/summary")
    Call<RestfulJsonObject> getRandomSummary();
}
