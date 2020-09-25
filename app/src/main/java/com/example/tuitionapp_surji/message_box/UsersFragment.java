package com.example.tuitionapp_surji.message_box;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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


public class UsersFragment extends Fragment
{


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<MessageBoxInfo> mUsers;
    private String checkUser;
    private String tutorName;
    private ArrayList<String> userInfo ;
    private TextView no_users;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;



    public UsersFragment(String checkUser, String tutorName, ArrayList<String> userInfo) {
        this.checkUser=checkUser;
        this.tutorName=tutorName;
        this.userInfo=userInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_users,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        no_users =  view.findViewById(R.id.no_users);


        mUsers = new ArrayList<>();
        readUsers();

        return view;
    }

    private void readUsers()
    {
          firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
          reference= FirebaseDatabase.getInstance().getReference("MessageBox");

          reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    MessageBoxInfo user= snapshot.getValue(MessageBoxInfo.class);

                    //System.out.println("User's Tutor Email ="+ user.getTutorEmail());

                  /*  assert user !=null;
                    assert  firebaseUser != null;
*/

                    if(checkUser.equals("guardian")){
                        if(user.getGuardianUid().equals(firebaseUser.getUid()))
                        {
                            if(user.isMessageFromGuardianSide() && user.isMessageFromTutorSide())
                                mUsers.add(user);
                        }
                    }

                    else if(checkUser.equals("tutor"))
                    {
                       // System.out.println("AAAAAAAAAAAAAAAAAAAAA = "+ user.getTutorUid());
                        //System.out.println("BBBBBBBBBBBBBBBBBBBBB = "+ firebaseUser.getUid());
                        if(user.getTutorUid().equals(firebaseUser.getUid()))
                        {
                            if(user.isMessageFromGuardianSide() && user.isMessageFromTutorSide())
                                mUsers.add(user);
                        }
                    }

                   /* if(!user.getGuardianUid().equals(firebaseUser.getUid())){
                        mUsers.add(user);

                    }*/
                }

                userAdapter = new UserAdapter(getContext(), mUsers,checkUser,true,userInfo) ;
                recyclerView.setAdapter(userAdapter);

                if(mUsers.size()==0)
                    no_users.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

