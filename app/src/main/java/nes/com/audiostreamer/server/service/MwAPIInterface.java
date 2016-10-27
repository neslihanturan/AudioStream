package nes.com.audiostreamer.server.service;


import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.model.gson.RestfulJsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


/**
 * Created by nesli on 22.10.2016.
 */

public interface MwAPIInterface {
    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("w/api.php?action=query&continue=gaccontinue||&generator=allcategories&gacprefix=Audio files&gacmin=1&gaclimit=500&&gacprop=size&format=json")
    Call<MwJsonObject> getRelevantCategories(@Query("gaccontinue") String _continue);


    @Headers({                              //TODO: Add interceptor with okhttp3 for pass header to all requests
            "User-Agent: AudioStreamerAndroidApp/tur.neslihan@gmail.com"
    })
    @GET("w/api.php?action=query&generator=categorymembers&gcmtype=file&prop=info|imageinfo&format=json&gcmlimit=100&iiprop=url|user|canonicaltitle|mime|mediatype")
    Call<MwJsonObject> getRandomAudio(@Query("gcmtitle") String categoryTitle);


}
