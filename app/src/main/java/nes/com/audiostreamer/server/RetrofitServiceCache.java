package nes.com.audiostreamer.server;

public class RetrofitServiceCache {

    private static MwAPIInterface INSTANCE = null;

    public static MwAPIInterface getService() {
        if (INSTANCE ==null) {
            INSTANCE = RetrofitInstanceCache.getClient().create(MwAPIInterface.class);
        }
        return INSTANCE;
    }
}