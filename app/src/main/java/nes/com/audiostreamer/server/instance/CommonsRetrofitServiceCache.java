package nes.com.audiostreamer.server.instance;

import nes.com.audiostreamer.server.service.MwAPIInterface;

public class CommonsRetrofitServiceCache {

    private static MwAPIInterface INSTANCE = null;

    public static MwAPIInterface getService() {
        if (INSTANCE ==null) {
            INSTANCE = CommonsRetrofitInstanceCache.getClient().create(MwAPIInterface.class);
        }
        return INSTANCE;
    }
}