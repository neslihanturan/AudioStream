package nes.com.audiostreamer.main;

/**
 * Created by nesli on 07.10.2016.
 */

public class Constant {
    //TODO: carry strings to strings.xml

    public static String ACTION_STOP = "nes.com.audiostreamer.STOP";
    public static String ACTION_PLAY = "nes.com.audiostreamer.PLAY";
    public static String ACTION_CLOSE = "nes.com.audiostreamer.CLOSE";
    public static int NOTIFICATION_ID = 200;
    public static final String ACTION_COMMONS = "COMMONS";
    public static final String ACTION_WIKIPEDIA = "WIKIPEDIA";
    public static final String COMMONS_BASE_URL = "https://commons.wikimedia.org/";
    public static final String WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/";
    public static final String NETWORK_FAILURE_MESSAGE = "Can not connect to network";
    public static final String NETWORK_SUCCESS_MESSAGE = "Connected to network";
    public static final int CACHE_NOT_READY = 0;
    public static final int CACHE_PREPARING = 1;
    public static final int CACHE_READY = 7;
    public static final int CACHE_IS_USED = 3;
}
