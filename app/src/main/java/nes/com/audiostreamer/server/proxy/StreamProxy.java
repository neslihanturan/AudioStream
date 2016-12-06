package nes.com.audiostreamer.server.proxy;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import nes.com.audiostreamer.MediaPlayerCallback;
import nes.com.audiostreamer.server.callback.AudioStreamCompletedCallback;
import nes.com.audiostreamer.server.callback.CacheThreadStatus;
import nes.com.audiostreamer.server.callback.ProxyStreamReadyCallback;
import nes.com.audiostreamer.server.instance.RandomAudioServiceCache;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;

public class StreamProxy implements Runnable, AudioStreamCompletedCallback{
    public ProxyStreamReadyCallback delegate = null;
    public File cahceRefference;
    private static final String LOG_TAG = StreamProxy.class.getName();

    public int getPort() {
        return port;
    }

    private int port = 9090;
    //public final static String LOCAL_ADDRESS = "127.0.0.1";
    public final static String LOCAL_ADDRESS = "10.0.2.2";
    private ServerSocket socket;
    private Thread thread;
    private boolean isRunning;
    String filePath = "";
    String remoteUrl = "";
    int bytePointer;
    CacheThreadStatus delegate2;
    int key;

    public StreamProxy(Context context){
        try {
            socket = new ServerSocket(port, 0, InetAddress.getByName(null));
            socket.setSoTimeout(5000);
            port = socket.getLocalPort();
            Log.d(LOG_TAG, "Stream Proxy object created, port " + port + " obtained");
            cahceRefference = context.getCacheDir();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Context context,String filePath, String remoteUrl,CacheThreadStatus cacheThreadStatus,int key,int bytePointer, ProxyStreamReadyCallback delegate ) {
        this.filePath = filePath;
        this.remoteUrl = remoteUrl;
        this.bytePointer = bytePointer;
        this.delegate2 = cacheThreadStatus;
        this.delegate = delegate;
        this.cahceRefference = context.getCacheDir();
        this.key = key;
        if (socket == null) {
            Log.d(LOG_TAG,"Cannot start proxy; it has not been initialized.");
            throw new IllegalStateException("Cannot start proxy; it has not been initialized.");
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Stream Proxy thread is started with \n" +
                "remote url: "+ remoteUrl+"\n"+
                "cahceRefference: "+cahceRefference+"\n"+
                "key: "+key+"\n"+
                "bytePointer: "+bytePointer);
        isRunning = true;

        while (isRunning) {
            Log.d(LOG_TAG, "running");
            try {
                Log.d(LOG_TAG, "in try");
                Socket client = socket.accept();
                delegate.onProxyStreamReady();
                Log.d(LOG_TAG, "socket is accepted, onProxtStreamReady method delegated");
                if (client == null) {
                    Log.d(LOG_TAG, "client is null");
                    continue;
                }
                Log.d(LOG_TAG, "client connected, get audio streams method is calling");

                //WmCommonsDataUtil.getAudioStreams(this, remoteUrl, client, cahceRefference, delegate2, key , bytePointer);

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "socket timeout Exception");
                // Do nothing
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to client", e);
                Log.d(LOG_TAG, "IO Exception");
            }
        }
        Log.d(LOG_TAG, "Proxy interrupted. Shutting down.");
        Log.d(LOG_TAG, "Proxy interrupted. Shutting down.");
    }

    @Override
    public void onSuccessAudioStream() {
        Log.d(LOG_TAG,"data written to socket");
    }

    @Override
    public void onErrorAudioStream() {

    }
}