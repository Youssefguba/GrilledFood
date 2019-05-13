package com.example.grilledfood.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.grilledfood.Model.User;
import com.example.grilledfood.Remote.APIService;
import com.example.grilledfood.Remote.RetrofitClient;

public class Common {
    public static User mCurrentUser;

    public static String topicName = "News";


    public static final String baseURL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetrofitClient.getClient(baseURL).create(APIService.class);
    }

    public static String PHONE_TEXT = "userPhone";

    public static String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";

        else if (status.equals("1"))
            return "On my way .. ";

        else
            return "Shipeed";
    }

    public static  boolean isConnectionToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null ) {
                for (int i=0; i<info.length; i++) {
                        if(info[i].getState() == NetworkInfo.State.CONNECTED)
                            return true;
                }
            }
        }
        return false;
    }


    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";


}
