package nes.com.audiostreamer.server.callback;

import nes.com.audiostreamer.model.WikipediaPageSummary;

/**
 * Created by nesli on 27.10.2016.
 */

public interface RandomSummaryCallback {
    void onSuccessWikipediaData(WikipediaPageSummary pageSummary);
    void onErrorWikipediaData();
}
