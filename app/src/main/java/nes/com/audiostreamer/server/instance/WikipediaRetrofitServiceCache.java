package nes.com.audiostreamer.server.instance;

import nes.com.audiostreamer.server.service.ResfulAPIInterface;

/**
 * Created by nesli on 27.10.2016.
 */

public class WikipediaRetrofitServiceCache {
    private static ResfulAPIInterface INSTANCE = null;

    public static ResfulAPIInterface getService() {
        if (INSTANCE ==null) {
            //INSTANCE = CommonsRetrofitInstanceCache.getClient().create(ResfulAPIInterface.class);
            INSTANCE = WikipediaRetrofitInstanceCache.getClient().create(ResfulAPIInterface.class);
        }
        return INSTANCE;
    }
}
