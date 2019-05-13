package com.example.grilledfood.Remote;


import com.example.grilledfood.Model.MyResponse;
import com.example.grilledfood.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAapRm6lo:APA91bGai7KA2LSHMbFApTl2UROBGgq6Odes0F4xvrn1-nhdPRGTI0tA3g32D31ndqxqIi_Z_MomOf0W0exKOARLJlq2GAIlkKUlHMk08wQ6kZkVeTV-xfuNNiPdopy7cmtr6AdCakFQ"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
