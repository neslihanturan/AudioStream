package nes.com.audiostreamer.server.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by nesli on 05.11.2016.
 */

public interface RandomAudioAPIInterface {
    //Base url will be ignored for this case
    @GET                            //Urls are determined in runtime so used @Url anotation
    Call<ResponseBody> getAudioStreams(@Url String url);

}

