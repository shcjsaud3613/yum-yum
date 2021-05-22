package com.omnyom.yumyum.interfaces;

import com.example.messengerapp.Notifications.Sender;
import com.omnyom.yumyum.model.eureka.EurekaResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISerivce {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAATe-xivM:APA91bFrABS7QKSxLlLYrVHHlqtIgCX2AGgW4T2c9tVSBBDyGdN74tdEh8poESgabZuwyGRAJeePHsfhGp3uYMQAKD43IPBypTnFFsjBQRN1-32R06m1eiyEjbRLkyIx26-VsxYiJvy7"
    })

    @POST("fcm/send")
    Call<EurekaResponse> sendNotification(@Body Sender body);
}
