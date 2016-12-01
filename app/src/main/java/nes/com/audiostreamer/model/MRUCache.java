package nes.com.audiostreamer.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by nesli on 28.11.2016.
 * code is get from https://blog.kodcu.com/2012/01/javada-lru-cache-uygulamasi/
 */

public class MRUCache<K,V> {
    private int cacheSize;
    private LinkedList<K> keyList;
    private HashMap<K, V> cacheTable;
    private String LOG_TAG = MRUCache.class.getName();

    public MRUCache(int cacheSize){
        this.cacheSize = cacheSize;
        keyList = new LinkedList<K>();
        cacheTable = new HashMap<K, V>(cacheSize);
    }
    public V getCachedItem(K key){
        V tempItem;
        tempItem = cacheTable.get(key);
        if(tempItem != null){
            keyList.remove(key);
            keyList.addLast(key);
            return tempItem;
        }
        return null;
    }
    private void removeLastItem(){

        if(keyList.getLast() != null){
            K key = keyList.getLast();
            keyList.removeLast();
            cacheTable.remove(key);
        }
    }
    public void insertToCache(K key, V value){

        if (cacheTable.size() >= cacheSize){
            removeLastItem();
        }
        if(!cacheTable.containsKey(key)){
            keyList.addLast(key);
            cacheTable.put(key, value);
        }
    }
    public void printAll(){

        Iterator<V> iterator = cacheTable.values().iterator();

        while(iterator.hasNext()){
            Log.d(LOG_TAG,iterator.next().toString());
        }
    }
}
