package com.example.tuitionapp_surji.notification_pack;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tuitionapp_surji.tuition_post.TuitionPostViewSinglePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotification {
    private String receiverId, title, body ;

    private DatabaseReference myRefNotificationToken ;
    private FirebaseUser firebaseUser ;

    private APIService apiService ;

    public SendNotification(String receiverId, String title, String body) {
        this.receiverId = receiverId;
        this.title = title;
        this.body = body;
    }

    public void sendNotificationOperation(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefNotificationToken = FirebaseDatabase.getInstance().getReference("Notification").child("Tokens") ;
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        myRefNotificationToken.child(receiverId).child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userToken = dataSnapshot.getValue(String.class);
                sendNotifications(userToken);
                System.out.println("\nuserToken: " + userToken);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }) ;
        updateToken();
    }

    public void sendNotifications(String userToken){
        Data data = new Data(title,body);
        NotificationSender sender = new NotificationSender(data, userToken) ;
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        System.out.println("Failed");
                    }
                }else {
                    System.out.println("Failed ResponseCode");
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
            }
        });
    }

    public void updateToken(){
        String refreshToken = FirebaseInstanceId.getInstance().getToken() ;
        TokenInfo token = new TokenInfo(refreshToken) ;
        myRefNotificationToken.child(firebaseUser.getUid()).setValue(token) ;
    }
}
