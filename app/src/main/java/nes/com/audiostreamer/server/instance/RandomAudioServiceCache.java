package nes.com.audiostreamer.server.instance;

import nes.com.audiostreamer.server.service.RandomAudioAPIInterface;

/**
 * Created by nesli on 05.11.2016.
 */

public class RandomAudioServiceCache {

    private static RandomAudioAPIInterface INSTANCE = null;

    public static RandomAudioAPIInterface getService() {
        if (INSTANCE ==null) {
            INSTANCE = RandomAudioRetrofitInstanceCache.getClient().create(RandomAudioAPIInterface.class);
        }
        return INSTANCE;
    }
}
