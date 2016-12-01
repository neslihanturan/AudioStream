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
import nes.com.audiostreamer.server.callback.ProxyStreamReadyCallback;
import nes.com.audiostreamer.server.instance.RandomAudioServiceCache;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;

public class StreamProxy implements Runnable, AudioStreamCompletedCallback{
    public static ProxyStreamReadyCallback delegate = null;
    public static File cahceRefference;
    private static final String LOG_TAG = StreamProxy.class.getName();

    public int getPort() {
        return port;
    }

    private int port = 0;
    public final static String LOCAL_ADDRESS = "127.0.0.1";
    private ServerSocket socket;
    private Thread thread;
    private boolean isRunning;
    String filePath = "";
    String remoteUrl = "";

    public StreamProxy(Context context){
        try {
            socket = new ServerSocket(port, 0, InetAddress.getByName(LOCAL_ADDRESS));
            socket.setSoTimeout(5000);
            port = socket.getLocalPort();
            Log.d(LOG_TAG, "port " + port + " obtained");
            cahceRefference = context.getCacheDir();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String filePath) {
        this.filePath = filePath;
        this.remoteUrl = remoteUrl;
        if (socket == null) {
            throw new IllegalStateException("Cannot start proxy; it has not been initialized.");
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        isRunning = true;
        Log.d(LOG_TAG, "running");
        while (isRunning) {
            try {
                Socket client = socket.accept();
                delegate.onProxyStreamReady();
                if (client == null) {
                    continue;
                }
                Log.d(LOG_TAG, "client connected");

                WmCommonsDataUtil.getAudioStreams(this, remoteUrl, client, cahceRefference);

            } catch (SocketTimeoutException e) {
                // Do nothing
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to client", e);
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