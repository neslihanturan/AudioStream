package nes.com.audiostreamer.util.data;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.WikipediaPageSummary;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.model.gson.MwJsonPage;
import nes.com.audiostreamer.model.gson.RestfulJsonObject;
import nes.com.audiostreamer.server.callback.AudioStreamCompletedCallback;
import nes.com.audiostreamer.server.callback.CacheThreadStatus;
import nes.com.audiostreamer.server.callback.RandomAudioCallback;
import nes.com.audiostreamer.server.callback.RandomCategoryCallback;
import nes.com.audiostreamer.server.instance.CommonsRetrofitServiceCache;
import nes.com.audiostreamer.server.callback.RandomSummaryCallback;
import nes.com.audiostreamer.server.instance.RandomAudioServiceCache;
import nes.com.audiostreamer.server.instance.WikipediaRetrofitServiceCache;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by nesli on 26.10.2016.
 */

public class WmCommonsDataUtil {
    public static String LOG_TAG = WmCommonsDataUtil.class.getName();
    private static Call<MwJsonObject> commonsQueryResponse;
    private static Call<RestfulJsonObject> wikipediaQueryResponse;
    private static Call<ResponseBody> audioStreamQueryResponse;

    public static void getRandomAudio(final String categoryTitle, final RandomAudioCallback callback){
        Log.d(LOG_TAG,"get random audio method is running");
        commonsQueryResponse = CommonsRetrofitServiceCache.getService().getRandomAudio(categoryTitle);
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d(LOG_TAG,"get random audio method is on response");
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
                    Log.d(LOG_TAG,"get random audio method is finishing, RandomAudioCallback is:" + callback);
                    callback.onSuccessCommonsAudioData(audioFile, true);    //true means valid category
                    Log.d(LOG_TAG,"onSuccessCommonsAudioData has been thrown, method is finished");
                }
            }
            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                callback.onErrorCommonsAudioData();
            }
        });
    }

    public static void getRandomCategory(final RandomCategoryCallback callback){
        Log.d(LOG_TAG,"get random category method is starting");
        commonsQueryResponse = CommonsRetrofitServiceCache.getService().getRelevantCategories("");        //TODO: apply continuation
        commonsQueryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                Log.d(LOG_TAG,"get random category method is onResponse");
                ArrayList<String> categoryList = new ArrayList<>();
                for(String key: response.body().getQuery().getPages().keySet()){
                    Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                Log.d(LOG_TAG,"get random category method is finishing, RandomCategoryCallback is "+callback);
                callback.onSuccessCommonsCategoryData(categoryList);
                Log.d(LOG_TAG,"get random category method is finished, onSuccessCommonsCategoryData method has been thrown");
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
                        response.body().getThumbnail().getWidth(),      //TODO: sometimes there is no thumbnail and throws null point ex.
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

    public static void getAudioStreams(final AudioStreamCompletedCallback callback, String url, final Socket client, final File cacheRefference,final CacheThreadStatus delegate, final int key, final int bytePointer){
        Log.d(LOG_TAG,"get audio streams method is starting");
        audioStreamQueryResponse = RandomAudioServiceCache.getService().getAudioStreams(url);
        audioStreamQueryResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(LOG_TAG,"get audio streams method is onResponse");
                writeResponseBodyToSocket(response.body(), client, cacheRefference, delegate, key, bytePointer);
                Log.d(LOG_TAG,"get audio streams method is finishing, AudioStreamCompletedCallback is "+callback);
                callback.onSuccessAudioStream();
                Log.d(LOG_TAG,"get audio streams method is finished, onSuccessCommonsCategoryData method has been thrown");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onErrorAudioStream();
            }
        });
    };
    private static void writeResponseBodyToSocket(ResponseBody responseBody, Socket client, File cacheRefference, CacheThreadStatus delegate, int key, int bytePointer){
        //File outputDir = context.getCacheDir();
        Log.d(LOG_TAG,"writing response to socket and file");
        try {
            File file = File.createTempFile("prefix", "extension", cacheRefference);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            OutputStream fileOutputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = responseBody.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = responseBody.byteStream();
                outputStream = client.getOutputStream();
                fileOutputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileOutputStream.write(fileReader, 0, read);


                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();


                return;
            } catch (IOException e) {
                return;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
                Log.d(LOG_TAG,"response body is written, with CacheThreadStatus"+delegate);
                bytePointer += 4096;
                delegate.cacheThreadDone(key, bytePointer);
                Log.d(LOG_TAG,"cache thread done method has been thrown");
            }
        } catch (IOException e) {
            return;
        }
    }
    /*
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    */
}
