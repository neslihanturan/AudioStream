package nes.com.audiostreamer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import nes.com.audiostreamer.main.Constant;
import nes.com.audiostreamer.main.MainActivity;
import nes.com.audiostreamer.model.AudioFile;
import nes.com.audiostreamer.model.MRUCache;
import nes.com.audiostreamer.model.PartOfAudio;
import nes.com.audiostreamer.server.callback.CacheEmptySpaceCallback;
import nes.com.audiostreamer.server.callback.CacheThreadStatus;
import nes.com.audiostreamer.server.callback.ProxyStreamReadyCallback;
import nes.com.audiostreamer.server.callback.RandomAudioCallback;
import nes.com.audiostreamer.server.proxy.StreamProxy;
import nes.com.audiostreamer.util.MediaPlayerController;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;

/**
 * Created by nesli on 26.11.2016.
 */

public class CacheOrganizerService extends Service implements RandomAudioCallback, CacheEmptySpaceCallback, CacheThreadStatus, ProxyStreamReadyCallback {
    private static final String LOG_TAG = CacheOrganizerService.class.getName();
    public static PartOfAudio[] partOfAudios = new PartOfAudio[5];
    public static ArrayList<String> categoryList;
    private boolean isCacheFull = false;
    public static MRUCache<Integer,PartOfAudio> partOfAudioMRUCache;
    private final int CACHE_SIZE = 1;
    public static StreamProxy streamProxy;
    //public static AudioFile[] audioFiles = new AudioFile[5];


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(LOG_TAG, "cache organizer service is started");
        categoryList = getCategoryList(intent);
        Log.d(LOG_TAG, "caterory list is gotten");
        streamProxy = new StreamProxy(this);
        initCache();
        Log.d(LOG_TAG, "cache is initiliasing");
        partOfAudioMRUCache.printAll();
        /*for(int i = 0; i<audioFiles.length;i++){
            getSonUrls();
        }*/

        //ckeckNextEmpty();
        return START_STICKY;
    }

    public void initCache(){
        partOfAudioMRUCache = new MRUCache<Integer, PartOfAudio>(CACHE_SIZE, this);
        for(int i = 0; i<CACHE_SIZE; i++){
            WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this); //audio copmleted callback will be called
            Log.d(LOG_TAG, "get random audio method is called for "+i+". time");
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
        Log.d(LOG_TAG, "get random category method is called");
        return categoryList.get(index);
    }

    public ArrayList<String> getCategoryList(Intent intent){
        Log.d(LOG_TAG, "get category list method is called");
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
            Log.d(LOG_TAG, "category was empty we will find new category");
        }else{
            PartOfAudio partOfAudio = new PartOfAudio(audioFile.category, audioFile.title, audioFile.url,0,0,0,0);
            int key;
            if(partOfAudioMRUCache.getLastKey()!=null){
                key = partOfAudioMRUCache.getLastKey()+1;
            }else{
                key = 0;
            }
            partOfAudioMRUCache.insertToCache(key, partOfAudio);
            Log.d(LOG_TAG, "starting stream proxy with song "+partOfAudio.getTitle()+" and key "+key);
            streamProxy.start(this,"file"+audioFile.url, audioFile.url,this, key, 0/*byte pointer*/, this );
        }
    }

    @Override
    public void onErrorCommonsAudioData() {

    }

    @Override
    public void emptySpaceWarning() {
        Log.d(LOG_TAG,"to do empty space warning");
        WmCommonsDataUtil.getRandomAudio(getRandomCategory(),this); //audio copmleted callback will be called
    }

    @Override
    public void cacheThreadDone(int key, int bytePointer) {
        Log.d(LOG_TAG,"to do cache thread done warning");
        partOfAudioMRUCache.getCachedItem(key).setEndByte(bytePointer);
        partOfAudioMRUCache.changeKey(key , Constant.CACHE_READY, partOfAudioMRUCache.getCachedItem(key) );
    }

    @Override
    public void onProxyStreamReady() {
        try {
            Log.d(LOG_TAG,"in on proxy stream ready");
            CacheOrganizerService.partOfAudioMRUCache.printAll();
            File cahceRefference = this.getCacheDir();
            File file = File.createTempFile("prefix", "extension", cahceRefference);
            FileInputStream inputStream = new FileInputStream(file);
            //MediaPlayerController.changeSong(inputStream.getFD());
            //MediaPlayerController.play(inputStream.getFD());
            //if(MainActivity.isPlaying){
                MediaPlayerController.changeSong(String.format("http://127.0.0.1:%d/%s",
                        streamProxy.getPort(), partOfAudioMRUCache.getCachedItem(0).getUrl()));
                MediaPlayerController.play(String.format("http://127.0.0.1:%d/%s",
                        streamProxy.getPort(), partOfAudioMRUCache.getCachedItem(0).getUrl()));
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
