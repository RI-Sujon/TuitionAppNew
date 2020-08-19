package com.example.tuitionapp_surji.notification_pack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVAvHVjI:APA91bGqMJUN_9YSn1m1ehduBuFgyKYH2M6s9VlvSNrWtGOtlTh5e7Uc7Ba48McYyK02T9PsCI8bsEeyvdpUgGBEOCpuAhmVlfFLn2Ybah2Iv_9w37Ink2mHf2wElrXGWt1FEqzG288Z"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}

