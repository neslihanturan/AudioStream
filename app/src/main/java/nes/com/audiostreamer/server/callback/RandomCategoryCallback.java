package nes.com.audiostreamer.server.callback;

import java.util.ArrayList;

/**
 * Created by nesli on 26.10.2016.
 */

public interface RandomCategoryCallback {
    void onSuccessCommonsCategoryData(ArrayList<String> categoryList);
    void onErrorCommonsCategoryData();
}
