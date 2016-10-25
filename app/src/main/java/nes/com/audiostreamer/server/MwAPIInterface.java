package nes.com.audiostreamer.server;


import nes.com.audiostreamer.model.gson.MwJsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by nesli on 22.10.2016.
 */

public interface MwAPIInterface {

    @GET("w/api.php?action=query&continue=gaccontinue||&generator=allcategories&gacprefix=Audio files&gacmin=1&gaclimit=500&&gacprop=size&format=json")
    Call<MwJsonObject> getRelevantCategories(@Query("gaccontinue") String _continue);

    @GET("w/api.php?action=query&generator=categorymembers&gcmtype=file&prop=info|imageinfo&format=json&gcmlimit=100&iiprop=url|user|canonicaltitle|mime|mediatype")
    Call<MwJsonObject> getRandomAudio(@Query("gcmtitle") String categoryTitle);
}
