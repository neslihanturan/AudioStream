package nes.com.audiostreamer.server;

import nes.com.audiostreamer.model.AudioFile;

/**
 * Created by nesli on 26.10.2016.
 */

public interface RandomAudioCallback {
        void onSuccess(AudioFile audioFile, boolean isCategoryNonEmpty);
        void onError();
}
