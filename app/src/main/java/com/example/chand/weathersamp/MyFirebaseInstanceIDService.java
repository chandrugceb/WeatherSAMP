package com.example.chand.weathersamp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by chand on 05-11-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.v("Victor", "Refreshed Token : "+refreshToken);
        sendRegistrationToken(refreshToken);
    }
    private void sendRegistrationToken(String token){
        //Add custom code to save the registration id to Firebase DB
    }
}
