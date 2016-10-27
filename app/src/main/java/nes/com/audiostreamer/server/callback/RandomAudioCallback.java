package nes.com.audiostreamer.server.callback;

import nes.com.audiostreamer.model.AudioFile;

/**
 * Created by nesli on 26.10.2016.
 */

public interface RandomAudioCallback {
        void onSuccessCommonsAudioData(AudioFile audioFile, boolean isCategoryNonEmpty);
        void onErrorCommonsAudioData();
}
