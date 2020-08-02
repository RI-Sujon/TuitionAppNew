package com.example.tuitionapp_surji.notification_pack;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        System.out.println("toooooooooooooooooooooo\n\nkkkkkkkk kkkkkkkkkkkkkkkkkk\n\neeeeeeeeeeeeeeeeeeennnnnnnnnnn");
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            TokenInfo token = new TokenInfo(refreshToken) ;

            FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);
        }
    }
}
