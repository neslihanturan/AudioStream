package nes.com.audiostreamer.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.server.MwAPIInterface;
import nes.com.audiostreamer.server.RetrofitServiceCache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nesli on 17.10.2016.
 */

public class SplashActivity extends Activity {
    private ArrayList<String> categoryList;
    private Toast toast;
    private int pendingRequests;
    private MwAPIInterface mwAPIService;
    @NonNull
    private Call<MwJsonObject> queryResponse ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        categoryList = new ArrayList<>();

        initDataClient();
        getCategoryData();
        queryResponse.enqueue(new Callback<MwJsonObject>() {
            @Override
            public void onResponse(Call<MwJsonObject> call, Response<MwJsonObject> response) {
                replaceToast(Constant.NETWORK_SUCCESS_MESSAGE);
                for(String key: response.body().getQuery().getPages().keySet()){
                    Log.d("i","gson "+response.body().getQuery().getPages().get(key).getTitle());
                    categoryList.add(response.body().getQuery().getPages().get(key).getTitle());
                }
                startApplication();
            }

            @Override
            public void onFailure(Call<MwJsonObject> call, Throwable t) {
                replaceToast(Constant.NETWORK_FAILURE_MESSAGE);
            }
        });
    }

    public void getCategoryData(){
        recordQueryRequest();
        queryResponse = mwAPIService.getRelevantCategories("");
    }

    private void initDataClient() {
        mwAPIService = RetrofitServiceCache.getService();
    }


    public void startApplication(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putStringArrayListExtra("category_list", categoryList);
        startActivity(intent);
        finish();
    }
    private void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void recordQueryResponse() {
        --pendingRequests;
        updateActivity();
    }

    private void recordQueryRequest() {
        ++pendingRequests;
        updateActivity();
    }
    private void updateActivity() {
        if(pendingRequests > 0){
            return;
        }
        else{
            startApplication();
        }

    }
}
