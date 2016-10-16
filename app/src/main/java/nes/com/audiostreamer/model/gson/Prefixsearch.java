
package nes.com.audiostreamer.model.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nesli on 16.10.2016.
 */

public class Prefixsearch {
    private String title;

    private String ns;

    private String pageid;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getNs ()
    {
        return ns;
    }

    public void setNs (String ns)
    {
        this.ns = ns;
    }

    public String getPageid ()
    {
        return pageid;
    }

    public void setPageid (String pageid)
    {
        this.pageid = pageid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", ns = "+ns+", pageid = "+pageid+"]";
    }
}
