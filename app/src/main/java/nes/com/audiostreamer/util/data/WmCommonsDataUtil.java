package nes.com.audiostreamer.util.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.model.gson.MwJsonPage;
import nes.com.audiostreamer.server.RandomAudioCallback;
import nes.com.audiostreamer.server.RandomCategoryCallback;
import nes.com.audiostreamer.server.RetrofitServiceCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nesli on 26.10.2016.
 */

public class WmCommonsDataUtil {
    private static Call<MwJsonObject> queryResponse ;

    public static void getRandomAudio(final String categoryTitle, final RandomAudioCallback callback){
        queryResponse = RetrofitServiceCache.getService().getRandomAudio(categoryTitle);
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                if(response.body().getQuery()==null){
                    callback.onSuccess(null, false);        //false means category is empty
                }
                else{
                    AudioFile audioFile = new AudioFile();
                    Random generator = new Random();
                    Object[] values = response.body().getQuery().getPages().values().toArray();
                    Object randomValue = values[generator.nextInt(values.length)];
                    Log.d("i",((MwJsonPage)randomValue).getImageinfo()[0].toString());
                    audioFile.setUrl(((MwJsonPage)randomValue).getImageinfo()[0].getUrl());
                    audioFile.setTitle(((MwJsonPage)randomValue).getImageinfo()[0].getCanonicaltitle());
                    audioFile.setCategory(categoryTitle);
                    callback.onSuccess(audioFile, true);    //true means valid category
                }

            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onError();
            }
        });
    }

    public static void getRandomCategory(final RandomCategoryCallback callback){
        queryResponse = RetrofitServiceCache.getService().getRelevantCategories("");
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                ArrayList<String> categoryList = new ArrayList<>();
                for(String key: response.body().getQuery().getPages().keySet()){
                    Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                callback.onSuccess(categoryList);
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onError();
            }
        });
    }

}
