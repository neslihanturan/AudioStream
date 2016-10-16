package nes.com.audiostreamer.model.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nesli on 16.10.2016.
 */

public class Query {
    private Prefixsearch[] prefixsearch;

    public Prefixsearch[] getPrefixsearch ()
    {
        return prefixsearch;
    }

    public void setPrefixsearch (Prefixsearch[] prefixsearch)
    {
        this.prefixsearch = prefixsearch;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [prefixsearch = "+prefixsearch+"]";
    }
}
