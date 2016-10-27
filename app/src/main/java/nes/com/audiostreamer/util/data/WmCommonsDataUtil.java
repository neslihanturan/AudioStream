package nes.com.audiostreamer.util.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.WikipediaPageSummary;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.model.gson.MwJsonPage;
import nes.com.audiostreamer.model.gson.RestfulJsonObject;
import nes.com.audiostreamer.server.callback.RandomAudioCallback;
import nes.com.audiostreamer.server.callback.RandomCategoryCallback;
import nes.com.audiostreamer.server.instance.CommonsRetrofitServiceCache;
import nes.com.audiostreamer.server.callback.RandomSummaryCallback;
import nes.com.audiostreamer.server.instance.WikipediaRetrofitServiceCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nesli on 26.10.2016.
 */

public class WmCommonsDataUtil {
    private static Call<MwJsonObject> commonsQueryResponse;
    private static Call<RestfulJsonObject> wikipediaQueryResponse;

    public static void getRandomAudio(final String categoryTitle, final RandomAudioCallback callback){
        commonsQueryResponse = CommonsRetrofitServiceCache.getService().getRandomAudio(categoryTitle);
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                if(response.body().getQuery()==null){
                    callback.onSuccessCommonsAudioData(null, false);        //false means category is empty
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
                    callback.onSuccessCommonsAudioData(audioFile, true);    //true means valid category
                }
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onErrorCommonsAudioData();
            }
        });
    }

    public static void getRandomCategory(final RandomCategoryCallback callback){
        commonsQueryResponse = CommonsRetrofitServiceCache.getService().getRelevantCategories("");        //TODO: apply continuation
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                ArrayList<String> categoryList = new ArrayList<>();
                for(String key: response.body().getQuery().getPages().keySet()){
                    Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                callback.onSuccessCommonsCategoryData(categoryList);
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onErrorCommonsCategoryData();
            }
        });
    }

    //TODO: create resfuldatacommons class and put this in it
    public static void getRandomSummary(final RandomSummaryCallback callback){
        wikipediaQueryResponse = WikipediaRetrofitServiceCache.getService().getRandomSummary();
        wikipediaQueryResponse.enqueue(new Callback<RestfulJsonObject>() {
            @Override
            public void onResponse(Call<RestfulJsonObject> call, Response<RestfulJsonObject> response) {
                WikipediaPageSummary pageSummary = new WikipediaPageSummary(
                        response.body().getThumbnail().getWidth(),
                        response.body().getThumbnail().getWidth(),
                        response.body().getThumbnail().getSource(),
                        response.body().getExtract(),
                        response.body().getTitle());
                Log.d("wikipedia response",response.body().getTitle() + " url"+response.body().getExtract());
                callback.onSuccessWikipediaData(pageSummary);
            }

            @Override
            public void onFailure(Call<RestfulJsonObject> call, Throwable t) {
                Log.d("wikipedia response","couldnt get data");
                callback.onErrorWikipediaData();
            }
        });
    }

}
