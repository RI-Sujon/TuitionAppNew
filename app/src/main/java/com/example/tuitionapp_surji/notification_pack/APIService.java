package com.example.tuitionapp_surji.notification_pack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA7CviImg:APA91bGKClI6EmeaLCMEAVAjkE-9R5zfFYh6NcNrDviQ-zPZ32fmTb73kbk2fbTMrIqnmkgVbOTHWY2ZlaiCxMAfj7RqA2HhB8mLnDETj1kSG96mbQ-Da8BO7fi1elMoip69Zo7qR6uI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}

