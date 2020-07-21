package com.example.tuitionapp_surji.notification_pack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVAvHVjI:AAAA7CviImg:APA91bEGe7vLW8lTfKlRK-HsZdlF9TlK-AAAA7CviImg:APA91bEGe7vLW8lTfKlRK-HsZdlF9TlK-Ra1TiOpJM38g5hMewBTcCN9Kc1P7Wyl3cj1zyxy5wFORFFq6DkQXLH8UytBxffXRhgudwvMrtIiP5O5Ydgidtxq9CKrZsYQO8mrLfcfY25r"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}

