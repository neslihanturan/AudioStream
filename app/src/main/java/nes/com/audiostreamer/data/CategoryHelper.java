package nes.com.audiostreamer.data;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import nes.com.audiostreamer.model.gson.CategoryGsonParser;


/**
 * Created by nesli on 15.10.2016.
 */

public class CategoryHelper extends AsyncTask{
    private final String QUERY = "action=query&list=prefixsearch&pssearch=Category:Audio files&pslimit=500&format=json&psoffset=1000";

    @Override
    protected Object doInBackground(Object[] urls) {
        try {
            return downloadUrl((String) urls[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "Coudn't get data";
        }
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("i",((String) o));
        String result = (String) o;
        Gson gson = new Gson();
        CategoryGsonParser gsonObj = gson.fromJson(result, CategoryGsonParser.class);
        Log.d("i","gson "+ gsonObj.getBatchcomplete());
        Log.d("i","gson "+gsonObj.getQuery().getPrefixsearch()[0]);
        for(int i = 0; i < gsonObj.getQuery().getPrefixsearch().length-1; i++){
            Log.d("i","gson "+gsonObj.getQuery().getPrefixsearch()[i]);
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());   //send query as output
            writeStream(out);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());   //get json response as string
            return readStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readStream(InputStream inputStream) throws IOException, UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        inputStream.close();
        return total.toString();
    }

    // Writes Query to OutputStream
    public void writeStream(OutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(constructQuery(QUERY));
        writer.flush();
        writer.close();
        outputStream.close();
    }
    public String constructQuery(String QUERY){

        return QUERY;
    }
}
