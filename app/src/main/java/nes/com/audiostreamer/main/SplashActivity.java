package nes.com.audiostreamer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;

import nes.com.audiostreamer.R;
import nes.com.audiostreamer.model.gson.MwJsonObject;
import nes.com.audiostreamer.server.MwAPIInterface;
import nes.com.audiostreamer.server.RandomCategoryCallback;
import nes.com.audiostreamer.util.data.WmCommonsDataUtil;
import retrofit2.Call;


/**
 * Created by nesli on 17.10.2016.
 */

public class SplashActivity extends Activity implements RandomCategoryCallback{
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
        WmCommonsDataUtil.getRandomCategory(this);
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

    @Override
    public void onSuccess(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
        startApplication();
    }

    @Override
    public void onError() {
        replaceToast(Constant.NETWORK_FAILURE_MESSAGE);
    }

        /*private void recordQueryResponse() {
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

    }*/
}
