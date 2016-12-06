package nes.com.audiostreamer.server.callback;

/**
 * Created by nesli on 06.12.2016.
 */

public interface CacheThreadStatus {
    public void cacheThreadDone(int key, int bytePointer);
}
