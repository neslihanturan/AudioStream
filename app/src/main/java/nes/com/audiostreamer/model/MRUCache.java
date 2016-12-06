package nes.com.audiostreamer.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import nes.com.audiostreamer.server.callback.CacheEmptySpaceCallback;

/**
 * Created by nesli on 28.11.2016.
 * code is get from https://blog.kodcu.com/2012/01/javada-lru-cache-uygulamasi/
 */

public class MRUCache<K,V> {
    private int cacheSize;
    private LinkedList<K> keyList;
    private HashMap<K, V> cacheTable;
    private String LOG_TAG = MRUCache.class.getName();
    public CacheEmptySpaceCallback delegate;

    public MRUCache(int cacheSize, CacheEmptySpaceCallback delegate ){
        this.cacheSize = cacheSize;
        keyList = new LinkedList<K>();
        this.delegate = delegate;
        cacheTable = new HashMap<K, V>(cacheSize);
        Log.d(LOG_TAG, "cached is created");
    }
    public V getCachedItem(K key){
        Log.d(LOG_TAG, "cached item is getting");
        V tempItem;
        tempItem = cacheTable.get(key);
        if(tempItem != null){
            keyList.remove(key);
            keyList.addLast(key);
            Log.d(LOG_TAG, "process completed");
            return tempItem;
        }
        Log.d(LOG_TAG, "temp item was null");
        return null;
    }
    private void removeLastItem(){
        Log.d(LOG_TAG, "last item is removing");
        if(keyList.getLast() != null){
            K key = keyList.getLast();
            keyList.removeLast();
            cacheTable.remove(key);
            delegate.emptySpaceWarning();
        }
    }
    public K getLastKey(){
        Log.d(LOG_TAG, "last key is getting");
        try{
            if(keyList.getLast() != null){
                return keyList.getLast();
            }else{
                return null;
            }
        }catch (NoSuchElementException e){
            return null;
        }

    }
    public void changeKey(K oldKey,K newKey, V value){
        cacheTable.remove(oldKey);
        cacheTable.put(newKey, value);
    }
    public K insertToCache(K key, V value){
        Log.d(LOG_TAG, "Inserting into cache ");
        if (cacheTable.size() >= cacheSize){
            removeLastItem();
        }
        if(!cacheTable.containsKey(key)){
            keyList.addLast(key);
            cacheTable.put(key, value);
        }
        return key;
    }
    public void printAll(){
        Iterator<V> iterator = cacheTable.values().iterator();
        Iterator<K> iterator2 = cacheTable.keySet().iterator();

        while(iterator.hasNext() && iterator2.hasNext()){
            Log.d(LOG_TAG,"printing value "+iterator.next().toString());
            Log.d(LOG_TAG,"printing key "+iterator2.next().toString());
        }
    }
}
