package com.example.tuitionapp_surji.message_box;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuitionapp_surji.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MessageRequestsFragment extends Fragment {


    RecyclerView message_request_recycler_view;
    private List<MessageBoxInfo> mUsers;
    private MessageRequestAdapter messageRequestAdapter;
    private String checkUser;
    private String tutorName;
    ArrayList<String> userInfo ;


    public MessageRequestsFragment(String checkUser, String tutorName, ArrayList<String> userInfo) {
        this.checkUser=checkUser;
        this.tutorName=tutorName;
        this.userInfo=userInfo;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_message_requests,container,false);

        message_request_recycler_view = view.findViewById(R.id.recycler_view_message_request);
        message_request_recycler_view.setHasFixedSize(true);
        message_request_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
       readRequests();

        return view;

    }


    private void readRequests() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("MessageBox");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    MessageBoxInfo user = snapshot.getValue(MessageBoxInfo.class);

                    assert user !=null;
                    assert  firebaseUser != null;


                    if(checkUser.equals("guardian")){
                        if(user.getGuardianUid().equals(firebaseUser.getUid()))
                        {
                            if(!user.isMessageFromGuardianSide())
                                mUsers.add(user);
                        }
                    }

                    else if(checkUser.equals("tutor")){
                        // System.out.println("AAAAAAAAAAAAAAAAAAAAA = "+ user.getTutorUid());
                        //System.out.println("BBBBBBBBBBBBBBBBBBBBB = "+ firebaseUser.getUid());
                        if(user.getTutorUid().equals(firebaseUser.getUid()))
                        {
                            if(!user.isMessageFromTutorSide())
                                mUsers.add(user);
                        }
                    }

                   /* if(!user.getGuardianUid().equals(firebaseUser.getUid())){
                        mUsers.add(user);

                    }*/
                }

                messageRequestAdapter = new MessageRequestAdapter(getContext(), mUsers,checkUser,true,userInfo) ;
                message_request_recycler_view.setAdapter(messageRequestAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}