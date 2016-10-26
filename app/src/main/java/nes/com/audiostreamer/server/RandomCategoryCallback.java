package nes.com.audiostreamer.server;

import java.util.ArrayList;

/**
 * Created by nesli on 26.10.2016.
 */

public interface RandomCategoryCallback {
    void onSuccess(ArrayList<String> categoryList);
    void onError();
}
