package nes.com.audiostreamer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.MRUCache;
import nes.com.audiostreamer.model.PartOfAudio;
import nes.com.audiostreamer.server.callback.CacheEmptySpaceCallback;
import nes.com.audiostreamer.server.callback.RandomAudioCallback;
import nes.com.audiostreamer.server.proxy.StreamProxy;
import nes.com.audiostreamer.util.MediaPlayerController;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;

/**
 * Created by nesli on 26.11.2016.
 */

public class CacheOrganizerService extends Service implements RandomAudioCallback, CacheEmptySpaceCallback {
    private static final String LOG_TAG = CacheOrganizerService.class.getName();
    public static PartOfAudio[] partOfAudios = new PartOfAudio[5];
    public static ArrayList<String> categoryList;
    private boolean isCacheFull = false;
    public static MRUCache<Integer,PartOfAudio> partOfAudioMRUCache;
    private final int CACHE_SIZE = 5;
    StreamProxy streamProxy;
    //public static AudioFile[] audioFiles = new AudioFile[5];


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(LOG_TAG, "started");
        categoryList = getCategoryList(intent);
        streamProxy = new StreamProxy(this);
        initCache();
        partOfAudioMRUCache.printAll();
        /*for(int i = 0; i<audioFiles.length;i++){
            getSonUrls();
        }*/

        //ckeckNextEmpty();
        return START_STICKY;
    }

    public void initCache(){
        partOfAudioMRUCache = new MRUCache<Integer, PartOfAudio>(CACHE_SIZE);
        for(int i = 0; i<CACHE_SIZE; i++){
            WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this); //audio copmleted callback will be called
        }
    }

    public PartOfAudio getPartsOfAudios(AudioFile audioFile){
        return new PartOfAudio(audioFile.category,audioFile.title, audioFile.url,
                                                0,0,0,0);
    }

    public void getSonUrls(){
        WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this);
    }

    public String getRandomCategory(){
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(categoryList.size());
        return categoryList.get(index);
    }

    public ArrayList<String> getCategoryList(Intent intent){
        return intent.getStringArrayListExtra("category_list");
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSuccessCommonsAudioData(AudioFile audioFile, boolean isCategoryNonEmpty) {
        if(!isCategoryNonEmpty){
            WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this);     //if category is empty, find new category
        }else{
            PartOfAudio partOfAudio = new PartOfAudio(audioFile.category, audioFile.title, audioFile.url,0,0,0,0);
            partOfAudioMRUCache.insertToCache(1, partOfAudio);
            Log.d(LOG_TAG,partOfAudio.getTitle());
            streamProxy.start(audioFile.url);
        }
    }

    @Override
    public void onErrorCommonsAudioData() {

    }

    @Override
    public void emptySpaceWarning() {

    }
}
