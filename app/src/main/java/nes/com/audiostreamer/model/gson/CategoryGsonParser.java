
package nes.com.audiostreamer.model.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nesli on 16.10.2016.
 */

public class CategoryGsonParser {
    private Query query;
    private String batchcomplete;

    public Query getQuery ()
    {
        return query;
    }

    public void setQuery (Query query)
    {
        this.query = query;
    }

    public String getBatchcomplete ()
    {
        return batchcomplete;
    }

    public void setBatchcomplete (String batchcomplete)
    {
        this.batchcomplete = batchcomplete;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [query = "+query+", batchcomplete = "+batchcomplete+"]";
    }
}