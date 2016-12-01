package nes.com.audiostreamer.server.callback;

/**
 * Created by nesli on 05.11.2016.
 */

public interface AudioStreamCompletedCallback {
    void onSuccessAudioStream();
    void onErrorAudioStream();
}
